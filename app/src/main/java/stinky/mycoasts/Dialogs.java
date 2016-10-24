package stinky.mycoasts;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.ui.ViewHolder.AccountViewHolder;

public class Dialogs {

    public static class Tags{
        public static final String createAccount = "create_account_dialog";
        public static final String selectAccount = "select_account_dialog";
    }

    public static MyDialog createAccount(Context context, MyDialog.OnClickListener onCreate){
        final MyDialog dialog = MyDialog.getInstance(context);
        dialog.setContent(R.layout.dialog_create_account);
        dialog.setTitle(R.string.dialog_create_account_title);
        dialog.setPositiveButton(R.string.action_create, onCreate);
        return dialog;
    }

    public static MyDialog selectAccount(final Context context, List<Account> accountList, final MyDialog.OnClickListener onCreateAccount, final ListDialogAdapter.OnItemClickListener onSelectAccount){
        final ListDialog dialog = new ListDialog();
        dialog.setContext(context);
        dialog.setTitle("Выберите счет");
        ListDialogAdapter<Account, AccountViewHolder> adapter = new ListDialogAdapter<>(accountList,AccountViewHolder.class,R.layout.item_select_account_list);
        adapter.setOnItemClickListener(new ListDialogAdapter.OnItemClickListener() {
            @Override
            public void onClick(ListDialogAdapter parent, View view, Object selectedObject, int position) {
                dialog.dismiss();
                onSelectAccount.onClick(parent,view,selectedObject,position);
            }
        });
        dialog.setAdapter(adapter);
        dialog.setPositiveButton(R.string.action_create, new MyDialog.OnClickListener() {
            @Override
            public void onClick(MyDialog d) {
                d.dismiss();
                createAccount(context,onCreateAccount).show(Tags.createAccount);
            }
        });
        return dialog;
    }

    public static DialogFragment showMessage(Context context, String message){
        final MyDialog dialog = new MyDialog();
        dialog.setContext(context);
        dialog.setMessage(message);
        return dialog;
    }

    @SuppressWarnings("all")
    public static class MyDialog extends DialogFragment {

//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.dialog_create_account, container, false);
//            getDialog().setTitle(R.string.dialog_create_account_title);
//            return rootView;
//        }

        public interface OnClickListener{
            void onClick(MyDialog d);
        }

        protected Context context;
        private   int       layoutId;
        protected Dialog d;
        private   View      content;

        protected OnClickListener positiveListener;
        protected OnClickListener negativeListener;
        protected String positiveText;
        protected String negativeText;

        private String  titleText;
        private float   titleSize = 0;
        private int     titleColor = 0;

        private final String btnOkDefaultText = "Ок";
        private final String btnCancelDefaultText = "Отмена";

        private boolean regForContextMenu = false;

        private String message;

        private Button btnOk;
        private Button btnCancel;
        private boolean btnEnabled = true;

        public static MyDialog getInstance(Context context){
            MyDialog d = new MyDialog();
            d.setContext(context);
            return d;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (content == null){
                setContent(layoutId);
            }
            return super.onCreateDialog(savedInstanceState);
        }

//        @Override
//        public void onSaveInstanceState(Bundle outState) {
//            super.onSaveInstanceState(outState);
//            outState.putInt("layoutId", layoutId);
//        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setRetainInstance(true);
            Log.d("asd", "asd");
            if (content == null && message == null){
                throw new RuntimeException("Content is null");
            }
            if (context == null){
                throw new RuntimeException("Context is null");
            }
            d = getDialog();
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View dialogView = inflater.inflate(R.layout.dialog_template, null);

            Rect displayRectangle = new Rect();
            Window window = ((Activity) context).getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));


            if (positiveText != null && !positiveText.isEmpty()) {
                dialogView.findViewById(R.id.dialog_button_layout).setVisibility(View.VISIBLE);
                btnOk = (Button) dialogView.findViewById(R.id.dialog_button_ok);
                btnOk.setVisibility(View.VISIBLE);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        positiveListener.onClick(MyDialog.this);
                    }
                });
                btnOk.setText(positiveText);
                if (regForContextMenu){
                    ((Activity)context).registerForContextMenu(btnOk);
                }
                btnOk.setEnabled(btnEnabled);
            }
            if (negativeText != null && !negativeText.isEmpty()) {
                dialogView.findViewById(R.id.dialog_button_layout).setVisibility(View.VISIBLE);
                btnCancel = (Button) dialogView.findViewById(R.id.dialog_button_cancel);
                btnCancel = (Button) dialogView.findViewById(R.id.dialog_button_cancel);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        negativeListener.onClick(MyDialog.this);
                    }
                });
                btnCancel.setText(negativeText);
                btnCancel.setEnabled(btnEnabled);
            }

            if (titleText != null && !titleText.isEmpty()) {
                TextView title = (TextView) dialogView.findViewById(R.id.dialog_text_title);
                title.setVisibility(View.VISIBLE);
                title.setText(titleText);
                if (titleSize > 0) {
                    title.setTextSize(titleSize);
                }
                if (titleColor > 0) {
                    title.setTextColor(getResources().getColor(titleColor));
                }
            }

            if (message != null){
                TextView messageTv = (TextView) findViewById(R.id.message);
                messageTv.setVisibility(View.VISIBLE);
                messageTv.setText(message);
            } else {
                LinearLayout llContent = (LinearLayout) dialogView.findViewById(R.id.dialog_content);
                if (content != null && content.getParent() != null) {
                    ((ViewGroup) content.getParent()).removeView(content);
                }
                llContent.addView(content);
                llContent.setVisibility(View.VISIBLE);
            }
            return dialogView;

        }
//        public void setContentView(View view){
//            d.setContentView(view);
//        }

//        public void setContent(View content) {
//            this.content = content;
//        }

        public void setContent(int layoutId) {
            this.layoutId = layoutId;
            this.content = ((Activity) context).getLayoutInflater().inflate(layoutId, null);
        }

//        public void setLayout(int layoutId) {
//            this.layoutId = layoutId;
//        }

        public void setTitle(String text) {
            titleText = text;
        }
        public void setTitle(int textId) {
            titleText = context.getString(textId);
        }

        public void setTitleColor(int color) {
            titleColor = color;
        }

        public void setPositiveButton(int textId, OnClickListener l) {
            positiveText = context.getString(textId);
            positiveListener = l;
        }

        public void setPositiveButton(String text, OnClickListener l) {
            positiveText = text;
            positiveListener = l;
        }

        public void setNegativeButton(OnClickListener l) {
            negativeText = btnCancelDefaultText;
            negativeListener = l;
        }

        public void setNegativeButton(String text) {
            negativeText = text;
            negativeListener = new OnClickListener() {
                @Override
                public void onClick(MyDialog d) {
                    MyDialog.this.dismiss();
                }
            };
        }

        public void setNegativeButton(String text, OnClickListener l) {
            negativeText = text;
            negativeListener = l;
        }

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }

        public void setContext(Context context) {
            this.context = context;
            this.content = new LinearLayout(context);
        }
        public Context getContext(){
            return  context;
        }
        public View getContent() {
            return content;
        }

        public View findViewById(int id) {
            return content.findViewById(id);
        }
        public void registerForContextMenu(){
            regForContextMenu = true;
        }

        public void openContextMenu(){
            if (btnOk != null) ((Activity)context).openContextMenu(btnOk);
        }

        public void show(String tag) {
            FragmentTransaction ft = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }

        public void setTitleTextSize(float size){
            titleSize = size;
        }

        public void setButtonEnabled(boolean enabled){
            btnEnabled = enabled;
            if (btnOk != null){
                btnOk.setEnabled(enabled);
            }
            if (btnCancel != null){
                btnCancel.setEnabled(enabled);
            }
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

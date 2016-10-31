package stinky.mycoasts;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Category;
import stinky.mycoasts.model.tools.HelperFactory;
import stinky.mycoasts.ui.MainActivity;
import stinky.mycoasts.ui.ViewHolder.AccountViewHolder;
import stinky.mycoasts.view.ErrorView;

public class Dialogs {

    public static class Tags{
        public static final String createAccount = "create_account";
        public static final String selectAccount = "select_account";
        public static final String ADD_COST = "add_coast";
        public static final String EXCEPTION = "exception";
    }

    public static MyDialog createAccount(Context context, MyDialog.OnClickListener onCreate){
        final MyDialog dialog = MyDialog.getInstance(context);
        dialog.setContent(R.layout.dialog_create_account);
        dialog.setTitle(R.string.dialog_create_account_title);
        dialog.setPositiveButton(R.string.action_create, onCreate);
        return dialog;
    }

    public static MyDialog selectAccount(final Context context, List<Account> accountList, final MyDialog.OnClickListener onCreateAccount, final ListAdapter.OnItemClickListener onSelectAccount){
        final ListDialog dialog = new ListDialog();
        dialog.setContext(context);
        dialog.setTitle("Выберите счет");
        ListAdapter<Account, AccountViewHolder> adapter = new ListAdapter<>(accountList,AccountViewHolder.class,R.layout.item_select_account_list);
        adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onClick(ListAdapter parent, View view, Object selectedObject, int position) {
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
        dialog.setCancelable(false);
        return dialog;
    }

    public static MyDialog addCoast(Context context, final ErrorView errorView, final MyDialog.OnClickListener addCoast){
        final MyDialog dialog = MyDialog.getInstance(context);
        dialog.setTitle("Добавить");
        dialog.setContent(R.layout.dialog_add_coast);
        AutoCompleteTextView subCategoryTv = (AutoCompleteTextView) dialog.findViewById(R.id.subcategory_name);
        final AutoCompleteTextView categoryTv = (AutoCompleteTextView) dialog.findViewById(R.id.category_name);
        final SimpleCursorAdapter subCategoryAdapter = new SimpleCursorAdapter(context, R.layout.item_autocomplete_subcategory, null, new String[]{"scn","cn"}, new int[]{R.id.subCategory, R.id.category}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        final SimpleCursorAdapter categoryAdapter = new SimpleCursorAdapter(context, R.layout.item_autocomplete_category, null, new String[]{"name"}, new int[]{R.id.category}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        subCategoryAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                if (charSequence == null || charSequence.length() == 0){
                    return null;
                }
                Cursor c = null;
                try {
                    c = HelperFactory.getHelper().getCursorFindSubCategory(charSequence.toString());
                } catch (SQLException e) {
                    errorView.onError(e);
                }
                return c;
            }
        });
        categoryAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(2);
            }
        });

        categoryAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                if (charSequence == null || charSequence.length() == 0){
                    return null;
                }
                Cursor c = null;
                try {
                    c = HelperFactory.getHelper().getCursorFindCategory(charSequence.toString());
                } catch (SQLException e) {
                    errorView.onError(e);
                }
                return c;
            }
        });
        subCategoryAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(1);
            }
        });


        subCategoryTv.setAdapter(subCategoryAdapter);
        categoryTv.setAdapter(categoryAdapter);
        subCategoryTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                categoryTv.setText(subCategoryAdapter.getCursor().getString(2));
            }
        });
        dialog.setPositiveButton(R.string.action_income, new MyDialog.OnClickListener() {
            @Override
            public void onClick(MyDialog d) {
                dialog.setData(1);
                addCoast.onClick(d);
            }
        });
        dialog.setNegativeButton(R.string.action_outcome, new MyDialog.OnClickListener() {
            @Override
            public void onClick(MyDialog d) {
                dialog.setData(-1);
                addCoast.onClick(d);
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

        private Object data;

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
        public void setNegativeButton(int textId, OnClickListener l) {
            negativeText = context.getString(textId);
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

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}

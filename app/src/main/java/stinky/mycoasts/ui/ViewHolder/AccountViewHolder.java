package stinky.mycoasts.ui.ViewHolder;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;

import stinky.mycoasts.ListDialogAdapter;
import stinky.mycoasts.R;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.tools.HelperFactory;

public class AccountViewHolder extends ListDialogAdapter.ViewHolder<Account> {
    TextView name;
    TextView amount;
    ImageView delete;

    public AccountViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.account_name);
        amount = (TextView) itemView.findViewById(R.id.account_amount);
        delete = (ImageView) itemView.findViewById(R.id.account_delete);
    }

    @Override
    public void bind(final Account obj) {
        name.setText(obj.getName());
        amount.setText(String.valueOf(obj.getAmount()));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    HelperFactory.getHelper().getAccountDao().delete(obj);
                    getRoot().setVisibility(View.GONE);
                    Snackbar.make(view, R.string.action_deleted, Snackbar.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    Snackbar.make(view, R.string.error_delete_account, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}

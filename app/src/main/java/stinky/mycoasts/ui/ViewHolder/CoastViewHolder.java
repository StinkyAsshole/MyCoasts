package stinky.mycoasts.ui.ViewHolder;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;

import stinky.mycoasts.ListAdapter;
import stinky.mycoasts.R;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.tools.HelperFactory;

public class CoastViewHolder extends ListAdapter.ViewHolder<Coast> {

    TextView category;
    TextView amount;

    public CoastViewHolder(View itemView) {
        super(itemView);
        category = (TextView) itemView.findViewById(R.id.category);
        amount = (TextView) itemView.findViewById(R.id.amount);
    }

    @Override
    public void bind(final Coast obj) {
        amount.setText(String.valueOf(obj.getAmount()));
        category.setText(obj.getSubCategory().getName());
    }
}

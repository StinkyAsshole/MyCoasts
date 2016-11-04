package stinky.mycoasts.ui.ViewHolder;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.Calendar;

import stinky.mycoasts.ListAdapter;
import stinky.mycoasts.R;
import stinky.mycoasts.model.entity.Account;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.tools.DateUtils;
import stinky.mycoasts.model.tools.HelperFactory;

public class CoastViewHolder extends ListAdapter.ViewHolder<Coast> {

    TextView category;
    TextView amount;
    TextView date;

    public CoastViewHolder(View itemView) {
        super(itemView);
        category = (TextView) itemView.findViewById(R.id.category);
        amount = (TextView) itemView.findViewById(R.id.amount);
        date = (TextView) itemView.findViewById(R.id.date);
    }

    @Override
    public void bind(final Coast obj) {
        int dateVisibility;
        int position = getAdapterPosition();

        if (position == 0){
            dateVisibility = View.VISIBLE;
        } else {
            Coast prevObj = (Coast) getAdapter().getItem(position-1);
            if(DateUtils.compare(obj.getDate(), prevObj.getDate(), DateUtils.Unit.DAY) == 0){
                dateVisibility = View.INVISIBLE;
            } else {
                dateVisibility = View.VISIBLE;
            }
        }
        amount.setText(String.format(getRoot().getResources().getString(R.string.amount_rub), obj.getAmount()));
        category.setText(obj.getSubCategory().getName());
        date.setText(DateUtils.toString(obj.getDate()));
        date.setVisibility(dateVisibility);
    }
}

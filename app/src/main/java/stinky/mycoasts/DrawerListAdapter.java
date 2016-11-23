package stinky.mycoasts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stinky.mycoasts.model.entity.Category;
import stinky.mycoasts.model.entity.PersistEntity;
import stinky.mycoasts.model.entity.SubCategory;


public class DrawerListAdapter extends RecyclerView.Adapter {
    private final int TYPE_CATEGORY = 0;
    private final int TYPE_SUB_CATEGORY = 1;
    List<? super PersistEntity> items;

    public DrawerListAdapter(List<Category> items){
        this.items = new ArrayList<>();
        for (Category cat: items) {
            this.items.add(cat);
            this.items.addAll(cat.getSubCategories());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof Category ? TYPE_CATEGORY : TYPE_SUB_CATEGORY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                viewType == TYPE_CATEGORY ?
                        R.layout.item_category_drawer_list :
                        R.layout.item_subcategory_drawer_list,
                parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        if (getItemViewType(position) == TYPE_CATEGORY){
            Category cat = (Category ) items.get(position);
            vh.name.setText(cat.getName());
        } else {
            SubCategory sub = (SubCategory) items.get(position);
            vh.name.setText(sub.getName());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}

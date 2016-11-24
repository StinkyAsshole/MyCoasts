package stinky.mycoasts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import stinky.mycoasts.model.entity.Category;
import stinky.mycoasts.model.entity.PersistEntity;
import stinky.mycoasts.model.entity.SubCategory;


public class DrawerListAdapter extends RecyclerView.Adapter {
    private final int TYPE_CATEGORY = 0;
    private final int TYPE_SUB_CATEGORY = 1;
    List<ItemHolder> items;
    List<ItemHolder> filteredItems;
    Set<Integer> collapseIdList = new HashSet<>();

    public DrawerListAdapter(List<Category> items){
        this.items = new ArrayList<>();
        filteredItems = new ArrayList<>();
        for (Category cat: items) {
            ItemHolder ih = new ItemHolder(cat,TYPE_CATEGORY);
            this.items.add(ih);
            filteredItems.add(ih);
            collapseIdList.add(cat.getId());
            for (SubCategory sub: cat.getSubCategories()) {
                this.items.add(new ItemHolder(sub, TYPE_SUB_CATEGORY));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return filteredItems.get(position).type;
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
        final ItemHolder itemHolder = filteredItems.get(position);
        if (getItemViewType(position) == TYPE_CATEGORY){
            Category cat = (Category ) itemHolder.item;
            vh.name.setText(cat.getName());
        } else {
            SubCategory sub = (SubCategory) itemHolder.item;
            vh.name.setText(sub.getName());
        }
        vh.checkBox.setOnCheckedChangeListener(null);
        vh.checkBox.setChecked(filteredItems.get(position).isChecked);
        vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                itemHolder.isChecked = b;
                if (itemHolder.type == TYPE_CATEGORY) {
                    for (ItemHolder ih : items) {
                        if (ih.type == TYPE_SUB_CATEGORY){
                            SubCategory sub = (SubCategory )ih.item;
                            if (sub.getCategory().getId().equals(itemHolder.item.getId())){
                                ih.isChecked = b;
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

        if (vh.icon != null){
            vh.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = itemHolder.item.getId();
                    if(collapseIdList.contains(id)){
                        collapseIdList.remove(id);
                    } else {
                        collapseIdList.add(id);
                    }

                    filteredItems = new ArrayList<>();
                    for (ItemHolder ih: items) {
                        if (ih.type == TYPE_CATEGORY){
                            if (!collapseIdList.contains(ih.item.getId())){
                                // TODO: 24.11.2016 сменить иконку стрелочки
                            } else {
                                // TODO: 24.11.2016 сменить иконку стрелочки
                            }
                            filteredItems.add(ih);
                        } else {
                            SubCategory sub = (SubCategory )ih.item;
                            if (!collapseIdList.contains(sub.getCategory().getId())) {
                                filteredItems.add(ih);
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CheckBox checkBox;
        MaterialIconView icon;
        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            icon = (MaterialIconView) itemView.findViewById(R.id.icon);
        }
    }

    private class ItemHolder{
        PersistEntity item;
        int type;
        boolean isChecked;

        ItemHolder(PersistEntity item, int type){
            this.item = item;
            this.type = type;
            isChecked = true;
        }
    }
}

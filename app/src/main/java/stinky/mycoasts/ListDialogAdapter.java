package stinky.mycoasts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class ListDialogAdapter<I,V extends ListDialogAdapter.ViewHolder> extends RecyclerView.Adapter<V>{
    private ArrayList<I> list;
    protected OnItemClickListener listener;
    private Class<V> clazz;
    private int layoutId;

    interface OnItemClickListener{
        void onClick(ListDialogAdapter parent, View view, int position);
    }

    public ListDialogAdapter(){};
    public ListDialogAdapter(ArrayList<I> list, Class<V> clazz, int layoutId){
        this.list = list;
        this.clazz = clazz;
        this.layoutId = layoutId;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

        V vh = null;
        try {
            vh = clazz.getConstructor(View.class).newInstance(v);
        } catch (Exception e) {
            //TODO Обработтаь
            e.printStackTrace();
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ListDialogAdapter.ViewHolder holder, final int position) {
        final V vh = (V) holder;

        vh.bind(list.get(position));
            if (listener != null){
                vh.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(ListDialogAdapter.this, view,position);
                    }
                });
            }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public abstract static class ViewHolder<I> extends RecyclerView.ViewHolder {
        View root;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
        }

        abstract public void bind(I obj);
    }
}
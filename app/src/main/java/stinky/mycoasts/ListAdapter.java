package stinky.mycoasts;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import stinky.mycoasts.model.entity.PersistEntity;
import stinky.mycoasts.ui.MainActivity;

public class ListAdapter<I extends PersistEntity,V extends ListAdapter.ViewHolder> extends RecyclerView.Adapter<V>{
    private List<I> list;
    protected OnItemClickListener listener;
    private Class<V> clazz;
    private int layoutId;
//    private OnListEnd onListEnd;

    public interface OnItemClickListener{
        void onClick(ListAdapter parent, View view, PersistEntity selectedObject, int position);
    }
//    public interface OnListEnd{
//        void onListEnd();
//    }

//    public ListAdapter(){};
    public ListAdapter(List<I> list, Class<V> clazz, int layoutId){
        this.list = list;
        this.clazz = clazz;
        this.layoutId = layoutId;
    }

    public List<I> getItems(){
        return list;
    }

    public void addItems(List<I> newList, boolean toStart){
        if (toStart){
            list.addAll(0, newList);
        }else {
            list.addAll(newList);
        }
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

    public void remove(PersistEntity obj){
        list.remove(obj);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, final int position) {
        final V vh = (V) holder;
        vh.setAdapter(this);
        vh.bind(list.get(position));
            if (listener != null){
                vh.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(ListAdapter.this, view, list.get(position), position);
                    }
                });
            }
//        if (onListEnd != null && position == list.size() - 1 ){
//            onListEnd.onListEnd();
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public I getItem(int position){
        return list.get(position);
    }

    public abstract static class ViewHolder<I> extends RecyclerView.ViewHolder {
        private ListAdapter adapter;
        private View root;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
        }

        abstract public void bind(I obj);

        public View getRoot() {
            return root;
        }

        public ListAdapter getAdapter() {
            return adapter;
        }
        protected void setAdapter(ListAdapter adapter){
            this.adapter = adapter;
        }
    }
//
//    public void setOnListEnd(OnListEnd onListEnd) {
//        this.onListEnd = onListEnd;
//    }
}
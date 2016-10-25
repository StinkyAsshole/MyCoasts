package stinky.mycoasts;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ListDialog extends Dialogs.MyDialog {
    private RecyclerView listView;
    private ListAdapter adapter;


    public void setAdapter(ListAdapter adapter){
        setContent(R.layout.dialog_list);
        listView = (RecyclerView )findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = adapter;
        listView.setAdapter(adapter);
    }

    public void setOnItemClickListener(ListAdapter.OnItemClickListener listener){
        adapter.setOnItemClickListener(listener);
    }

}

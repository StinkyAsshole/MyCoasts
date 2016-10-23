package stinky.mycoasts;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ListDialog extends Dialogs.MyDialog {
    private RecyclerView listView;
    private ListDialogAdapter adapter;


    public void setAdapter(ListDialogAdapter adapter){
        setContent(R.layout.dialog_list);
        listView = (RecyclerView )findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = adapter;
        listView.setAdapter(adapter);
    }

    public void setOnItemClickListener(ListDialogAdapter.OnItemClickListener listener){
        adapter.setOnItemClickListener(listener);
    }

}

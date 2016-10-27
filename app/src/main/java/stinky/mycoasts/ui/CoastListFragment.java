package stinky.mycoasts.ui;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import stinky.mycoasts.ListAdapter;
import stinky.mycoasts.R;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.ui.ViewHolder.CoastViewHolder;

public class CoastListFragment extends Fragment {

    private static final String TAG = "CoastListFragment";
    public static final String KEY_LIST = "list";

    protected RecyclerView mRecyclerView;
    protected ListAdapter<Coast, CoastViewHolder> adapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_coast_list, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        List<Coast> list = new ArrayList<>();
        Bundle args = getArguments();

        if (args != null){
            list = (List<Coast>) args.getSerializable(KEY_LIST);
        }

        adapter = new ListAdapter<>(list, CoastViewHolder.class, R.layout.item_coast_list);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        return rootView;
    }
}
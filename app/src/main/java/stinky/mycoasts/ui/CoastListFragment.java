package stinky.mycoasts.ui;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import stinky.mycoasts.ListAdapter;
import stinky.mycoasts.R;
import stinky.mycoasts.model.entity.Coast;
import stinky.mycoasts.model.tools.DateUtils;
import stinky.mycoasts.presenters.AccountPresenter;
import stinky.mycoasts.ui.ViewHolder.CoastViewHolder;

public class CoastListFragment extends Fragment {

    public static final String TAG = "CoastListFragment";
    public static final String KEY_LIST = "list";
    public static final String KEY_PAGE = "page";
    public static final String KEY_ACCOUNT_ID = "account_ID";

    protected RecyclerView mRecyclerView;
    protected ListAdapter<Coast, CoastViewHolder> adapter;
    protected LinearLayoutManager mLayoutManager;

    private TextView date;
    private int lastVisiblePosition = 0;
    private int lastPage = 0;
    private int accountId = 0;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_coast_list, container, false);

//        date = (TextView) rootView.findViewById(R.id.date);
        date = (TextView) getActivity().findViewById(R.id.date);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int visiblePosition = llm.findFirstVisibleItemPosition();
                if (visiblePosition != lastVisiblePosition){
                    date.setText(DateUtils.toString(adapter.getItem(visiblePosition).getDate()));
                    lastVisiblePosition = visiblePosition;
                }
            }
        });

        List<Coast> list = new ArrayList<>();
        Bundle args = getArguments();

        if (args != null ){
            list = (List<Coast>) args.getSerializable(KEY_LIST);
            if (list != null && !list.isEmpty()){
                date.setText(DateUtils.toString(list.get(0).getDate()));
            }
            lastPage = args.getInt(KEY_PAGE, 0);
            accountId = args.getInt(KEY_ACCOUNT_ID, 0);
        }

        adapter = new ListAdapter<>(list, CoastViewHolder.class, R.layout.item_coast_list);
//        adapter.setOnListEnd(new ListAdapter.OnListEnd() {
//            @Override
//            public void onListEnd() {
//                MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.accountPresenter.showCoastList(accountId, ++lastPage);
//            }
//        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0){
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading){
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount){
                            loading = false;
                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.accountPresenter.showCoastList(accountId, ++lastPage);
                            loading = true;
                        }
                    }
                }
            }
        });

        return rootView;
    }
}

package stinky.mycoasts.ui;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
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
import stinky.mycoasts.presenters.CoastPresenter;
import stinky.mycoasts.ui.ViewHolder.CoastViewHolder;
import stinky.mycoasts.view.AccountView;
import stinky.mycoasts.view.CoastView;

import static android.R.id.list;

public class CoastListFragment extends MvpAppCompatFragment implements CoastView {

    @InjectPresenter
    CoastPresenter coastPresenter;

    public static final String TAG = "CoastListFragment";
    public static final String KEY_ACCOUNT_ID = "account_ID";
    public static final String KEY_MONTH_DIF = "month_dif";

    protected RecyclerView mRecyclerView;
    protected ListAdapter<Coast, CoastViewHolder> adapter;
    protected LinearLayoutManager mLayoutManager;

    private TextView date;
    private int lastVisiblePosition = 0;
    private int monthDif;
    private int accountId;
    private List<Coast> coastList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_coast_list, container, false);

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

        Bundle args = getArguments();
        accountId = args.getInt(KEY_ACCOUNT_ID, 0);
        monthDif = args.getInt(KEY_MONTH_DIF, 0);

        coastPresenter.showCoastList(accountId, monthDif);

//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
//        {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
//            {
//                if(dy > 0){
//                    visibleItemCount = mLayoutManager.getChildCount();
//                    totalItemCount = mLayoutManager.getItemCount();
//                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//
//                    if (loading){
//                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount){
//                            loading = false;
//                            MainActivity mainActivity = (MainActivity) getActivity();
//                            mainActivity.accountPresenter.showCoastList(accountId, ++lastPage);
//                            loading = true;
//                        }
//                    }
//                }
//            }
//        });

        return rootView;
    }

    @Override
    public void onShowCoastList(List<Coast> list) {
        if (list != null && !list.isEmpty()) {
            date.setText(DateUtils.toString(list.get(0).getDate()));
        }
        adapter = new ListAdapter<>(list, CoastViewHolder.class, R.layout.item_coast_list);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}

package stinky.mycoasts.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import stinky.mycoasts.R;

public class CoastListFragment extends Fragment {

    public CoastListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_coast_list, container, false);
        TextView kek = (TextView) v.findViewById(R.id.kek);
        Bundle args = getArguments();

        if (args != null) {
            kek.setText(args.getString("text"));
        }
        return v;
    }

}

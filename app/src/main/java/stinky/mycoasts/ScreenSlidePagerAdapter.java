package stinky.mycoasts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.sql.SQLException;

import stinky.mycoasts.model.tools.DatabaseHelper;
import stinky.mycoasts.model.tools.DateUtils;
import stinky.mycoasts.model.tools.HelperFactory;
import stinky.mycoasts.presenters.AccountPresenter;
import stinky.mycoasts.ui.CoastListFragment;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private int accountId;
    private int currentPosition;

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CoastListFragment();
        Bundle arg = new Bundle();
        arg.putInt(CoastListFragment.KEY_ACCOUNT_ID, accountId);
        arg.putInt(CoastListFragment.KEY_MONTH_DIF, position);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public int getCount() {
        int i;
        try {
            i = HelperFactory.getHelper().getCoastDao().getMonthCount();
        } catch (SQLException e) {
            i = 0;
        }
        return i;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return DateUtils.now().minusMonth(position).getMonthString();
    }

    public void refresh() {

    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}

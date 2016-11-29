package stinky.mycoasts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import stinky.mycoasts.presenters.AccountPresenter;
import stinky.mycoasts.ui.CoastListFragment;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private int accountId;

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
        return 2;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

}

package stinky.mycoasts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import stinky.mycoasts.ui.CoastListFragment;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new CoastListFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

}

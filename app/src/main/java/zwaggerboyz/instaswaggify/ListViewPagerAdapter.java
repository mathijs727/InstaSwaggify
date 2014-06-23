package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Mathijs on 23/06/14.
 */
public class ListViewPagerAdapter extends FragmentStatePagerAdapter {
    private FilterListAdapter mFilterAdapter;
    private OverlayListAdapter mOverlayAdapter;

    public ListViewPagerAdapter(FragmentManager fm,
                                FilterListAdapter filterAdapter,
                                OverlayListAdapter overlayAdapter) {
        super(fm);

        mFilterAdapter = filterAdapter;
        mOverlayAdapter = overlayAdapter;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            FilterListFragment fragment = new FilterListFragment();
            fragment.setAdapter(mFilterAdapter);
            return fragment;
        } else {
            OverlayListFragment fragment = new OverlayListFragment();
            fragment.setAdapter(mOverlayAdapter);
            return fragment;
        }
    }
}
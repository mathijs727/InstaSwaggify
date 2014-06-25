package zwaggerboyz.instaswaggify.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    ListViewPager.java
 * This file manages the items in the filter and overlay tab
 */

public class ListViewPagerAdapter extends FragmentStatePagerAdapter {
    private FilterListAdapter mFilterAdapter;
    private OverlayListAdapter mOverlayAdapter;

    public static final int PAGE_FILTERS = 0;
    public static final int PAGE_OVERLAYS = 1;

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
    public CharSequence getPageTitle(int position) {
        if (position == PAGE_FILTERS) {
            return "Filters";
        } else if (position == PAGE_OVERLAYS) {
            return "Overlays";
        }
        return "";
    }

    @Override
    public Fragment getItem(int position) {
        if (position == PAGE_FILTERS) {
            FilterListFragment fragment = new FilterListFragment();
            fragment.setAdapter(mFilterAdapter);
            return fragment;
        } else if (position == PAGE_OVERLAYS) {
            OverlayListFragment fragment = new OverlayListFragment();
            fragment.setAdapter(mOverlayAdapter);
            return fragment;
        }
        return null;
    }
}

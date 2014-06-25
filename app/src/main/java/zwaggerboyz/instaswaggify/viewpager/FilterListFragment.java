package zwaggerboyz.instaswaggify.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobeta.android.dslv.DragSortListView;

import zwaggerboyz.instaswaggify.R;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    FilterListFragment.java
 * TODO:    ik weet niet wat dit doet
 */

public class FilterListFragment extends android.support.v4.app.Fragment {
    private FilterListAdapter mAdapter;

    public void setAdapter(FilterListAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);
        DragSortListView mListView = (DragSortListView)view.findViewById(R.id.fragment_listview_listview);

        mListView.setAdapter(mAdapter);

        mListView.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int item) {
                mAdapter.remove(item);
                mAdapter.notifyDataSetChanged();
            }
        });

        mListView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                mAdapter.reorder(from, to);
            }
        });

        mListView.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int which) {
                mAdapter.remove(which);
            }
        });

        mListView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                mAdapter.reorder(from, to);
            }
        });

        return view;
    }
}

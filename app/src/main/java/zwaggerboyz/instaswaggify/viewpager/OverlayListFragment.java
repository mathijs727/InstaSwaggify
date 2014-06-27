package zwaggerboyz.instaswaggify.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mobeta.android.dslv.DragSortListView;

import zwaggerboyz.instaswaggify.CanvasDraggableItem;
import zwaggerboyz.instaswaggify.R;
import zwaggerboyz.instaswaggify.dialogs.OverlaySettingsDialog;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    OverlayListFragment.java
 * This file manages the tab containing active overlays
 */

public class OverlayListFragment extends Fragment {
    private OverlayListAdapter mAdapter;

    public void setAdapter(OverlayListAdapter adapter) {
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
                mAdapter.notifyDataSetChanged();
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");

                if (prev != null)
                    fragmentTransaction.remove(prev);

                fragmentTransaction.addToBackStack(null);

                DialogFragment dialogFragment = new OverlaySettingsDialog((CanvasDraggableItem) adapterView.getItemAtPosition(i), new OverlaySettingsDialog.onSettingsChangedListener() {
                    @Override
                    public void onSettingsChanged() {
                        mAdapter.invalidateCanvas();
                    }
                });
                dialogFragment.show(getFragmentManager(), "Dialog");

                mAdapter.setSelected((CanvasDraggableItem) adapterView.getItemAtPosition(i));
                mAdapter.invalidateCanvas();

                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CanvasDraggableItem selected = (CanvasDraggableItem) adapterView.getItemAtPosition(i);
                if (mAdapter.getSelected() != selected)
                    mAdapter.setSelected(selected);
                else
                    mAdapter.setSelected(null);

                mAdapter.invalidateCanvas();
            }
        });

        return view;
    }
}

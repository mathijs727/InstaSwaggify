package zwaggerboyz.instaswaggify.viewpager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zwaggerboyz.instaswaggify.CanvasDraggableItem;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    OverlayListAdapter.java
 * This file contains the adapter to change the list of currently selected overlays.
 */

public class OverlayListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<CanvasDraggableItem> mItems;
    private OverlayListInterface mListener;

    private class ViewHolder {
        TextView titleTextView;
    }

    public OverlayListAdapter(Activity activity, OverlayListInterface listener, List<CanvasDraggableItem> items) {
        mInflater = activity.getLayoutInflater();
        mListener = listener;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public CanvasDraggableItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }

    @Override
    public View getView(int parent, View convertView, ViewGroup position) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_1, position);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView)convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.titleTextView.setText("Overlay " + position);

        return convertView;
    }

    public List<CanvasDraggableItem> getItems() {
        return mItems;
    }

    /* Removes item at index from filter list */
    public void remove(int index) {
        mItems.remove(mItems.get(index));

        mListener.updateOverlays(mItems);
        notifyDataSetChanged();
    }

    public void reorder(int from, int to) {
        if (from != to) {
            CanvasDraggableItem element = mItems.remove(from);
            mItems.add(to, element);

            notifyDataSetChanged();
            mListener.updateOverlays(mItems);
        }
    }

    public void addItem(CanvasDraggableItem overlay) {
        mItems.add(overlay);
        updateList();
    }

    public void updateList() {
        notifyDataSetChanged();
        mListener.updateOverlays(mItems);
    }

    public void clearOverlays() {
        mItems.clear();
        notifyDataSetChanged();
        mListener.updateOverlays(mItems);
    }

    public interface OverlayListInterface {
        public void updateOverlays(List<CanvasDraggableItem> overlays);
    }
}

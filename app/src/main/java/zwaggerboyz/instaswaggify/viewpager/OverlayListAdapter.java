package zwaggerboyz.instaswaggify.viewpager;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zwaggerboyz.instaswaggify.CanvasDraggableItem;
import zwaggerboyz.instaswaggify.CanvasView;
import zwaggerboyz.instaswaggify.HistoryBuffer;
import zwaggerboyz.instaswaggify.R;

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
    private CanvasView mCanvasView;
    private List<CanvasDraggableItem> mItems;
    private HistoryBuffer mHistoryBuffer;

    private class ViewHolder {
        TextView titleTextView;
    }

    public OverlayListAdapter(Activity activity, CanvasView canvasView, List<CanvasDraggableItem> items, HistoryBuffer historyBuffer) {
        mInflater = activity.getLayoutInflater();
        mCanvasView = canvasView;
        mItems = items;
        mHistoryBuffer = historyBuffer;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_overlay, null);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.list_item_overlay_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.titleTextView.setText(getItem(position).getName());

        return convertView;
    }

    public List<CanvasDraggableItem> getItems() {
        return mItems;
    }

    /* Removes item at index from filter list */
    public void remove(int index) {
        mHistoryBuffer.updateBuffer(null, mItems);
        mItems.remove(mItems.get(index));
        mCanvasView.invalidate();
        notifyDataSetChanged();
    }

    public void reorder(int from, int to) {
        if (from != to) {
            mHistoryBuffer.updateBuffer(null, mItems);
            CanvasDraggableItem element = mItems.remove(from);
            mItems.add(to, element);

            mCanvasView.invalidate();
            notifyDataSetChanged();
        }
    }

    public void addItem(CanvasDraggableItem overlay) {
        mHistoryBuffer.updateBuffer(null, mItems);
        mItems.add(overlay);
        mCanvasView.invalidate();
        notifyDataSetChanged();
    }

    public void clearOverlays() {
        mHistoryBuffer.updateBuffer(null, mItems);
        mItems.clear();
        notifyDataSetChanged();
    }

    public void setItems(List<CanvasDraggableItem> items) {
        Log.v("SETTING", "ITEMS");
        mItems.clear();
        mItems.addAll(items);
        mCanvasView.invalidate();
        notifyDataSetChanged();
    }
}

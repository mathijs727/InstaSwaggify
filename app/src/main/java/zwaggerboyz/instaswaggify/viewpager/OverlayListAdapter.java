package zwaggerboyz.instaswaggify.viewpager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import zwaggerboyz.instaswaggify.CanvasDraggableItem;
import zwaggerboyz.instaswaggify.CanvasView;
import zwaggerboyz.instaswaggify.R;
import zwaggerboyz.instaswaggify.filters.IFilter;

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
    private OverlayListInterface mListener;
    private List<CanvasDraggableItem> mItems;

    private class ViewHolder {
        TextView titleTextView;
    }

    public OverlayListAdapter(Activity activity, OverlayListInterface listener, CanvasView canvasView, List<CanvasDraggableItem> items) {
        mInflater = activity.getLayoutInflater();
        mListener = listener;
        mCanvasView = canvasView;
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
        mItems.remove(mItems.get(index));
        mCanvasView.invalidate();
        if (mItems.size() == 0)
            mListener.overlaysEmpty();
        notifyDataSetChanged();
    }

    public void reorder(int from, int to) {
        if (from != to) {
            CanvasDraggableItem element = mItems.remove(from);
            mItems.add(to, element);

            mCanvasView.invalidate();
            notifyDataSetChanged();
        }
    }

    public void addItem(CanvasDraggableItem overlay) {
        mItems.add(0, overlay);
        mCanvasView.invalidate();
        mListener.overlaysNotEmpty();
        notifyDataSetChanged();
    }

    public void clearOverlays() {
        mItems.clear();
        mCanvasView.invalidate();
        mListener.overlaysEmpty();
        notifyDataSetChanged();
    }

    public interface OverlayListInterface {
        public void overlaysEmpty();
        public void overlaysNotEmpty();
    }
}

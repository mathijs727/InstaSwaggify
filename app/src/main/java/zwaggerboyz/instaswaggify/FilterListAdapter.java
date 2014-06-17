package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mathijs on 16/06/14.
 */
public class FilterListAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private List<ListItemFilter> mItems;

    private class ViewHolder {
        public TextView titleTextView, label1TextView, label2TextView;
        public Switch enabledSwitch;
        public SeekBar slider1Seekbar, slider2Seekbar;
    }

    public FilterListAdapter(Activity context, List<ListItemFilter> items) {
        mInflater = context.getLayoutInflater();
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ListItemFilter getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_filter, null);

            viewHolder= new ViewHolder();
            viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.list_item_filter_title);
            viewHolder.label1TextView = (TextView)convertView.findViewById(R.id.list_item_filter_label1);
            viewHolder.label2TextView = (TextView)convertView.findViewById(R.id.list_item_filter_label2);
            viewHolder.enabledSwitch = (Switch)convertView.findViewById(R.id.list_item_filter_switch);
            viewHolder.slider1Seekbar = (SeekBar)convertView.findViewById(R.id.list_item_filter_seekbar1);
            viewHolder.slider2Seekbar = (SeekBar)convertView.findViewById(R.id.list_item_filter_seekbar2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ListItemFilter item = getItem(position);
        viewHolder.titleTextView.setText(item.title);
        viewHolder.label1TextView.setText(item.label1);
        viewHolder.label2TextView.setText(item.label2);
        viewHolder.enabledSwitch.setChecked(item.isEnabled);
        viewHolder.slider1Seekbar.setProgress(item.slider1);
        viewHolder.slider2Seekbar.setProgress(item.slider2);
        return convertView;
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }

    /* Removes item at index from filter list */
    public void remove(int index) {
        mItems.remove(mItems.get(index));
    }

    /*  */
    public void swap(int from, int to) {
        Collections.swap(mItems, from, to);
        notifyDataSetChanged();
    }

    /* Adds a new item to the filter list */
    public void add() {
        mItems.add(new ListItemFilter());
        notifyDataSetChanged();
    }
}

package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Mathijs on 16/06/14.
 */
public class FilterArrayAdapter extends ArrayAdapter<ListItemFilter> {
    private final Activity mContext;
    private ListItemFilter[] mItems;

    private class ViewHolder {
        public TextView titleTextView, label1TextView, label2TextView;
        public Switch enabledSwitch;
        public SeekBar slider1Seekbar, slider2Seekbar;
    }

    public FilterArrayAdapter(Activity context, ListItemFilter[] items) {
        super(context, R.layout.list_item_filter, items);
        mContext = context;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_filter, null);

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

        ListItemFilter item = mItems[position];
        viewHolder.titleTextView.setText(item.title);
        viewHolder.label1TextView.setText(item.label1);
        viewHolder.label2TextView.setText(item.label2);
        viewHolder.enabledSwitch.setChecked(item.isEnabled);
        viewHolder.slider1Seekbar.setProgress(item.slider1);
        viewHolder.slider2Seekbar.setProgress(item.slider2);
        return convertView;
    }


}

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mathijs on 16/06/14.
 */

public class FilterListAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private List<IFilter> mItems;
    private List<List<IFilter>> mItemsPreviousBuffer = new ArrayList<List<IFilter>>();
    private int bufferLevel = 0;
    private Activity activity;

    private class ViewHolder {
        public TextView titleTextView, label1TextView, label2TextView;
        public SeekBar slider1Seekbar, slider2Seekbar;
        public int SeekBar1Value, SeekBar2Value;
    }

    public FilterListAdapter(Activity context, List<IFilter> items) {
        mInflater = context.getLayoutInflater();
        mItems = items;
        activity = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public IFilter getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_filter, null);

            viewHolder= new ViewHolder();
            viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.list_item_filter_title);
            viewHolder.label1TextView = (TextView)convertView.findViewById(R.id.list_item_filter_label1);
            viewHolder.label2TextView = (TextView)convertView.findViewById(R.id.list_item_filter_label2);
            viewHolder.slider1Seekbar = (SeekBar)convertView.findViewById(R.id.list_item_filter_seekbar1);
            viewHolder.slider2Seekbar = (SeekBar)convertView.findViewById(R.id.list_item_filter_seekbar2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final IFilter item = getItem(position);
        viewHolder.titleTextView.setText(item.getName());

        /**
         * Toggles visibility of viewHolder elements
         */
        switch (item.getNumValues()) {
            case 0:
                viewHolder.label1TextView.setVisibility(View.GONE);
                viewHolder.label2TextView.setVisibility(View.GONE);
                viewHolder.slider1Seekbar.setVisibility(View.GONE);
                viewHolder.slider2Seekbar.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.label2TextView.setVisibility(View.GONE);
                viewHolder.slider2Seekbar.setVisibility(View.GONE);

                viewHolder.label1TextView.setText(item.getLabel(0));
                viewHolder.slider1Seekbar.setProgress(item.getValue(0));
                break;
            case 2:
                viewHolder.label1TextView.setVisibility(View.VISIBLE);
                viewHolder.label2TextView.setVisibility(View.VISIBLE);
                viewHolder.slider1Seekbar.setVisibility(View.VISIBLE);
                viewHolder.slider2Seekbar.setVisibility(View.VISIBLE);

                viewHolder.label1TextView.setText(item.getLabel(0));
                viewHolder.label2TextView.setText(item.getLabel(1));
                viewHolder.slider1Seekbar.setProgress(item.getValue(0));
                viewHolder.slider2Seekbar.setProgress(item.getValue(1));
                break;
        }


        /**
         * Adds listeners to seek bars. Updates seek bar value on stop track
         */
        if (item.getNumValues() > 0) {
            viewHolder.slider1Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    item.setValue(0, seekBar.getProgress());
                }
            });
        }

        if (item.getNumValues() > 1) {
            viewHolder.slider2Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    item.setValue(1, seekBar.getProgress());
                }
            });
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }

    /* Removes item at index from filter list */
    public void remove(int index) {
        mItemsPreviousBuffer.add(new ArrayList<IFilter>(mItems));
        bufferLevel++;
        ((MainActivity)activity).setUndoState(true);

        mItems.remove(mItems.get(index));
    }

    public void reorder(int from, int to) {
        if (from != to) {
            mItemsPreviousBuffer.add(new ArrayList<IFilter>(mItems));
            bufferLevel++;
            ((MainActivity)activity).setUndoState(true);

            IFilter element = mItems.remove(from);
            mItems.add(to, element);
            notifyDataSetChanged();
        }
    }

    /* Adds a new item to the filter list */
    public void add(int filter) {
        mItemsPreviousBuffer.add(new ArrayList<IFilter>(mItems));
        bufferLevel++;
        ((MainActivity)activity).setUndoState(true);

        switch (filter) {
            case 0:
                mItems.add(new BrightnessFilter());
                break;
            case 1:
                mItems.add(new ContrastFilter());
                break;
            case 2:
                mItems.add(new RotationFilter());
                break;
            case 3:
                mItems.add(new SaturationFilter());
                break;
            case 4:
                mItems.add(new SepiaFilter());
            default:
                break;
        }
        notifyDataSetChanged();
    }

    /**
     * Undo last change. Only remembers last change.
     */
    public void undo() {
        mItems.clear();
        mItems.addAll(mItemsPreviousBuffer.remove(--bufferLevel));
        if(bufferLevel == 0) {
            ((MainActivity)activity).setUndoState(false);
        }
        notifyDataSetChanged();
    }

    public void add_favorite() {

        String favorite_name = "Schwarzenegger";
        int size = mItems.size();
        AbstractFilterClass.FilterID id;
        int value1 = 0;
        int value2 = 0;

        /* open or create file */
        // filename: R.raw.fav



        for (int i = 0; i < size; i++) {

            /* get filter id */
            id = mItems.get(i).getID();

            /* get slider values */
            switch (mItems.get(i).getNumValues()) {
                case 1:
                    value1 = mItems.get(i).getValue(0);
                    break;
                case 2:
                    value1 = mItems.get(i).getValue(0);
                    value2 = mItems.get(i).getValue(1);
                    break;
                default:
                    break;
            }
        }
    }
}

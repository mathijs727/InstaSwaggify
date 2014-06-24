package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zwaggerboyz.instaswaggify.filters.IFilter;

/**
 * Created by Mathijs on 16/06/14.
 */

public class FilterListAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private List<IFilter> mItems;
    private List<List<IFilter>> mItemsPreviousBuffer = new ArrayList<List<IFilter>>();
    private int bufferLevel = 0;
    private FilterListInterface mListener;

    private class ViewHolder {
        public TextView titleTextView, label1TextView, label2TextView, label3TextView;
        public SeekBar slider1Seekbar, slider2Seekbar, slider3Seekbar;
    }

    public FilterListAdapter(Activity activity, FilterListInterface listener, List<IFilter> items) {
        mInflater = activity.getLayoutInflater();
        mItems = items;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public IFilter getItem(int position) {
        return mItems.get(position);
    }

    public void setItems(List<IFilter> items) {
        mItems = items;
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
            viewHolder.label3TextView = (TextView)convertView.findViewById(R.id.list_item_filter_label3);
            viewHolder.slider1Seekbar = (SeekBar)convertView.findViewById(R.id.list_item_filter_seekbar1);
            viewHolder.slider2Seekbar = (SeekBar)convertView.findViewById(R.id.list_item_filter_seekbar2);
            viewHolder.slider3Seekbar = (SeekBar)convertView.findViewById(R.id.list_item_filter_seekbar3);
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
                viewHolder.label3TextView.setVisibility(View.GONE);
                viewHolder.slider1Seekbar.setVisibility(View.GONE);
                viewHolder.slider2Seekbar.setVisibility(View.GONE);
                viewHolder.slider3Seekbar.setVisibility(View.GONE);
                break;
            case 1:
                viewHolder.label1TextView.setVisibility(View.VISIBLE);
                viewHolder.slider1Seekbar.setVisibility(View.VISIBLE);
                viewHolder.label1TextView.setText(item.getLabel(0));
                viewHolder.slider1Seekbar.setProgress(item.getValue(0));

                viewHolder.label2TextView.setVisibility(View.GONE);
                viewHolder.label3TextView.setVisibility(View.GONE);
                viewHolder.slider2Seekbar.setVisibility(View.GONE);
                viewHolder.slider3Seekbar.setVisibility(View.GONE);
                break;
            case 2:
                viewHolder.label1TextView.setVisibility(View.VISIBLE);
                viewHolder.label2TextView.setVisibility(View.VISIBLE);
                viewHolder.label3TextView.setVisibility(View.GONE);
                viewHolder.slider1Seekbar.setVisibility(View.VISIBLE);
                viewHolder.slider2Seekbar.setVisibility(View.VISIBLE);
                viewHolder.slider3Seekbar.setVisibility(View.GONE);

                viewHolder.label1TextView.setText(item.getLabel(0));
                viewHolder.label2TextView.setText(item.getLabel(1));
                viewHolder.slider1Seekbar.setProgress(item.getValue(0));
                viewHolder.slider2Seekbar.setProgress(item.getValue(1));
                break;
            case 3:
                viewHolder.label1TextView.setVisibility(View.VISIBLE);
                viewHolder.label2TextView.setVisibility(View.VISIBLE);
                viewHolder.label3TextView.setVisibility(View.VISIBLE);
                viewHolder.slider1Seekbar.setVisibility(View.VISIBLE);
                viewHolder.slider2Seekbar.setVisibility(View.VISIBLE);
                viewHolder.slider3Seekbar.setVisibility(View.VISIBLE);

                viewHolder.label1TextView.setText(item.getLabel(0));
                viewHolder.label2TextView.setText(item.getLabel(1));
                viewHolder.label3TextView.setText(item.getLabel(2));
                viewHolder.slider1Seekbar.setProgress(item.getValue(0));
                viewHolder.slider2Seekbar.setProgress(item.getValue(1));
                viewHolder.slider3Seekbar.setProgress(item.getValue(2));
                break;
        }

        /**
         * Adds listeners to seek bars. Updates seek bar value on stop track
         */
        if (item.getNumValues() > 0) {
            viewHolder.slider1Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    /*if (fromUser) {
                        item.setValue(0, progress);
                        mListener.updateImage(mItems);
                    }*/
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    item.setValue(0, seekBar.getProgress());
                    mListener.updateImage(mItems);
                }
            });
        }

        if (item.getNumValues() > 1) {
            viewHolder.slider2Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    /*if (fromUser) {
                        item.setValue(1, progress);
                        mListener.updateImage(mItems);
                    }*/
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    item.setValue(1, seekBar.getProgress());
                    mListener.updateImage(mItems);
                }
            });
        }

        if (item.getNumValues() > 2) {
            viewHolder.slider3Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    /*if (fromUser) {
                        item.setValue(1, progress);
                        mListener.updateImage(mItems);
                    }*/
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    item.setValue(2, seekBar.getProgress());
                    mListener.updateImage(mItems);
                }
            });
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }

    public List<IFilter> getItems() {
        return mItems;
    }

    /* Removes item at index from filter list */
    public void remove(int index) {
        updateBuffer();

        mItems.remove(mItems.get(index));

        mListener.updateImage(mItems);
        notifyDataSetChanged();
    }

    public void reorder(int from, int to) {
        if (from != to) {
            updateBuffer();

            IFilter element = mItems.remove(from);
            mItems.add(to, element);

            notifyDataSetChanged();
            mListener.updateImage(mItems);
        }
    }

    public void addItem(IFilter filter) {
        mItems.add(filter);
        updateList();
    }

    public void updateList() {
        notifyDataSetChanged();
        mListener.updateImage(mItems);
    }

    private void updateBuffer() {
        if (bufferLevel < 25) {
            mItemsPreviousBuffer.add(new ArrayList<IFilter>(mItems));
            bufferLevel++;
            mListener.setUndoState(true);
        } else {
            mItemsPreviousBuffer.remove(0);
            mItemsPreviousBuffer.add(new ArrayList<IFilter>(mItems));
            mListener.setUndoState(true);
        }
    }

    /**
     * Undo last change. Only remembers last change.
     */
    public void undo() {
        mItems.clear();
        mItems.addAll(mItemsPreviousBuffer.remove(--bufferLevel));
        if(bufferLevel == 0) {
            mListener.setUndoState(false);
        }
        mListener.updateImage(mItems);
        notifyDataSetChanged();
    }

    public void clearFilters() {
        updateBuffer();
        mItems.clear();
        notifyDataSetChanged();
        mListener.updateImage(mItems);
    }

    public interface FilterListInterface {
        public void updateImage(List<IFilter> filters);
        public void setUndoState(boolean undoState);
    }
}



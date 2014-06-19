package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        public TextView titleTextView, label1TextView, label2TextView;
        public SeekBar slider1Seekbar, slider2Seekbar;
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
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        item.setValue(0, progress);
                        mListener.updateImage(mItems);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });
        }

        if (item.getNumValues() > 1) {
            viewHolder.slider2Seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        item.setValue(1, progress);
                        mListener.updateImage(mItems);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
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

    /* Adds a new item to the filter list */
    public void add(int filter) {
        updateBuffer();

        switch (filter) {
            case 0:
                mItems.add(new BrightnessFilter());
                break;
            case 1:
                mItems.add(new ContrastFilter());
                break;
            case 2:
                mItems.add(new GaussianBlurFilter());
                break;
            case 3:
                mItems.add(new RotationFilter());
                break;
            case 4:
                mItems.add(new SaturationFilter());
                break;
            case 5:
                mItems.add(new SepiaFilter());
                break;
            case 6:
                mItems.add(new ThresholdBlurFilter());
                break;
            default:
                break;
        }
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

    public interface FilterListInterface {
        public void setUndoState(boolean state);
        public void updateImage(List<IFilter> filters);
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

    public void add_favorite() {
        try {
            JSONArray jSONArray = new JSONArray();
            for (IFilter filter:mItems) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("name", filter.getName());
                for (int i = 0; i < filter.getNumValues(); i++) {
                    jSONObject.put("label" + i, filter.getLabel(i);
                    jSONObject.put("value" + i, filter.getValue(i));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String favorites_array [][][]; // parse from json
//        int favorite_index = 1;
//        String favorite_name = "Schwarzenegger";
//        int size = mItems.size();
//        AbstractFilterClass.FilterID id;
//        int value1 = 0;
//        int value2 = 0;
//
//        /* open or create file */
//        // filename: R.raw.fav
//
//
//
//        for (int i = 0; i < size; i++) {
//
//            /* get filter id */
//            id = mItems.get(i).getID();
//
//            /* get slider values */
//            switch (mItems.get(i).getNumValues()) {
//                case 1:
//                    value1 = mItems.get(i).getValue(0);
//                    break;
//                case 2:
//                    value1 = mItems.get(i).getValue(0);
//                    value2 = mItems.get(i).getValue(1);
//                    break;
//                default:
//                    break;
//            }
//            favorites_array[favorite_index][i][0] = id.toString();
//            favorites_array[favorite_index][i][1] = Integer.toString(value1);
//            favorites_array[favorite_index][i][2] = Integer.toString(value2);
//        }
//
//        JSONArray mJSONArray = new JSONArray(Arrays.asList(favorites_array));


    }
}



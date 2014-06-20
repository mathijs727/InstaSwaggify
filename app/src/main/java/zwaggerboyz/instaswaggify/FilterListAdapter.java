package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
    private Activity mActivity;

    private class ViewHolder {
        public TextView titleTextView, label1TextView, label2TextView, label3TextView;
        public SeekBar slider1Seekbar, slider2Seekbar, slider3Seekbar;
    }

    public FilterListAdapter(Activity activity, FilterListInterface listener, List<IFilter> items) {
        mInflater = activity.getLayoutInflater();
        mItems = items;
        mListener = listener;
        mActivity = activity;
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
                mItems.add(new NoiseFilter());
                break;
            case 7:
                mItems.add(new InvertColorsFilter());
                break;
            case 8:
                mItems.add(new ColorizeFilter());
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

    public void clearFilters() {
        updateBuffer();
        mItems.clear();
        notifyDataSetChanged();
        mListener.updateImage(mItems);
    }

    public void add_favorite(String favoritesTitle) {

        /* parse current favorite list to JSONArray */
        SharedPreferences prefs = mActivity.getPreferences(Context.MODE_PRIVATE);
        String favoritesString = prefs.getString("Favorites", "");
        JSONObject jsonObject = null;

        try {
            if (favoritesString.equals("")) {
                jsonObject = new JSONObject();
            }
            else {
                jsonObject = new JSONObject(favoritesString);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        /* create array of current filter states */
        try {
            JSONArray newFilterArray = new JSONArray();
            JSONObject jSONObjectFilter;
            for (IFilter filter:mItems) {
                jSONObjectFilter = new JSONObject();
                jSONObjectFilter.put("id", filter.getID());
                jSONObjectFilter.put("numValues", filter.getNumValues());
                for (int i = 0; i < filter.getNumValues(); i++) {
                    jSONObjectFilter.put("value" + i, filter.getValue(i));
                }

                /* add filter to favorite */
                newFilterArray.put(jSONObjectFilter.toString());
            }

            /* add new favorite to favorites list */
            jsonObject.put(favoritesTitle, newFilterArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* favorites array to String */
        favoritesString = jsonObject.toString();

        /* add store favorites String sharedPreferences */
        prefs.edit().putString("Favorites", favoritesString).commit();
    }
}



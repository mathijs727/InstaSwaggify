package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zwaggerboyz.instaswaggify.dialogs.LoadPresetDialog;
import zwaggerboyz.instaswaggify.dialogs.SavePresetDialog;
import zwaggerboyz.instaswaggify.filters.AbstractFilterClass;
import zwaggerboyz.instaswaggify.filters.BrightnessFilter;
import zwaggerboyz.instaswaggify.filters.ColorizeFilter;
import zwaggerboyz.instaswaggify.filters.ContrastFilter;
import zwaggerboyz.instaswaggify.filters.GaussianBlurFilter;
import zwaggerboyz.instaswaggify.filters.IFilter;
import zwaggerboyz.instaswaggify.filters.InvertColorsFilter;
import zwaggerboyz.instaswaggify.filters.NoiseFilter;
import zwaggerboyz.instaswaggify.filters.RotationFilter;
import zwaggerboyz.instaswaggify.filters.SaturationFilter;
import zwaggerboyz.instaswaggify.filters.SepiaFilter;
import zwaggerboyz.instaswaggify.viewpager.FilterListAdapter;

/**
 * Created by Mathijs on 24/06/14.
 */
public class PresetsHelper implements LoadPresetDialog.OnLoadPresetListener, SavePresetDialog.OnSavePresetListener {
    private Context mContext;
    private FilterListAdapter mAdapter;
    private List<IFilter> mFilters;
    private DialogFragment mDialog;

    public static final String PRESET_KEY = "presets";
    public static final String FILTERS_KEY = "filters";
    public static final String FILTER_ID_KEY = "id";
    public static final String FILTER_VALUE_KEY = "value";
    public static final String PRESET_TITLE_KEY = "title";

    public PresetsHelper(Context context) {
        mContext = context;
    }

    public void loadPreset(Activity activity, FilterListAdapter adapter) {
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            fragmentTransaction.remove(prev);
        fragmentTransaction.addToBackStack(null);

        mDialog = new LoadPresetDialog();
        ((LoadPresetDialog)mDialog).setOnLoadPresetListener(this);
        mDialog.show(fragmentTransaction, "dialog");

        mAdapter = adapter;
    }

    public void savePreset(Activity activity, List<IFilter> filters) {
        FragmentTransaction fragmentTransaction = activity.getFragmentManager().beginTransaction();
        Fragment prev = activity.getFragmentManager().findFragmentByTag("dialog");
        if (prev != null)
            fragmentTransaction.remove(prev);
        fragmentTransaction.addToBackStack(null);

        mDialog = new SavePresetDialog();
        ((SavePresetDialog)mDialog).setOnSavePresetListener(this);
        mDialog.show(fragmentTransaction, "dialog");

        mFilters = filters;
    }

    @Override
    public void OnSavePresetListener(String title) {
        savePresetToSharedPrefs(mFilters, title);
    }

    @Override
    public void OnLoadPresetListener(int index) {
        mAdapter.setItems(loadPresetFromSharedPrefs(index));
    }

    public List<String> getPresets() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PRESET_KEY, Context.MODE_PRIVATE);
        String parseString = sharedPreferences.getString(PRESET_KEY, "");
        List<String> presets = new ArrayList<String>();

        try {
            JSONArray presetsJSONArray;
            if (parseString.isEmpty()) {
                presetsJSONArray = new JSONArray();
            } else {
                presetsJSONArray = new JSONArray(parseString);
            }

            int length = presetsJSONArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject presetJSONObject = presetsJSONArray.getJSONObject(i);
                presets.add(presetJSONObject.getString(PRESET_TITLE_KEY));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return presets;
    }

    public void removePreset(int index) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PRESET_KEY, Context.MODE_PRIVATE);
        String parseString = sharedPreferences.getString(PRESET_KEY, "");

        try {
            JSONArray presetsJSONArray = new JSONArray(parseString);
            JSONArray newPresetsJSONArray = new JSONArray();

            int length = presetsJSONArray.length();
            for (int i = 0; i < length; i++) {
                if (i != index)
                    newPresetsJSONArray.put(presetsJSONArray.getJSONObject(i));
            }
            sharedPreferences.edit()
                             .putString(PRESET_KEY, newPresetsJSONArray.toString())
                             .commit();
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private void savePresetToSharedPrefs(List<IFilter> filters, String title) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PRESET_KEY, Context.MODE_PRIVATE);
        String parseString = sharedPreferences.getString(PRESET_KEY, "");

        try {
            JSONArray presetsJSONArray;
            if (parseString.isEmpty()) {
                presetsJSONArray = new JSONArray();
            } else {
                presetsJSONArray = new JSONArray(parseString);
            }

            JSONObject presetJSONObject = new JSONObject();
            presetJSONObject.put(PRESET_TITLE_KEY, title);

            JSONArray filtersJSONArray = new JSONArray();
            for (IFilter filter : filters) {
                JSONObject filterJSONObject = new JSONObject();
                filterJSONObject.put(FILTER_ID_KEY, filter.getID().ordinal());
                for (int i = 0; i < 3; i++) {
                    filterJSONObject.put(FILTER_VALUE_KEY + i, filter.getValue(i));
                }
                filtersJSONArray.put(filterJSONObject);
            }
            presetJSONObject.put(FILTERS_KEY, filtersJSONArray);

            presetsJSONArray.put(presetJSONObject);
            sharedPreferences.edit()
                    .putString(PRESET_KEY, presetsJSONArray.toString())
                    .commit();
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private List<IFilter> loadPresetFromSharedPrefs(int id) {
        SharedPreferences prefs = mContext.getSharedPreferences(PRESET_KEY, Context.MODE_PRIVATE);
        String parseString = prefs.getString(PRESET_KEY, "");

        List<IFilter> filters = new ArrayList<IFilter>();
        try {
            JSONArray presetsJSONArray = new JSONArray(parseString);
            JSONObject presetJSONObject = presetsJSONArray.getJSONObject(id);
            JSONArray filtersJSONArray = presetJSONObject.getJSONArray(FILTERS_KEY);

            int length = filtersJSONArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jSONFilter = filtersJSONArray.getJSONObject(i);
                AbstractFilterClass.FilterID filterId = AbstractFilterClass.FilterID.values()[jSONFilter.getInt(FILTER_ID_KEY)];

                IFilter filter = null;
                switch (filterId) {
                    case BRIGHTNESS:
                        filter = new BrightnessFilter();
                        break;
                    case CONTRAST:
                        filter = new ContrastFilter();
                        break;
                    case GAUSSIAN:
                        filter = new GaussianBlurFilter();
                        break;
                    case ROTATION:
                        filter = new RotationFilter();
                        break;
                    case SATURATION:
                        filter = new SaturationFilter();
                        break;
                    case SEPIA:
                        filter = new SepiaFilter();
                        break;
                    case NOISE:
                        filter = new NoiseFilter();
                        break;
                    case INVERT:
                        filter = new InvertColorsFilter();
                        break;
                    case COLORIZE:
                        filter = new ColorizeFilter();
                        break;
                }

                for (int j = 0; j < 3; j++) {
                    filter.setValue(j, jSONFilter.optInt(FILTER_VALUE_KEY + j, 0));
                }

                filters.add(filter);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        mDialog.dismiss();

        return filters;
    }
}

package zwaggerboyz.instaswaggify.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zwaggerboyz.instaswaggify.R;
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

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    FilterDialog.java
 * This file contains the dialog that is shown selecting a new filter.
 */

public class FilterDialog extends DialogFragment {
    private static final String[] mAllFilters =
            {
                    "Brightness",
                    "Contrast",
                    "Gaussian Blur",
                    "Rotation",
                    "Saturation",
                    "Sepia",
                    "Noise",
                    "Invert Colors",
                    "Colorize"
            };
    private OnAddFilterListener mListener = null;
    ArrayAdapter<String> mAdapter;

    public FilterDialog(Context context, List<IFilter> filters) {
        List<String> filterNamesList = new ArrayList<String>();
        for (String filterName : mAllFilters)
            filterNamesList.add(filterName);

        for (IFilter filter : filters)
            filterNamesList.remove(filter.getName());

        String[] filterNames = new String[filterNamesList.size()];
        filterNamesList.toArray(filterNames);
        mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, filterNames);
    }

    public void setOnAddFilterListener(OnAddFilterListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        getDialog().setTitle(R.string.filter_dialog_title);

        /* Setting up ListView and the adapter */
        View mView = inflater.inflate(R.layout.filterdialog, container, false);
        ListView listView = (ListView) mView.findViewById(R.id.filter_dialog_list);

        listView.setAdapter(mAdapter);

        /* List item listeners */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int listId, long l) {
                if (mListener != null) {
                    IFilter filter = null;
                    int id;
                    for (id = 0; id < mAllFilters.length; id++)
                        if (mAllFilters[id] == mAdapter.getItem(listId))
                            break;

                    switch (id) {
                        case 0:
                            filter = new BrightnessFilter();
                            break;
                        case 1:
                            filter = new ContrastFilter();
                            break;
                        case 2:
                            filter = new GaussianBlurFilter();
                            break;
                        case 3:
                            filter = new RotationFilter();
                            break;
                        case 4:
                            filter = new SaturationFilter();
                            break;
                        case 5:
                            filter = new SepiaFilter();
                            break;
                        case 6:
                            filter = new NoiseFilter();
                            break;
                        case 7:
                            filter = new InvertColorsFilter();
                            break;
                        case 8:
                            filter = new ColorizeFilter();
                            break;
                    }
                    mListener.OnAddFilterListener(filter);
                }
            }
        });
        return mView;
    }

    public interface OnAddFilterListener {
        public void OnAddFilterListener(IFilter filter);
    }
}

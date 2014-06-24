package zwaggerboyz.instaswaggify.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import zwaggerboyz.instaswaggify.viewpager.FilterListAdapter;
import zwaggerboyz.instaswaggify.MainActivity;
import zwaggerboyz.instaswaggify.R;
import zwaggerboyz.instaswaggify.filters.IFilter;


/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    SavePresetDialog.java
 * This file contains the dialog that is shown when saving a preset.
 */


public class SavePresetDialog extends DialogFragment {
    FilterListAdapter adapter;

    public void setAdapter(FilterListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.save_preset_dialog, null);

        /* Inflate and set the layout for the dialog. Pass null as the parent view because its going
         * in the dialog layout. */
        builder .setView(view)

        /* Add buttons. */
        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            TextView presetNameField = (TextView)(view.findViewById(R.id.save_preset_name));
            addFavorite(presetNameField.getText().toString());
            }
        })

        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            SavePresetDialog.this.getDialog().cancel();
            }
        });

        builder.setTitle(R.string.preset_dialog_title);
        return builder.create();
    }

    private void addFavorite(String favoritesTitle) {
        /* parse current favorite list to JSONArray */
        MainActivity mainActivity = ((MainActivity)getActivity());
        SharedPreferences prefs = mainActivity.getPreferences(Context.MODE_PRIVATE);
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
            for (IFilter filter : mainActivity.getAdapterItems()) {
                jSONObjectFilter = new JSONObject();
                jSONObjectFilter.put("id", filter.getID());
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

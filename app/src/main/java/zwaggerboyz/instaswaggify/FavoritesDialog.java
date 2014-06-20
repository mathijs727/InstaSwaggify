package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by scoud on 19-6-14.
 */
public class FavoritesDialog extends DialogFragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (getFavorites().length > 0) {
            getDialog().setTitle("Select preset");
        }
        else {
            getDialog().setTitle("No favorites yet");
        }

        /**
         * Setting up ListView and the adapter
         */
        final View mView = inflater.inflate(R.layout.filterdialog, container, false);
        final ListView listView = (ListView) mView.findViewById(R.id.filter_dialog_list);

        /* show favorites if available */
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mView.getContext(), android.R.layout.simple_list_item_1, getFavorites());
        listView.setAdapter(adapter);

        /**
         * List item listeners
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: Handle this (Uses this preset)
//                ((MainActivity)getActivity()).setFilter();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                final String favoritesTitle = adapterView.getItemAtPosition(i).toString();
                builder1.setMessage("Delete " + adapterView.getItemAtPosition(i) + "?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                remove_favorite(favoritesTitle);
                                listView.setAdapter(new ArrayAdapter<String>(mView.getContext(), android.R.layout.simple_list_item_1, getFavorites()));
                            }
                        }
                );
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }
                );

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }
        });
        return mView;
    }

     /* Fetches saved favorites */
    private String[] getFavorites() {

        /* read favorites from SharedPreferences and parsen them to JSONObject*/
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        String favoritesString = prefs.getString("Favorites", "");
        JSONObject favoritesObject = null;
        ArrayList<String> keys = new ArrayList<String>();

        if (!favoritesString.equals("")) {
            try {
                /* fill array with keys */
                favoritesObject = new JSONObject(favoritesString);
                Iterator<?> iterate = favoritesObject.keys();
                while(iterate.hasNext()) {
                    String test  = (String)iterate.next();
                    keys.add(test);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            return new String[0];
        }

        /* return keys */
        String [] favorites = new String[keys.size()];
        keys.toArray(favorites);
        return favorites;
    }

    /* remove favorite from sharedPreferences */
    public void remove_favorite(String keyToRemove) {
        /* read favorites from SharedPreferences and parsen them to JSONObject*/
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        String favoritesString = prefs.getString("Favorites", "");
        JSONObject favorites = null;

        if (!favoritesString.equals("")) {

            /* remove key here */
            try {
                favorites = new JSONObject(favoritesString);
                favorites.remove(keyToRemove);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        favoritesString = favorites.toString();
        prefs.edit().putString("Favorites", favoritesString).commit();
    }
}
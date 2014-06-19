package zwaggerboyz.instaswaggify;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by scoud on 19-6-14.
 */
public class FavoritesDialog extends DialogFragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        getDialog().setTitle("Select preset");

        View mView = inflater.inflate(R.layout.filterdialog, container, false);
        ListView listView = (ListView) mView.findViewById(R.id.filter_dialog_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mView.getContext(), android.R.layout.simple_list_item_1, getFavorites());
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
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: Handle this (Removes this preset)
                return false;
            }
        });

        return mView;
    }

    /**
     * Fetches saved favorites
     */
    private String[] getFavorites() {
        //TODO: Implement this (Fetches all favorites from file)
        return new String[] {};
    }
}

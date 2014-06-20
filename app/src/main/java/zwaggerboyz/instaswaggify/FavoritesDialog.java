package zwaggerboyz.instaswaggify;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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

        /**
         * Setting up ListView and the adapter
         */
        View mView = inflater.inflate(R.layout.filterdialog, container, false);
        ListView listView = (ListView) mView.findViewById(R.id.filter_dialog_list);
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
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Delete " + adapterView.getItemAtPosition(i) + "?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
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
        return new String[] {"TEST"};
    }
}

package zwaggerboyz.instaswaggify.dialogs;

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

import zwaggerboyz.instaswaggify.PresetsHelper;
import zwaggerboyz.instaswaggify.R;

// TODO don't show "Load preset" button if there are no presets, also update this, when last
// TODO         preset is deleted from list

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    FavoritesDialog.java
 * This file contains the dialog that is shown when loading a preset.
 */

public class LoadPresetDialog extends DialogFragment {
    private PresetsHelper mPresetsHelper;

    public LoadPresetDialog(PresetsHelper presetsHelper) {
        mPresetsHelper = presetsHelper;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        getDialog().setTitle(R.string.filter_dialog_title);

        /* Setting up ListView and the adapter. */
        final View view = inflater.inflate(R.layout.dialog_list, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.dialog_list_listview);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                view.getContext(),
                android.R.layout.simple_list_item_1,
                mPresetsHelper.getPresets());
        listView.setAdapter(adapter);

        /* Set the onClickListener for the dialog. */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int id, long l) {
                mPresetsHelper.loadPresets(id);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int presetId, long l) {
                dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Delete " + adapter.getItem(presetId) + "?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adapter.remove(adapter.getItem(presetId));
                                mPresetsHelper.removePreset(presetId);
                                dialog.dismiss();
                            }
                        }
                );
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }
                );

                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        return view;
    }
}
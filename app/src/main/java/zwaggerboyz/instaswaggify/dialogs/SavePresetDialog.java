package zwaggerboyz.instaswaggify.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import zwaggerboyz.instaswaggify.PresetsHelper;
import zwaggerboyz.instaswaggify.R;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    SavePresetDialog.java
 * This file manages the saving of presets.
 */

public class SavePresetDialog extends DialogFragment {
    private PresetsHelper mPresetsHelper;

    public SavePresetDialog(PresetsHelper presetsHelper) {
        mPresetsHelper = presetsHelper;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_preset_save, null);

        /*
         * Inflate and set the layout for the dialog. Pass null as the parent view because its going
         * in the dialog layout.
         */
        builder.setView(view)
               /* Add buttons. */
               .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                       EditText titleEditText = (EditText) (view.findViewById(R.id.dialog_preset_save_edittext));
                       mPresetsHelper.savePreset(titleEditText.getText().toString());
                   }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       dismiss();
                   }
                });
        builder.setTitle(R.string.preset_dialog_title);
        return builder.create();
    }
}
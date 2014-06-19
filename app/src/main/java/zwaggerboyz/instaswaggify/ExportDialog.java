package zwaggerboyz.instaswaggify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;

/**
 * Created by zeta on 6/19/14.
 */

public class ExportDialog extends DialogFragment {
    int quality = 60;
    Bitmap.CompressFormat compression= Bitmap.CompressFormat.JPEG;
    SeekBar qualitySlider;
    Spinner compressionSpinner;

    protected void setQuality(int quality) {
        this.quality = quality;
    }

    protected void setCompression(int compressionId) {
        if (compressionId == 0){
            this.compression = Bitmap.CompressFormat.JPEG;
        }
        else {
            this.compression = Bitmap.CompressFormat.PNG;
        }
    }

    public void setListeners(View view) {
        SeekBar qualitySlider = (SeekBar)(view.findViewById(R.id.picture_quality));
        Spinner compressionSpinner = (Spinner)(view.findViewById(R.id.picture_compression));

        qualitySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                setQuality(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });

        qualitySlider.setProgress(60);

        compressionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int compressionId, long l) {
                setCompression(compressionId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.export_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder .setView(view)

                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity)getActivity()).save_picture(compression, quality);
                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ExportDialog.this.getDialog().cancel();
                    }
                });

        builder.setTitle("Export Picture");
        setListeners(view);

        return builder.create();
    }
}

package zwaggerboyz.instaswaggify.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import zwaggerboyz.instaswaggify.CanvasView;
import zwaggerboyz.instaswaggify.R;

/**
 * Created by zeta on 6/19/14.
 */

public class ExportDialog extends DialogFragment {
    private boolean notifySucces = true;
    private boolean share = true;
    protected int quality = 60;
    CanvasView mCanvasView;
    protected Bitmap.CompressFormat compression= Bitmap.CompressFormat.JPEG;

    public void setShare(Boolean shareEnabled) {
        share = shareEnabled;
    }

    public void setNotifySucces(boolean notify) {
        notifySucces = notify;
    }

    public void setCanvasView(CanvasView canvas){
        mCanvasView = canvas;
    }

    protected void setQuality(int quality) {
        this.quality = quality;
    }

    protected void setCompression(Bitmap.CompressFormat compression) {
            this.compression = compression;
    }

    public void setListeners(View view) {
        final SeekBar qualitySlider = (SeekBar)(view.findViewById(R.id.picture_quality));
        final Spinner compressionSpinner = (Spinner)(view.findViewById(R.id.picture_compression));
        final TextView textView = (TextView) (view.findViewById(R.id.export_text_view));

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

        compressionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int compressionId, long l) {
                if (compressionId == 0) {
                    setCompression(Bitmap.CompressFormat.JPEG);
                    qualitySlider.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
                else {
                    setCompression(Bitmap.CompressFormat.PNG);
                    qualitySlider.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /* restore previous settings.
         */
        qualitySlider.setProgress(quality);
        if (compression == Bitmap.CompressFormat.JPEG)
            compressionSpinner.setSelection(0);
        else
            compressionSpinner.setSelection(1);

    }

    protected void acceptHandler() {
        if (share) {
            Intent share = new Intent(Intent.ACTION_SEND);
            Uri fileUri = save_picture();

            if (fileUri == null)
                return;

            if (compression == Bitmap.CompressFormat.JPEG)
                share.setType("image/jpeg");
            else
                share.setType("image/png");

            share.putExtra(Intent.EXTRA_STREAM, fileUri);
            startActivity(Intent.createChooser(share, "Share Image"));
        }
        else {
            save_picture();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.export_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ExportDialog.this.getDialog().cancel();
            }
        });

        if (share) {
            builder.setPositiveButton(R.string.action_share, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    acceptHandler();
                }
            });
            builder.setTitle(R.string.share_dialog_title);
        }
        else {
            builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    acceptHandler();
                }
            });
            builder.setTitle(R.string.export_dialog_title);
        }

        setListeners(view);
        return builder.create();
    }

    protected Uri save_picture() {
        File folder, file;
        FileOutputStream output;
        String state = Environment.getExternalStorageState();
        String extension;
        Uri fileUri;

        Bitmap bitmap = mCanvasView.getBitmap();
        boolean externalIsAvailable = true;
        Toast errorToast = Toast.makeText(getActivity(),
                "Error while exporting image.",
                Toast.LENGTH_SHORT);

        if(bitmap == null) {
            return null;
        }

        if(compression == Bitmap.CompressFormat.JPEG) {
            extension = ".jpg";
        }
        else {
            extension = ".png";
        }

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            externalIsAvailable = false;
            Toast.makeText(getActivity(),
                    "No SD-card available",
                    Toast.LENGTH_SHORT).show();

            return null;
        }

        /* Try to open a file to export the picture
         */
        try {

            /* filename is made with a timestamp */
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
            String date = s.format(new Date());

            if (!externalIsAvailable) {
                folder = new File("InstasSwaggify");
            }
            else {
                folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "InstaSwaggify");
            }

            if (folder.exists() == false) {
                if (folder.mkdirs() == false) {
                    Log.i("Take Photo", "no directory created");
                    errorToast.show();
                    return null;
                }
            }

            folder = new File(folder, "Swaggified pictures");
            if (folder.exists() == false) {
                if (folder.mkdirs() == false) {
                    Log.i("Take Photo", "no directory created");
                    errorToast.show();
                    return null;
                }
            }

            file = new File(folder, date + extension);
            if (!file.exists()) {
                file.createNewFile();
            }

            else {
                Toast.makeText(getActivity(),
                        "File Already exists",
                        Toast.LENGTH_SHORT).show();

                Log.i("Pevid", "create file failed");
                return null;
            }

            fileUri = Uri.fromFile(file);
            output = new FileOutputStream(file);
        }

        catch (Exception e) {
            errorToast.show();
            e.printStackTrace();
            Log.e("Error opening histogram output stream", e.toString());
            return null;

        }

        try {
            /* The media scanner has to scan the newly made image, for it to be visible
             * in the pictures folder.
             */

            bitmap.compress(compression, quality, output);

            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {

                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    }
            );

            if(!share)
                Toast.makeText(getActivity(), "Picture successfully exported", Toast.LENGTH_SHORT).show();

        }

        catch (Exception e) {

            Toast.makeText(getActivity(),
                    "An error occurred while exporting",
                    Toast.LENGTH_SHORT).show();

            e.printStackTrace();
            Log.e("Error writing histogram picture", e.toString());
        }

        finally {

            try {
                output.flush();
                output.close();
            }

            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "An error occurred while exporting",
                        Toast.LENGTH_SHORT).show();
            }
        }

        return fileUri;
    }


}

package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    ExportHelper.java
 * This file contains the dialog that is shown when exporting a photo.
 */

public class ExportHelper {
    private boolean mShare = true;
    private Context mContext;

    CanvasView mCanvasView;
    public void setCanvasView(CanvasView canvas){
        mCanvasView = canvas;
    }

    public void exportPicture (boolean mustShare, Context context, MainActivity activity) {
        this.mShare = mustShare;
        this.mContext = context;

        Uri fileUri = savePicture();

        if (mShare) {
            Intent share = new Intent(Intent.ACTION_SEND);

            if (fileUri == null)
                return;

            share.setType("image/png");
            share.putExtra(Intent.EXTRA_STREAM, fileUri);
            activity.startActivity(Intent.createChooser(share, "Share Image"));
        }
    }

    protected Uri savePicture() {
        File folder, file;
        FileOutputStream output;
        String state = Environment.getExternalStorageState();
        String extension = ".png";
        Uri fileUri;

        Bitmap bitmap = mCanvasView.getBitmap();
        boolean externalIsAvailable = true;

        if(bitmap == null) {
            return null;
        }

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Toast.makeText(mContext,
                "No SD-card available",
                Toast.LENGTH_SHORT).show();
            return null;
        }

        /* Try to open a file to export the picture. */
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
                    return null;
                }
            }

            folder = new File(folder, "Swaggified pictures");
            if (folder.exists() == false) {
                if (folder.mkdirs() == false) {
                    return null;
                }
            }

            file = new File(folder, date + extension);
            if (!file.exists()) {
                file.createNewFile();
            }
            else {
                Toast.makeText(mContext,
                "File Already exists",
                Toast.LENGTH_SHORT).show();
                return null;
            }

            fileUri = Uri.fromFile(file);
            output = new FileOutputStream(file);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        try {
            /* The media scanner has to scan the newly made image, for it to be visible
             * in the pictures folder.
             */

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            MediaScannerConnection.scanFile(mContext,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {

                        public void onScanCompleted(String path, Uri uri) {
                        }
                    }
            );
            if(!mShare)
                Toast.makeText(mContext, "Picture successfully exported", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(mContext,
                    "An error occurred while exporting",
                    Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        finally {
            try {
                output.flush();
                output.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext,
                        "An error occurred while exporting",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return fileUri;
    }
}
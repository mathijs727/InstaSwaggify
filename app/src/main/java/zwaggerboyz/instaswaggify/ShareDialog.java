package zwaggerboyz.instaswaggify;

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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zeta on 6/19/14.
 */

public class ShareDialog extends ExportDialog {
    @Override
    protected void acceptHandler() {

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

}

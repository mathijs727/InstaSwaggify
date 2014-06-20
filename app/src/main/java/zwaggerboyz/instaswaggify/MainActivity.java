package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortListView;

import java.io.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements FilterListAdapter.FilterListInterface {
    private ShareActionProvider mShareActionProvider;
    private DragSortListView mListView;
    private CanvasView mCanvasView;
    private RSFilterHelper mRSFilterHelper;
    private FilterListAdapter mAdapter;
    private DialogFragment mDialog;
    private Menu mMenu;

    private Uri mImageUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 27031996;
    private static final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 10495800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<IFilter> items = new ArrayList<IFilter>();
        for (int i = 0; i < 5; i++) {
            items.add(new SaturationFilter());

        }

        mListView = (DragSortListView) findViewById(R.id.activity_main_listview);
        mCanvasView = (CanvasView) findViewById(R.id.activity_main_canvasview);

        mAdapter = new FilterListAdapter(this, this, new ArrayList<IFilter>());
        mListView.setAdapter(mAdapter);

        mRSFilterHelper = new RSFilterHelper();
        mRSFilterHelper.createRS(this);
        mRSFilterHelper.setCanvasView(mCanvasView);
        mRSFilterHelper.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.data), true);
        mRSFilterHelper.generateBitmap(new ArrayList<IFilter>(), this);

        mListView.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int item) {
                mAdapter.remove(item);
                mAdapter.notifyDataSetChanged();
            }
        });

        mListView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {

                mAdapter.reorder(from, to);
            }
        });

        /* plays a sound without blocking the app's execution */
        SoundThread soundThread = new SoundThread(this, R.raw.instafrenchecho);
        soundThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {

            /* Settings. */
            case R.id.action_settings: {
                return true;
            }

            /* Add filter. */
            case R.id.action_add_filter: {

                /**
                 * Handles stack for fragments
                 */
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null)
                    fragmentTransaction.remove(prev);

                fragmentTransaction.addToBackStack(null);

                /**
                 * Creates new filter dialog and shows it
                 */
                mDialog = new FilterDialog();
                mDialog.show(fragmentTransaction, "dialog");

                return true;
            }

            /* Take a photo with the camera. */
            case R.id.action_take_photo: {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            /* Create a folder to store the pictures if it does not exist yet. */
                File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "InstaSwaggify/Original Pictures");
                if (imagesFolder.exists() == false) {
                    if (imagesFolder.mkdirs() == false) {
                        Log.i("Take Photo", "no directory created");
                        return true;
                    }
                }

            /* Get the current time and date to use in the filename. */

                Date now = new Date();
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");

                String date = simpleFormat.format(now);
                Log.i("FILENAME", date + ".jpg");

                File image = new File(imagesFolder, date + ".jpg");
                mImageUri = Uri.fromFile(image);

            /* The intent is started. */
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                return true;
            }

            /* Select a photo from the filesystem. */
            case R.id.action_select_photo: {
                Intent pickPic_intent = new Intent();
                pickPic_intent.setType("image/*");
                pickPic_intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(pickPic_intent, "Select Picture"), SELECT_IMAGE_ACTIVITY_REQUEST_CODE);

                return true;
            }

            /**
             * Select saved filter presets
             */
            case R.id.action_favorites: {
                /**
                 * Handles stack for fragments
                 */
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null)
                    fragmentTransaction.remove(prev);

                fragmentTransaction.addToBackStack(null);

                /**
                 * Creates new filter dialog and shows it
                 */
                mDialog = new FavoritesDialog();
                mDialog.show(fragmentTransaction, "dialog");

                return true;
            }

            /**
             * Save new preset
             */
            case R.id.action_add_favorite: {
                SavePresetDialog dialog = new SavePresetDialog();
                dialog.setAdapter(mAdapter);
                dialog.show(getFragmentManager(), "Save Preset");
                return true;
            }

            case R.id.action_undo: {
                mAdapter.undo();
                return true;
            }

            case R.id.action_save_picture: {
                ExportDialog exportDialog = new ExportDialog();
                exportDialog.setmCanvasView(mCanvasView);
                exportDialog.show(getFragmentManager(), "Export Dialog");
                return true;

            }

            case R.id.action_share: {

/*                Bitmap icon = mCanvasView.getBitmap();
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");

                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
                startActivity(Intent.createChooser(share, "Share Image"));*/
                ShareDialog dialog = new ShareDialog();
                dialog.setmCanvasView(mCanvasView);
                dialog.setNotifySucces(false);

                dialog.show(getFragmentManager(), "Share Dialog");

                return true;

            }

            case R.id.action_clear: {
                mAdapter.clearFilters();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* Get image taken by the camera. */
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    /* The image is converted to a bitmap and send to the FilterHelper object. */
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    mRSFilterHelper.setBitmap(bitmap, true);
                    updateImage(mAdapter.getItems());
                }
                catch (Exception e) {
                    Log.e("onActivityResult", "create bitmap failed: " + e);
                }
            }
            else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(this, "Could not capture image", Toast.LENGTH_SHORT).show();
            }
        }

        /* Get image selected from the file-system. */
        else if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mImageUri = data.getData();
                try {
                    /* The image is converted to a bitmap and send to the FilterHelper object. */
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    mRSFilterHelper.setBitmap(bitmap, true);
                    updateImage(mAdapter.getItems());
                } catch (Exception e) {
                    Log.e("onActivityResult", "create bitmap failed: " + e);
                }
            }
            else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(this, "Could not select image", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * Adds a new filter to the list
     */
    public void addFilter(int i) {
        mDialog.dismiss();
        mAdapter.add(i);
    }

    protected void save_picture(Bitmap.CompressFormat compression, int quality) {
        File folder, file;
        FileOutputStream output;
        String state = Environment.getExternalStorageState();
        String extension;

        Bitmap bitmap = mCanvasView.getBitmap();
        boolean externalIsAvailable = true;
        Toast errorToast = Toast.makeText(this,
                "Error while exporting image.",
                Toast.LENGTH_SHORT);

        if(bitmap == null) {
            return;
        }

        if(compression == Bitmap.CompressFormat.JPEG) {
            extension = ".jpg";
        }
        else {
            extension = ".png";
        }

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            externalIsAvailable = false;
            Toast.makeText(this,
                    "No SD-card available",
                    Toast.LENGTH_SHORT).show();

            return;
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
                    return;
                }
            }

            folder = new File(folder, "Swaggified pictures");
            if (folder.exists() == false) {
                if (folder.mkdirs() == false) {
                    Log.i("Take Photo", "no directory created");
                    errorToast.show();
                    return;
                }
            }

            file = new File(folder, date + extension);
            if (!file.exists()) {
                file.createNewFile();
            }

            else {
                Toast.makeText(this,
                        "File Already exists",
                        Toast.LENGTH_SHORT).show();

                Log.i("Pevid", "create file failed");
                return;
            }

            output = new FileOutputStream(file);
        }

        catch (Exception e) {
            errorToast.show();
            e.printStackTrace();
            Log.e("Error opening histogram output stream", e.toString());
            return;

        }

        try {
            /* The media scanner has to scan the newly made image, for it to be visible
             * in the pictures folder.
             */

            bitmap.compress(compression, quality, output);

            MediaScannerConnection.scanFile(this,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {

                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    }
            );
            Toast.makeText(this, "Picture successfully exported", Toast.LENGTH_SHORT).show();

        }

        catch (Exception e) {

            Toast.makeText(this,
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
                Toast.makeText(this,
                        "An error occurred while exporting",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Sets the filter list as this preset
     */
    public void setFilter(String fav_key) {
        SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
        String favoritesString = prefs.getString("Favorites", "");
        JSONObject favoritesObject = null;
        JSONObject jsonFilter = null;
        JSONArray favortiesArray = null;
        int value0, value1, value2;
        String filterId = "";

        IFilter filter = null;
    //    mAdapter

        try {
            favoritesObject = new JSONObject(favoritesString);
            favortiesArray = favoritesObject.getJSONArray(fav_key);

            for (int i = 0; i < favortiesArray.length(); i++) {
                Log.e("MainActivity", "show favorite: " + favortiesArray.get(i));
                filterId = (String)jsonFilter.get("id");

                Log.e("MainActivity", "filterId: " + filterId);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUndoState(boolean state) {
        mMenu.findItem(R.id.action_undo).setEnabled(state);
    }

    @Override
    public void updateImage(List<IFilter> filters) {
        mRSFilterHelper.generateBitmap(filters, this);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}


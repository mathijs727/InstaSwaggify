package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.mobeta.android.dslv.DragSortListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements FilterListAdapter.FilterListInterface, FilterDialog.OnAddFilterListener {
    private ShareActionProvider mShareActionProvider;
    private ViewSwitcher mViewSwitcher;
    private DragSortListView mListView, mObjectView;
    private FilterListAdapter mFilterAdapter;
    private CanvasView mCanvasView;
    private RSFilterHelper mRSFilterHelper;
    private DialogFragment mDialog;
    private Menu mMenu;
    private ExportDialog mExportDialog;

    private Uri mImageUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 27031996;
    private static final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 10495800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewSwitcher = (ViewSwitcher) findViewById(R.id.activity_main_viewSwitcher);
        mExportDialog = new ExportDialog();
        mListView = (DragSortListView) findViewById(R.id.activity_main_listview);
        mObjectView = (DragSortListView) findViewById(R.id.activity_main_listview2);
        mCanvasView = (CanvasView) findViewById(R.id.activity_main_canvasview);
        mExportDialog.setCanvasView(mCanvasView);

        mFilterAdapter = new FilterListAdapter(this, this, new ArrayList<IFilter>());
        mListView.setAdapter(mFilterAdapter);

        mRSFilterHelper = new RSFilterHelper();
        mRSFilterHelper.createRS(this);
        mRSFilterHelper.setCanvasView(mCanvasView);
        mRSFilterHelper.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.data), true);
        mRSFilterHelper.generateBitmap(new ArrayList<IFilter>(), this);

        mListView.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int item) {
            mFilterAdapter.remove(item);
            mFilterAdapter.notifyDataSetChanged();
            }
        });

        mListView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
            mFilterAdapter.reorder(from, to);
            }
        });

        mObjectView.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int which) {
            mFilterAdapter.remove(which);
            }
        });

        mObjectView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
            mFilterAdapter.reorder(from, to);
            }
        });

        /* plays a sound without blocking the app's execution. */
        SoundThread soundThread = new SoundThread(this, R.raw.instafrenchecho);
        soundThread.start();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu: add items to the action bar. */
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            // TODO: hebben we settings wel nodig?
            case R.id.action_settings: {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blazeit);
                mCanvasView.addDraggable(new CanvasDraggableItem(bitmap, 100, 100));
                mCanvasView.invalidate();

                mViewSwitcher.showNext();
                return true;
            }

            /* Adds a filter to the list. */
            case R.id.action_add_filter: {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");

                if (prev != null)
                    fragmentTransaction.remove(prev);

                fragmentTransaction.addToBackStack(null);

                /* Show the filter-dialog. */
                FilterDialog dialog = new FilterDialog(this, mFilterAdapter.getItems());
                dialog.setOnAddFilterListener(this);
                mDialog = dialog;
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

            case R.id.action_favorites: {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null)
                    fragmentTransaction.remove(prev);
                fragmentTransaction.addToBackStack(null);

                mDialog = new FavoritesDialog();
                mDialog.show(fragmentTransaction, "dialog");

                return true;
            }

            case R.id.action_add_favorite: {
                SavePresetDialog dialog = new SavePresetDialog();
                dialog.setAdapter(mFilterAdapter);
                dialog.show(getFragmentManager(), "Save Preset");
                return true;
            }

            case R.id.action_undo: {
                mFilterAdapter.undo();
                return true;
            }

            case R.id.action_save_picture: {
                //ExportDialog exportDialog = new ExportDialog();
                //exportDialog.setCanvasView(mCanvasView);
                mExportDialog.setShare(false);
                mExportDialog.show(getFragmentManager(), "Export Dialog");
                return true;

            }

            case R.id.action_share: {
                mExportDialog.setShare(true);
                mExportDialog.show(getFragmentManager(), "Share Dialog");
/*               ShareDialog dialog = new ShareDialog();
                dialog.setCanvasView(mCanvasView);
                dialog.setNotifySucces(false);

                dialog.show(getFragmentManager(), "Share Dialog");*/

                return true;
            }

            case R.id.action_clear: {
                mFilterAdapter.clearFilters();
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
                    updateImage(mFilterAdapter.getItems());
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
                    updateImage(mFilterAdapter.getItems());
                }
                catch (Exception e) {
                    Log.e("onActivityResult", "create bitmap failed: " + e);
                }
            }
            else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(this, "Could not select image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* Sets the filter list as this preset. */
    public void setFilter(String fav_key) {
        //mFilterAdapter.clearFilters();
        SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
        String favoritesString = prefs.getString("Favorites", "");
        JSONObject favoritesObject = null;
        JSONObject jsonFilter = null;
        JSONArray favortiesArray = null;
        int numValues, value0, value1, value2;
        String filterId = "";
        List<IFilter> filterArray = new ArrayList<IFilter>();
        AbstractFilterClass.FilterID id;

        IFilter filter = null;

        try {
            favoritesObject = new JSONObject(favoritesString);
            favortiesArray = favoritesObject.getJSONArray(fav_key);

            for (int i = 0; i < favortiesArray.length(); i++) {
                jsonFilter = new JSONObject(favortiesArray.get(i).toString());
                filterId = jsonFilter.getString("id");
                id = AbstractFilterClass.FilterID.valueOf(filterId);

                /* create according filter and add to filter list */
                switch (id) {
                    case BRIGHTNESS:
                        filter = new BrightnessFilter();
                        filter.setValue(0, jsonFilter.getInt("value0"));
                        filterArray.add(filter);
                        break;
                    case CONTRAST:
                        filter = new ContrastFilter();
                        filter.setValue(0, jsonFilter.getInt("value0"));
                        filterArray.add(filter);
                        break;
                    case GAUSSIAN:
                        filter = new GaussianBlurFilter();
                        filter.setValue(0, jsonFilter.getInt("value0"));
                        filterArray.add(filter);
                        break;
                    case ROTATION:
                        filter = new RotationFilter();
                        filter.setValue(0, jsonFilter.getInt("value0"));
                        filterArray.add(filter);
                        break;
                    case SATURATION:
                        filter = new SaturationFilter();
                        filter.setValue(0, jsonFilter.getInt("value0"));
                        filterArray.add(filter);
                        break;
                    case SEPIA:
                        filter = new SepiaFilter();
                        filter.setValue(0, jsonFilter.getInt("value0"));
                        filter.setValue(1, jsonFilter.getInt("value1"));
                        filterArray.add(filter);
                        break;
                    case NOISE:
                        filter = new NoiseFilter();
                        filter.setValue(0, jsonFilter.getInt("value0"));
                        filterArray.add(filter);
                        break;
                    case INVERT:
                        filter = new InvertColorsFilter();
                        filterArray.add(filter);
                        break;
                    case COLORIZE:
                        filter = new ColorizeFilter();
                        filter.setValue(0, jsonFilter.getInt("value0"));
                        filter.setValue(1, jsonFilter.getInt("value1"));
                        filter.setValue(2, jsonFilter.getInt("value2"));
                        filterArray.add(filter);
                        break;
                }
            }
            mFilterAdapter.setmItems(filterArray);
            mFilterAdapter.updateList();
            mDialog.dismiss();
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

    // TODO: is dit nog nodig?
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (imageUri == null)
            return;

        try {
            /* The image is converted to a bitmap and send to the FilterHelper object. */
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            mRSFilterHelper.setBitmap(bitmap, true);
            updateImage(mFilterAdapter.getItems());
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Error occurred while opening picture",
                    Toast.LENGTH_SHORT).show();
            Log.e("handleSendImage", "create bitmap failed: " + e);
        }
    }

    @Override
    public void OnAddFilterListener(IFilter filter) {
        mDialog.dismiss();
        mFilterAdapter.addItem(filter);
    }
}


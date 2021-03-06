package zwaggerboyz.instaswaggify;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zwaggerboyz.instaswaggify.dialogs.FilterDialog;
import zwaggerboyz.instaswaggify.dialogs.OverlayDialog;
import zwaggerboyz.instaswaggify.filters.IFilter;
import zwaggerboyz.instaswaggify.viewpager.FilterListAdapter;
import zwaggerboyz.instaswaggify.viewpager.ListViewPagerAdapter;
import zwaggerboyz.instaswaggify.viewpager.OverlayListAdapter;
import zwaggerboyz.instaswaggify.viewpager.SlidingTabLayout;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    MainActivity.java
 * This file contains the main-activity for the app.
 */

public class MainActivity extends FragmentActivity
        implements FilterListAdapter.FilterListInterface,
                   OverlayListAdapter.OverlayListInterface,
                   ViewPager.OnPageChangeListener,
                   FilterDialog.OnAddFilterListener,
                   OverlayDialog.OnAddOverlayListener,
                   HistoryBuffer.UndoInterface,
                   PresetsHelper.PresetsHelperListener {

    private FilterListAdapter mFilterAdapter;
    private OverlayListAdapter mOverlayAdapter;
    private CanvasView mCanvasView;
    private ViewPager mViewPager;
    private RSFilterHelper mRSFilterHelper;
    private PresetsHelper mPresetsHelper;
    private DialogFragment mDialog;
    private Menu mMenu;
    private ExportHelper mExportHelper;
    private HistoryBuffer mHistoryBuffer;

    private Uri mImageUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private static final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHistoryBuffer = new HistoryBuffer(this);

        mExportHelper = new ExportHelper();

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout)findViewById(R.id.activity_main_tabs);
        mViewPager = (ViewPager) findViewById(R.id.activity_main_viewPager);
        mCanvasView = (CanvasView) findViewById(R.id.activity_main_canvasview);

        mExportHelper.setCanvasView(mCanvasView);

        mPresetsHelper = new PresetsHelper(this);
        mPresetsHelper.setPresetsHelperListener(this);

        mRSFilterHelper = new RSFilterHelper();
        mRSFilterHelper.createRS(this);
        mRSFilterHelper.setCanvasView(mCanvasView);
        mRSFilterHelper.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.data), true);
        mRSFilterHelper.generateBitmap(new ArrayList<IFilter>(), true, true);

        List<CanvasDraggableItem> overlays = new ArrayList<CanvasDraggableItem>();
        mFilterAdapter = new FilterListAdapter(this, this, new ArrayList<IFilter>(), mHistoryBuffer);
        mOverlayAdapter = new OverlayListAdapter(this, this, mCanvasView, overlays, mHistoryBuffer);
        mCanvasView.setOverlays(overlays);
        FragmentStatePagerAdapter pagerAdapter = new ListViewPagerAdapter(
                getSupportFragmentManager(),
                mFilterAdapter,
                mOverlayAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(pagerAdapter);
        slidingTabLayout.setViewPager(mViewPager);
        slidingTabLayout.setOnPageChangeListener(this);

        /* plays a sound without blocking the app's execution */
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

        if (mPresetsHelper.getPresets().size() > 0)
            mMenu.findItem(R.id.action_preset_load).setEnabled(true);
        else
            mMenu.findItem(R.id.action_preset_load).setEnabled(false);
        return true;
    }

    @Override
    /* perform action from menu bar */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            /* Adds a filter to the list. */
            case R.id.action_add: {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");

                if (prev != null)
                    fragmentTransaction.remove(prev);

                fragmentTransaction.addToBackStack(null);

                /* Show the filter-dialog. */
                DialogFragment dialog = null;
                int curItem = mViewPager.getCurrentItem();
                if (curItem == ListViewPagerAdapter.PAGE_FILTERS) {
                    dialog = new FilterDialog(this, mFilterAdapter.getItems());
                    ((FilterDialog)dialog).setOnAddFilterListener(this);
                } else if (curItem == ListViewPagerAdapter.PAGE_OVERLAYS) {
                    dialog = new OverlayDialog(this);
                    ((OverlayDialog)dialog).setOnAddOverlayListener(this);
                }
                mDialog = dialog;
                mDialog.show(fragmentTransaction, "dialog");
                return true;
            }

            /* Take a photo with the camera. */
            case R.id.action_take_photo: {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                /* Create a folder to store the pictures if it does not exist yet. */
                File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "InstaSwaggify/Original Pictures");
                if (!imagesFolder.exists()) {
                    if (!imagesFolder.mkdirs()) {
                        return true;
                    }
                }

                /* Get the current time and date to use in the filename. */
                Date now = new Date();
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");

                String date = simpleFormat.format(now);

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

            case R.id.action_preset_load: {
                mPresetsHelper.showLoadPresetDialog(this, mFilterAdapter);
                return true;
            }

            case R.id.action_preset_save: {
                mPresetsHelper.showSavePresetDialog(this, mFilterAdapter.getItems());
                return true;
            }

            case R.id.action_undo: {
                mHistoryBuffer.undo(mFilterAdapter, mOverlayAdapter);
                return true;
            }

            case R.id.action_save_picture: {
                mExportHelper.exportPicture(false, this);
                return true;
            }

            case R.id.action_share: {
                mExportHelper.exportPicture(true, this);
                return true;
            }

            case R.id.action_clear: {
                if (mViewPager.getCurrentItem() == ListViewPagerAdapter.PAGE_FILTERS)
                    mFilterAdapter.clearFilters();
                else if (mViewPager.getCurrentItem() == ListViewPagerAdapter.PAGE_OVERLAYS)
                    mOverlayAdapter.clearOverlays();
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

                    /* Set it on canvas already so we can force recalculateSize */
                    mCanvasView.setBitmap(bitmap, true);
                    mRSFilterHelper.setBitmap(bitmap, true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                mOverlayAdapter.clearOverlays();
                mFilterAdapter.clearFilters();
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

                    /* Set it on canvas already so we can force recalculateSize */
                    mCanvasView.setBitmap(bitmap, true);
                    mRSFilterHelper.setBitmap(bitmap, true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                mFilterAdapter.clearFilters();
                mOverlayAdapter.clearOverlays();
            }
            else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(this, "Could not select image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setUndoState(boolean state) {
        mMenu.findItem(R.id.action_undo).setEnabled(state);
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

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
        }
    }

    @Override
    public void updateImage(List<IFilter> filters) {
        updateImage(filters, true, false);
    }

    @Override
    public void updateImage(List<IFilter> filters, boolean listChanged, boolean forceUpdate) {
        mRSFilterHelper.generateBitmap(filters, listChanged, forceUpdate);
    }

    @Override
    public void filtersEmpty() {
        mMenu.findItem(R.id.action_preset_save).setEnabled(false);
        if (mViewPager.getCurrentItem() == ListViewPagerAdapter.PAGE_FILTERS)
            mMenu.findItem(R.id.action_clear).setEnabled(false);
    }

    @Override
    public void filtersNotEmpty() {
        mMenu.findItem(R.id.action_preset_save).setEnabled(true);
        if (mViewPager.getCurrentItem() == ListViewPagerAdapter.PAGE_FILTERS)
            mMenu.findItem(R.id.action_clear).setEnabled(true);
    }

    @Override
    public void overlaysEmpty() {
        if (mViewPager.getCurrentItem() == ListViewPagerAdapter.PAGE_OVERLAYS)
            mMenu.findItem(R.id.action_clear).setEnabled(false);
    }

    @Override
    public void overlaysNotEmpty() {
        if (mViewPager.getCurrentItem() == ListViewPagerAdapter.PAGE_OVERLAYS)
            mMenu.findItem(R.id.action_clear).setEnabled(true);
    }

    @Override
    public void OnAddOverlayListener(String resourceName) {
        mDialog.dismiss();
        int resourceId = getResources().getIdentifier(resourceName.toLowerCase().replaceAll(" ", ""), "drawable", getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        CanvasDraggableItem overlay = new CanvasDraggableItem(bitmap,
                mCanvasView.getWidth() / 2,
                mCanvasView.getHeight() / 2,
                resourceName);
        mOverlayAdapter.addItem(overlay);
    }

    @Override
    public void OnAddFilterListener(IFilter filter) {
        mDialog.dismiss();
        mFilterAdapter.addItem(filter);
    }

    @Override
    public void OnAllPresetsRemoved() {
        mMenu.findItem(R.id.action_preset_load).setEnabled(false);
    }

    @Override
    public void OnPresetSaved() {
        mMenu.findItem(R.id.action_preset_load).setEnabled(true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) {
        if (position == ListViewPagerAdapter.PAGE_FILTERS) {
            if (mFilterAdapter.isEmpty())
                mMenu.findItem(R.id.action_clear).setEnabled(false);
            else
                mMenu.findItem(R.id.action_clear).setEnabled(true);
        } else if (position == ListViewPagerAdapter.PAGE_OVERLAYS) {
            if (mOverlayAdapter.isEmpty())
                mMenu.findItem(R.id.action_clear).setEnabled(false);
            else
                mMenu.findItem(R.id.action_clear).setEnabled(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) { }
}
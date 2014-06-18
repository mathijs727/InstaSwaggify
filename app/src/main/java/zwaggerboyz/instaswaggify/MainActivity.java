package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.mobeta.android.dslv.DragSortListView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {
    private DragSortListView mListView;
    private ImageView mImageView;
    private RSFilterHelper mRSFilterHelper;
    private FilterListAdapter mAdapter;
    private FilterDialog mFilterDialog;

    private Uri mImageUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 27031996;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<IFilter> items = new ArrayList<IFilter>();
        for (int i = 0; i < 5; i++) {
            items.add(new SaturationFilter());

        }

        mListView = (DragSortListView) findViewById(R.id.activity_main_listview);
        mImageView = (ImageView) findViewById(R.id.activity_main_imageview);

        mAdapter = new FilterListAdapter(this, items);
        mListView.setAdapter(mAdapter);

        mRSFilterHelper = new RSFilterHelper();
        mRSFilterHelper.createRS(this);
        mRSFilterHelper.setCanvasView(mImageView);
        mRSFilterHelper.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.data));
        List<IFilter> filters = new ArrayList<IFilter>();
        filters.add(new SaturationFilter());
        mRSFilterHelper.generateBitmap(filters);

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
        SoundThread soundThread = new SoundThread(this, R.raw.instaswaggify);
        soundThread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;

        } else if (id == R.id.action_add_filter) {

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
            mFilterDialog = new FilterDialog();
            mFilterDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
            mFilterDialog.show(fragmentTransaction, "dialog");

            return true;
        }

        else if (id == R.id.action_take_photo) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            /* Create a folder to store the pictures if it does not exist yet. */
            File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "InstaSwaggify Original Pictures");
            if (imagesFolder.exists() == false) {
                if (imagesFolder.mkdirs() == false) {
                    Log.i("Take Photo", "no directory created");
                    return true;
                }
            }

            /* Get the current time and date to use in the filename. */

            Date now = new Date();
            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm");

            String date = simpleFormat.format(now);
            Log.i("FILENAME", date + ".jpg");

            File image = new File(imagesFolder, date + ".jpg");
            mImageUri = Uri.fromFile(image);

            /* The intent is started. */
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

            return true;
        }

        else if (id == R.id.action_select_photo) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("YOLOLO", "DATATA");

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    /* The image is converted to a bitmap and send to the FilterHelper object. */
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    mRSFilterHelper.setBitmap(bitmap);
                }
                catch (Exception e) {
                    Log.e("onActivityResult", "create bitmap failed: " + e);
                }
            }

            else {
                //TODO: error message for user
            }
        }
    }

    public void addFilter(int i) {
        mFilterDialog.dismiss();
        mAdapter.add(i);
    }
}

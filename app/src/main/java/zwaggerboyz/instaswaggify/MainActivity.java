package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private DragSortListView mListView;
    private ImageView mImageView;
    private RSFilterHelper mRSFilterHelper;
    private FilterListAdapter mAdapter;

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

        mListView = (DragSortListView)findViewById(R.id.activity_main_listview);
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
        }

        else if (id == R.id.add_filter) {
            mAdapter.add();
            return true;
        }

        else if (id == R.id.take_photo) {
            mAdapter.add();
            return true;
        }

        else if (id == R.id.select_photo) {
            mAdapter.add();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

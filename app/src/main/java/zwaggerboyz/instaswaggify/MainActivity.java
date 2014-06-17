package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mobeta.android.dslv.DragSortListView;

public class MainActivity extends Activity {
    DragSortListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListItemFilter[] items = new ListItemFilter[20];
        for (int i = 0; i < 20; i++) {
            items[i] = new ListItemFilter();
        }

        final FilterArrayAdapter adapter = new FilterArrayAdapter(this, items);
        mListView = (DragSortListView)findViewById(R.id.activity_main_listview);
        mListView = (DragSortListView)findViewById(R.id.activity_main_listview);
        mListView.setAdapter(adapter);
        mListView.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int which) {
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

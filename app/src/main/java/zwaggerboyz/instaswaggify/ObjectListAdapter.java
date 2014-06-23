package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scoud on 21-6-14.
 */
public class ObjectListAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private List<IObject> mItems;
    private List<List<IObject>> mItemsPreviousBuffer = new ArrayList<List<IObject>>();
    private int bufferLevel = 0;
    private Activity mActivity;

    public ObjectListAdapter(Activity activity, List<IObject> items) {
        mInflater = activity.getLayoutInflater();
        mItems = items;
        mActivity = activity;
    }

    private class ViewHolder {
        public TextView nameTextView, descriptionTextView;
        public ImageView thumbnailImageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_object, null);

            viewHolder= new ViewHolder();
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.list_item_object_name);
            viewHolder.descriptionTextView = (TextView) convertView.findViewById(R.id.list_item_object_description);
            viewHolder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.list_item_object_thumbnail);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final IObject item = getItem(position);
        viewHolder.nameTextView.setText(item.getName());

        viewHolder.thumbnailImageView.setImageBitmap(item.getThumbnail());

        return convertView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public IObject getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void add(int filter) {

        switch (filter) {
            case 0:
                mItems.add(new PlaceholderObject(mActivity));
                break;
            default:
                break;
        }
    }

    /* Removes item at index from filter list */
    public void remove(int index) {

        mItems.remove(mItems.get(index));

//        mListener.updateImage(mItems);
        notifyDataSetChanged();
    }

    public void reorder(int from, int to) {
        if (from != to) {

            IObject element = mItems.remove(from);
            mItems.add(to, element);

            notifyDataSetChanged();
//            mListener.updateImage(mItems);
        }
    }
}
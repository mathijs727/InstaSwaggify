package zwaggerboyz.instaswaggify;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Mathijs on 16/06/14.
 */
public class FilterArrayAdapter extends ArrayAdapter<String> {
    private final Activity mContext;
    private String[] mItems;

    private class ViewHolder {
        public TextView title;
    }

    public FilterArrayAdapter(Activity context, String[] mItems) {
        super(context, R.layout.list_item_filter, mItems);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_filter, null);

            viewHolder= new ViewHolder();
            viewHolder.title = (TextView)convertView.findViewById(R.id.list_item_filter_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.title.setText(mItems[position]);
        return convertView;
    }
}

package zwaggerboyz.instaswaggify;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import zwaggerboyz.instaswaggify.filters.IFilter;
import zwaggerboyz.instaswaggify.viewpager.FilterListAdapter;
import zwaggerboyz.instaswaggify.viewpager.OverlayListAdapter;

/**
 * Created by scoud on 25-6-14.
 */
public class HistoryBuffer {

    private List<BufferItem> mBufferItems;
    private undoInterface mListener;

    public HistoryBuffer(undoInterface listener) {
        mBufferItems = new ArrayList<BufferItem>();
        mListener = listener;
    }

    public void updateBuffer(List<IFilter> filterList, List<CanvasDraggableItem> drawableList) {
        Log.v("UPDATE", "UPDATING");
        if (mBufferItems.size() < 25) {
            if (filterList != null)
                mBufferItems.add(new BufferItem(filterList, null));
            else
                mBufferItems.add(new BufferItem(null, drawableList));
            mListener.setUndoState(true);
        } else {
            mBufferItems.remove(0);
            if (filterList != null)
                mBufferItems.add(new BufferItem(filterList, null));
            else
                mBufferItems.add(new BufferItem(null, drawableList));
            mListener.setUndoState(true);
        }
    }

    public void undo(FilterListAdapter filters, OverlayListAdapter overlays) {
        BufferItem temp = mBufferItems.remove(mBufferItems.size() - 1);
        if (temp.type == 0) {
            filters.setItems(temp.mIFilterList);
            filters.updateList();
        } else {
            overlays.setItems(temp.mCanvasDraggableItemList);
        }

        if(mBufferItems.size() == 0)
            mListener.setUndoState(false);
    }

    private class BufferItem {

        ArrayList<IFilter> mIFilterList;
        ArrayList<CanvasDraggableItem> mCanvasDraggableItemList;
        int type;

        public BufferItem(List<IFilter> filterList, List<CanvasDraggableItem> overlayList) {
            if (filterList != null && overlayList == null) {
                mIFilterList = new ArrayList<IFilter>(filterList);
                mCanvasDraggableItemList = null;
                type = 0;
            } else {
                mIFilterList = null;
                mCanvasDraggableItemList = new ArrayList<CanvasDraggableItem>(overlayList);
                type = 1;
            }
        }
    }

    public interface undoInterface {
        public void setUndoState(boolean state);
    }
}

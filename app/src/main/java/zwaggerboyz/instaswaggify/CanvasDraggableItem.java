package zwaggerboyz.instaswaggify;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by Peter on 19-6-2014.
 */
public class CanvasDraggableItem {
    private Rect mRect;
    private int mHalfWidth, mHalfHeight;
    private Bitmap mBitmap;

    public CanvasDraggableItem (Bitmap bitmap, int x, int y) {
        mBitmap = bitmap;
        mHalfWidth = bitmap.getWidth() / 2;
        mHalfHeight = bitmap.getHeight() / 2;
        mRect = new Rect(x - mHalfWidth, y - mHalfHeight, x + mHalfWidth, y + mHalfHeight);
    }

    public void move (int x, int y) {
        mRect.set(x - mHalfWidth, y - mHalfHeight, x + mHalfWidth, y + mHalfHeight);
    }

    public boolean isWithinBounds (int x, int y) {
        return mRect.contains(x,  y);
    }

    public Rect getRect() {
        return mRect;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void resizeImage(double scale) {
        mHalfWidth = (int) (mHalfWidth * scale);
        mHalfHeight = (int) (mHalfHeight * scale);

        /* Make sure the rectangle gets resized */
        move(mRect.centerX(), mRect.centerY());
    }
}
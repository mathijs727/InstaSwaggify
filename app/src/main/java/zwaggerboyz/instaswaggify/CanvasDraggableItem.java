package zwaggerboyz.instaswaggify;

import android.graphics.Bitmap;
import android.graphics.Rect;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    CanvasDraggableItem.java
 * This file contains an overlay with its bitmap and position on the canvas.
 */

public class CanvasDraggableItem {
    private Rect mRect;
    private int mHalfWidth, mHalfHeight;
    private Bitmap mBitmap;
    private float mScaleFactor = 1.F;
    private int xOffset, yOffset;
    private String mName;

    public void calcOffsets(int x, int y) {
        this.xOffset = mRect.left + mHalfWidth - x;
        this.yOffset = mRect.top + mHalfHeight - y;
    }

    public CanvasDraggableItem (Bitmap bitmap, int x, int y, String name) {
        mBitmap = bitmap;
        mHalfWidth = bitmap.getWidth() / 2;
        mHalfHeight = bitmap.getHeight() / 2;
        mRect = new Rect(x - mHalfWidth, y - mHalfHeight, x + mHalfWidth, y + mHalfHeight);
        mName = name;
    }

    public void move (int x, int y) {
        mRect.set(x - mHalfWidth + xOffset, y - mHalfHeight + yOffset, x + mHalfWidth + xOffset, y + mHalfHeight + yOffset);
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
        mHalfWidth = (int) ((mBitmap.getWidth() * scale) / 2);
        mHalfHeight = (int) ((mBitmap.getHeight() * scale) / 2);

        /* Make sure the rectangle gets resized */
        move(mRect.centerX(), mRect.centerY());

    }

    public float getScaleFactor() {
        return mScaleFactor;
    }

    public void setScaleFactor(float scale) {
        mScaleFactor = scale;
    }

    public String getName() {
        return mName;
    }
}
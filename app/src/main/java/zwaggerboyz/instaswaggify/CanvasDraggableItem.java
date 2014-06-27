package zwaggerboyz.instaswaggify;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    CanvasDraggableItem.java
 * This file contains an overlay with its bitmap and position on the canvas.
 */

public class CanvasDraggableItem implements Cloneable {
    private RectF mRect;
    private RectF originalRectF;
    private float mHalfWidth, mHalfHeight;
    private Bitmap mBitmap;
    private float mScaleFactor = 1.F;
    public float xOffset, yOffset;
    public float mAngle;
    private Matrix matrix = new Matrix();
    private boolean flipped;
    private boolean locked = false;
    private String mName;
    private float centerX, centerY;
    private static final float DEFAULT_SIZE = 300;
    private int canvasCenterX, canvasCenterY;


    public CanvasDraggableItem (Bitmap bitmap, int x, int y, String name) {
        mBitmap = bitmap;
        mHalfWidth = bitmap.getWidth() / 2;
        mHalfHeight = bitmap.getHeight() / 2;
        originalRectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mRect = new RectF(originalRectF);

        centerX = 0;
        centerY = 0;
        mAngle = 0;
        mName = name;
        canvasCenterX = x;
        canvasCenterY = y;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            mScaleFactor = DEFAULT_SIZE / bitmap.getWidth();
        }
        else {
            mScaleFactor = DEFAULT_SIZE / bitmap.getHeight();
        }

        if (mScaleFactor > 1)
            mScaleFactor = 1.f;

        move(x, y);
    }

    /* Rotates the bitmap, with the given deltaAngle
     */
    public void rotate(float deltaAngle) {
        mAngle += deltaAngle;
        getMatrix().mapRect(mRect, originalRectF);
    }

    public void resetOffset() {
        centerX -= xOffset;
        centerY -= yOffset;
        xOffset = yOffset = 0;
    }

    /* Flips the bitmap horizontal
     */
    public void flip(Boolean state) {
        flipped = state;
        getMatrix().mapRect(mRect, originalRectF);
    }

    /* Returns the transformation matrix, that is needed to draw the bitmap.
     */
    public Matrix getMatrix() {
        /* The bitmap is first moved so that the center lies on point 0,0.
         * Then it's rotated and scaled around point 0,0. And finally it is translated
         * to the desired location.
         */
        matrix.reset();
        matrix.postTranslate(-mHalfWidth, -mHalfHeight);
        if (flipped) {
            matrix.postRotate(mAngle, 0, 0);
            matrix.postScale(-mScaleFactor, mScaleFactor, 0, 0);
        }
        else {
            matrix.postRotate(-mAngle, 0, 0);
            matrix.postScale(mScaleFactor, mScaleFactor, 0, 0);
        }
        matrix.postTranslate(centerX - xOffset, centerY - yOffset);

        return matrix;
    }

    /* This functions calculates the offset from the center of the bitmap.
     *
     * The offset is needed to prevent that the bitmap jumps when it's moved with
     * a multitouch move event.
     */
    public void calcOffsets(int x, int y) {
        this.xOffset = x - mRect.centerX();
        this.yOffset = y - mRect.centerY();
    }

    /* This functions moves the bitmap
     *
     * x and y are the new location of the center.
     */
    public void move (int x, int y) {
        centerX = x;
        centerY = y;
        getMatrix().mapRect(mRect, originalRectF);
    }

    public void wiggle() {
        move((int)centerX + 1, (int)centerY+1);
    }

    /* Returns true if the given coordinates lies within the boundingbox of the bitmap.
     */
    public boolean isWithinBounds (int x, int y) {
        return mRect.contains(x,  y);
    }

    /* Returns the boundingbox of the bitmap, as a RectF.
     */
    public RectF getRect() {
        return mRect;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    /* This functions resizes the image.
     */
    public void resizeImage(double scale) {
        mScaleFactor *= (float)scale;
        getMatrix().mapRect(mRect, originalRectF);
        calcOffsets((int) centerX, (int) centerY);
    }

    /* This functions sets the scaling factor.
     */
    public void setScaleFactor(double scale) {
        mScaleFactor = (float)scale;
        getMatrix().mapRect(mRect, originalRectF);
        calcOffsets((int) centerX, (int) centerY);
    }

    public float getScaleFactor() {
        return mScaleFactor;
    }

    public String getName() {
        return mName;
    }

    @Override
    public CanvasDraggableItem clone() {
        try {
            CanvasDraggableItem temp = (CanvasDraggableItem) super.clone();
            temp.mRect = new RectF(mRect);
            return temp;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void reset() {
        originalRectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mRect = new RectF(originalRectF);

        centerX = 0;
        centerY = 0;
        mAngle = 0;
        mScaleFactor = 1.0F;
        flipped = false;
        locked = false;

        if (mBitmap.getWidth() > mBitmap.getHeight()) {
            mScaleFactor = DEFAULT_SIZE / mBitmap.getWidth();
        }
        else {
            mScaleFactor = DEFAULT_SIZE / mBitmap.getHeight();
        }

        if (mScaleFactor > 1)
            mScaleFactor = 1.f;

        move(canvasCenterX, canvasCenterY);
    }

    public void lock(Boolean state) {
        this.locked = state;
    }

    public boolean getLockedState() {
        return locked;
    }

    public boolean getFlippedState() {
        return flipped;
    }
}
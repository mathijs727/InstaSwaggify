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
    public float mAngle;
    private Matrix matrix = new Matrix();
    private boolean flipped;
    private String mName;
    private float centerX, centerY;
    private static final float DEFAULT_SIZE = 300;

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

        matrix.postTranslate(centerX, centerY);

        return matrix;
    }

    /* This functions moves the bitmap
     *
     * x and y are the new location of the center.
     */
    public void move (float x, float y) {
        centerX = x;
        centerY = y;
    }

    public void relativeMove (float deltaX, float deltaY) {
        centerX += deltaX;
        centerY += deltaY;
    }

    /* Rotates the bitmap, with the given deltaAngle
     */
    public void rotate(float deltaAngle) {
        mAngle += deltaAngle;
    }

    /* Flips the bitmap horizontal
     */
    public void flip() {
        flipped ^= true;
    }

    /* This functions resizes the image.
     */
    public void resizeImage(double scale) {
        mScaleFactor *= (float)scale;
    }

    /* This functions sets the scaling factor.
     */
    public void setScaleFactor(double scale) {
        mScaleFactor = (float)scale;
    }

    /* Returns true if the given coordinates lies within the boundingbox of the bitmap.
     */
    public boolean isWithinBounds (int x, int y) {
        return mRect.contains(x,  y);
    }

    /* Returns the boundingbox of the bitmap, as a RectF.
     */
    public RectF getRect() {
        getMatrix().mapRect(mRect, originalRectF);
        return mRect;
    }

    public Bitmap getBitmap() {
        return mBitmap;
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
}
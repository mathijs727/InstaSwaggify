package zwaggerboyz.instaswaggify;

import android.graphics.Bitmap;
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

public class CanvasDraggableItem {
    private RectF mRect;
    private RectF originalRectF;
    private float mHalfWidth, mHalfHeight;
    private Bitmap mBitmap;
    private float mScaleFactor = 1.F;
    public float xOffset, yOffset;
    public float mAngle;
    private Matrix matrix = new Matrix();
    private boolean flipped;
    private String mName;

    private float centerX, centerY;

    public void rotate(float deltaAngle) {
        mAngle += deltaAngle;
    }

    public void resetOffset() {
        centerX -= xOffset;
        centerY -= yOffset;
        xOffset = yOffset = 0;
    }

    public void flip() {
        flipped ^= true;
    }

    public Matrix getMatrix() {
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


    private static final int DEFAULT_SIZE = 150;

    public void calcOffsets(int x, int y) {
        this.xOffset = x - mRect.centerX();
        this.yOffset = y - mRect.centerY();
    }

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
        //TODO: resize
        move(x, y);
    }

    public void move (int x, int y) {
        centerX = x;
        centerY = y;
        getMatrix().mapRect(mRect, originalRectF);
    }

    public boolean isWithinBounds (int x, int y) {
        return mRect.contains(x,  y);
    }

    public RectF getRect() {
        return mRect;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void resizeImage(double scale) {
        mScaleFactor *= (float)scale;
        getMatrix().mapRect(mRect, originalRectF);
        calcOffsets((int) centerX, (int) centerY);
    }

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
        
}
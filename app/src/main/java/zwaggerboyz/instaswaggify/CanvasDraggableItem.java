package zwaggerboyz.instaswaggify;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by Peter on 19-6-2014.
 */
public class CanvasDraggableItem {
    private Rect mRect;
    private RectF mRotatedRect;
    private RectF originalRectF;
    private int mHalfWidth, mHalfHeight;
    private Bitmap mBitmap;
    private float mScaleFactor = 1.F;
    private int xOffset, yOffset;
    public float mAngle;
    private float oldScale;
    private Matrix rotMatrix = new Matrix();

    private int oldX, oldY;

    public void addmAngle(float deltaAngle) {
        mAngle += deltaAngle;
        Log.i("Pevid", "angle "+mAngle);
    }

    public Matrix getMatrix() {
        rotMatrix.reset();
        rotMatrix.setTranslate(oldX, oldY);
        rotMatrix.preScale(-mScaleFactor, -mScaleFactor);
        rotMatrix.preRotate(mAngle, mHalfWidth, mHalfHeight);

        return rotMatrix;
    }

    public RectF getmRotatedRect() {
        return mRotatedRect;
    }

    public void calcOffsets(int x, int y) {
        this.xOffset = mRect.left + mHalfWidth - x;
        this.yOffset = mRect.top + mHalfHeight - y;
        Log.i("Pevid", "xoffset " + xOffset + " yoffset " + yOffset);
    }

    public CanvasDraggableItem (Bitmap bitmap, int x, int y) {
        mBitmap = bitmap;
        mHalfWidth = bitmap.getWidth() / 2;
        mHalfHeight = bitmap.getHeight() / 2;
        mRect = new Rect(x - mHalfWidth, y - mHalfHeight, x + mHalfWidth, y + mHalfHeight);
        originalRectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mRotatedRect = new RectF();

        rotMatrix.preTranslate(100 - mHalfWidth, 100 -mHalfHeight);
        rotMatrix.mapRect(mRotatedRect, originalRectF);
        oldX = oldY = 100;
        oldScale = 1F;

    }

    public void move (int x, int y) {
        Log.i("Pevid", "scale " + mScaleFactor + " angle " + mAngle);
        oldX = x;
        oldY = y;
        getMatrix().mapRect(mRotatedRect, originalRectF);
    }

    public boolean isWithinBounds (int x, int y) {
        return mRotatedRect.contains(x,  y);
    }

    public RectF getRect() {
        return mRotatedRect;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public Bitmap getRotatedBitmap() {
        return mBitmap;
    }

    public void resizeImage(double scale) {
        mScaleFactor = (float)scale;
        getMatrix().mapRect(mRotatedRect, originalRectF);
        oldScale = mScaleFactor;
    }

    public float getScaleFactor() {
        return mScaleFactor;
    }

    public void setScaleFactor(float scale) {
        mScaleFactor = scale;
    }
}
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
    private RectF mRotatedRect;
    private RectF originalRectF, temp;
    private float mHalfWidth, mHalfHeight;
    private Bitmap mBitmap;
    private float mScaleFactor = 1.F;
    public float xOffset, yOffset;
    public float mAngle;
    private Matrix rotMatrix = new Matrix();

    private float oldX, oldY;

    public void addmAngle(float deltaAngle) {
        mAngle += deltaAngle;
        Log.i("Pevid", "angle " + mAngle);
    }

    public void resetOffset() {
        xOffset = yOffset = 0;
    }

    public Matrix getMatrix() {
        rotMatrix.reset();
        rotMatrix.postTranslate(oldX - xOffset, oldY - yOffset);
        rotMatrix.postRotate(-mAngle, oldX + mHalfWidth - xOffset, oldY + mHalfHeight - yOffset);
        rotMatrix.postScale(mScaleFactor, mScaleFactor, oldX + mHalfWidth - xOffset, oldY + mHalfWidth - yOffset);

        return rotMatrix;
    }

    public Matrix getMatrix2() {
        rotMatrix.reset();
        rotMatrix.postTranslate(oldX - xOffset, oldY - yOffset);
        return rotMatrix;

    }

    public void calcOffsets(int x, int y) {
        //this.xOffset = mRotatedRect.left + mRotatedRect.width() - x;
        //this.yOffset = mRotatedRect.top + mRotatedRect.height() - y;
        //this.xOffset = x - originalRectF.left;
        //this.yOffset = y - originalRectF.top ;
        rotMatrix.reset();
        rotMatrix.postTranslate(oldX - xOffset, oldY - yOffset);
        rotMatrix.mapRect(temp, originalRectF);

        this.xOffset = x - temp.left;
        this.yOffset = y - temp.top ;

        Log.i("Pevid", "xoffset " + xOffset + " yoffset " + yOffset);
    }

    public CanvasDraggableItem (Bitmap bitmap, int x, int y) {
        mBitmap = bitmap;
        mHalfWidth = bitmap.getWidth() / 2;
        mHalfHeight = bitmap.getHeight() / 2;
        originalRectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mRotatedRect = new RectF(originalRectF);
        temp = new RectF();

        oldX = 0;
        oldY = 0;
        mAngle = 0;

        move(x, y);
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
        //mHalfWidth = mRotatedRect.width() / 2;
        //mHalfHeight = mRotatedRect.height() / 2;
        Log.i("Pevid", "mHalfWidth " + mHalfWidth);
        Log.i("Pevid", "mHalfHeight " + mHalfHeight);
        calcOffsets((int)oldX, (int)oldY);
    }

    public float getScaleFactor() {
        return mScaleFactor;
    }

    public void setScaleFactor(float scale) {
        mScaleFactor = scale;
    }
}
package zwaggerboyz.instaswaggify;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by Peter on 19-6-2014.
 */
public class CanvasDraggableItem {
    private float left, top, right, bottom;
    private Bitmap bitmap;

    public CanvasDraggableItem (Bitmap bitmap, float left, float top, float right, float bottom) {
        this.bitmap = bitmap;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public void move (float newX, float newY) {
        if (newX >= 0 && newX <= 1 && newY >= 0 && newY <= 1) {
            float width = right - left;
            float height = top - bottom;

            left = newX - 0.5f * width;
            right = newX + 0.5f * width;
            top = newY - 0.5F * height;
            bottom = newY + 0.5f * height;
        }
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public boolean isWithinBounds (float x, float y) {
        return (x >= left && x <= right && y >= top && y <= bottom);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}

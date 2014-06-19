package zwaggerboyz.instaswaggify;

import android.graphics.Bitmap;

/**
 * Created by Peter on 19-6-2014.
 */
public class CanvasDraggableItem {
    private float x, y;
    private int width, height;
    private boolean selected = false;
    private Bitmap bitmap;

    public CanvasDraggableItem (Bitmap bitmap, float x, float y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

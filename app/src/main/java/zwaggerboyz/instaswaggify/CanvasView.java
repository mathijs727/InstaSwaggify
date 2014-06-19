package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Peter on 19-6-2014.
 */

public class CanvasView extends View  {
    private Bitmap bitmap;
    private CanvasDraggableItem [] pictures = new CanvasDraggableItem [10];
    private Rect destination = null;

    private void finishConstructor() {
        this.setOnTouchListener(new CanvasTouchListener(this));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.swag);
        pictures[0] = new CanvasDraggableItem(bitmap, 0.2f, 0.2f);
    }

    // finishConstructor
    public CanvasView(Context context) {
        super(context);
        finishConstructor();
    }

    // finishConstructor
    public CanvasView(Context context, AttributeSet attributes) {
        super(context, attributes);
        finishConstructor();
    }

    // finishConstructor
    public CanvasView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        finishConstructor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /* If there is no destination calculated yet, we have to calculate it now. */
        if (destination == null) {
            int canvasHeight = getHeight();
            int canvasWidth = getWidth();
            int bitmapHeight = bitmap.getHeight();
            int bitmapWidth = bitmap.getWidth();

            if (((float) bitmapHeight / (float) bitmapWidth) < ((float) canvasHeight / (float) canvasWidth)) {
                float scale = (float) canvasWidth / (float) bitmapWidth;
                int offset = Math.round((canvasHeight - (bitmapHeight * scale)) * 0.5f);
                destination = new Rect(0, offset, canvasWidth, canvasHeight - offset);
            }
            else if (((float) bitmapHeight / (float) bitmapWidth) > ((float) canvasHeight / (float) canvasWidth)) {
                float scale = (float) canvasHeight / (float) bitmapHeight;
                int offset = Math.round((canvasWidth - (bitmapWidth * scale)) * 0.5f);
                destination = new Rect(offset, 0, canvasWidth - offset, canvasHeight);
            }
            else {
                destination = new Rect(0, 0, canvasWidth, canvasHeight);
            }
        }

        canvas.drawColor(R.color.background);

        canvas.drawBitmap(bitmap, null, destination, null );

        for (CanvasDraggableItem picture : pictures) {
            if (picture != null) {
                canvas.drawBitmap(picture.getBitmap(),
                    destination.left + destination.width() * picture.getX() - 0.5f * picture.getWidth(),
                    destination.top + destination.height() * picture.getY() - 0.5f * picture.getHeight(),
                    null);
            }
        }
    }

    public void onTouchDown (int x, int y) {
        for (CanvasDraggableItem picture : pictures) {
            // check if on coordinades
            // set selected
        }
    }

    public void onTouchMove (int x, int y) {
        // move selected

    }

    public void onTouchUp () {
        for (CanvasDraggableItem picture : pictures) {
            if (picture != null && picture.isSelected()) {
                picture.setSelected(false);
            }
        }
    }

    public void setBitmap (Bitmap newBitmap) {
        bitmap = newBitmap;
        destination = null;
    }

    public Bitmap getBitmap () {
        return bitmap;
    }
}



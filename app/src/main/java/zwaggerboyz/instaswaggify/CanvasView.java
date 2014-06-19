package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Peter on 19-6-2014.
 */

public class CanvasView extends View  {
    private Bitmap bitmap;
    private Rect destination = null;
    private ArrayList<CanvasDraggableItem> pictures = new ArrayList<CanvasDraggableItem>();
    private CanvasDraggableItem selected = null;
    private int xoffset, yoffset;

    private void finishConstructor() {
        this.setOnTouchListener(new CanvasTouchListener(this));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blazeit);
        pictures.add(new CanvasDraggableItem(bitmap, 0.45f, 0.45f, 0.55f, 0.55f));
    }

    public CanvasView(Context context) {
        super(context);
        finishConstructor();
    }

    public CanvasView(Context context, AttributeSet attributes) {
        super(context, attributes);
        finishConstructor();
    }

    public CanvasView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        finishConstructor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(R.color.background);

        /* If there is no destination calculated yet, we have to calculate it now. */
        if (destination == null) {
            updateDestination();
        }

        canvas.drawBitmap(bitmap, null, destination, null );

        for (CanvasDraggableItem picture : pictures) {
            if (picture != null) {
                Rect pictureDestination = new Rect (
                    (int)(destination.left + destination.width() * picture.getLeft()),
                    (int)(destination.top + destination.height() * picture.getTop()),
                    (int)(destination.left + destination.width() * picture.getRight()),
                    (int)(destination.top + destination.height() * picture.getBottom())
                    );
                canvas.drawBitmap(picture.getBitmap(), null, pictureDestination, null);
            }
        }
    }

    private void updateDestination () {
        int canvasHeight = getHeight();
        int canvasWidth = getWidth();
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();

        if (((float) bitmapHeight / (float) bitmapWidth) < ((float) canvasHeight / (float) canvasWidth)) {
            float scale = (float) canvasWidth / (float) bitmapWidth;
            yoffset = Math.round((canvasHeight - (bitmapHeight * scale)) * 0.5f);
            xoffset = 0;
            destination = new Rect(0, yoffset, canvasWidth, canvasHeight - yoffset);
        }
        else if (((float) bitmapHeight / (float) bitmapWidth) > ((float) canvasHeight / (float) canvasWidth)) {
            float scale = (float) canvasHeight / (float) bitmapHeight;
            xoffset = Math.round((canvasWidth - (bitmapWidth * scale)) * 0.5f);
            yoffset = 0;
            destination = new Rect(xoffset, 0, canvasWidth - xoffset, canvasHeight);
        }
        else {
            xoffset = 0;
            yoffset = 0;
            destination = new Rect(0, 0, canvasWidth, canvasHeight);
        }
    }

    public void onTouchDown (float x, float y) {
        float relativex = (x - xoffset) / (getWidth() - 2.0f * xoffset);
        float relativey = (y - yoffset) / (getHeight() - 2.0f * yoffset);

        for (CanvasDraggableItem picture : pictures) {
            if (picture.isWithinBounds(relativex, relativey)) {
                selected = picture;
                return;
            }
        }

        this.invalidate();
    }

    public void onTouchMove (float x, float y) {
        if (selected != null) {
            float relativex = (x - xoffset) / (getWidth() - 2.0f * xoffset);
            float relativey = (y - yoffset) / (getHeight() - 2.0f * yoffset);

            selected.move(relativex, relativey);
        }

        this.invalidate();
    }

    public void onTouchUp (float x, float y) {
        selected = null;

        for (CanvasDraggableItem picture : pictures) {
            Log.i("IK BEN ER NOG", picture.getLeft() + " : " + picture.getRight());
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



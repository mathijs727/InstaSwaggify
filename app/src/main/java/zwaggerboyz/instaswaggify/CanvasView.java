package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Peter on 19-6-2014.
 */

public class CanvasView extends View {
    private Bitmap bitmap;
    private Bitmap [] pictures = new Bitmap [10];
    private Rect destination = null;

    // constructor
    public CanvasView(Context context) {
        super(context);
        pictures[0] = BitmapFactory.decodeFile("@drawable/swag");
        bitmap = BitmapFactory.decodeFile("@drawable/swag");
    }

    // constructor
    public CanvasView(Context context, AttributeSet attributes) {
        super(context, attributes);
        pictures[0] = BitmapFactory.decodeFile("@drawable/swag");
        bitmap = BitmapFactory.decodeFile("@drawable/swag");
    }

    // constructor
    public CanvasView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        pictures[0] = BitmapFactory.decodeFile("@drawable/swag");
        bitmap = BitmapFactory.decodeFile("@drawable/swag");
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

            Log.i("WAARDES:", "" + canvasHeight + " " + canvasWidth + " : " + bitmapHeight + " " + bitmapWidth);

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

        canvas.drawColor(Color.RED);

        canvas.drawBitmap(bitmap, null, destination, null );
    }

    public void setImageBitmap (Bitmap newBitmap) {
        bitmap = newBitmap;
        destination = null;
    }

}



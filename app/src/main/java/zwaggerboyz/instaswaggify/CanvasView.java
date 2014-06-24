package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Swaggenegger on 19-6-2014.
 */

public class CanvasView extends View  {
    private Bitmap mBitmap;
    private Rect mBackgroundRect = new Rect();
    private ArrayList<CanvasDraggableItem> mOverlays = new ArrayList<CanvasDraggableItem>();
    private CanvasDraggableItem mSelected = null;
    private int mXOffset, mYOffset;
    private double mImgScale = 1.0;
    private int xOffSet, yOffset;
    private float mRotation = 1.F;

    public RotationGestureDetector mRotationGesture;
    public ScaleGestureDetector mScaleDetector;

    private void finishConstructor() {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blazeit);
        //mOverlays.add(new CanvasDraggableItem(bitmap, 100, 100));
        //mOverlays.add(new CanvasDraggableItem(bitmap, 200, 100));
    }

    public CanvasView(Context context) {
        super(context);
        setOnTouchListener(new CanvasTouchListener(this));
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setRotationGesture();
        finishConstructor();
    }

    public CanvasView(Context context, AttributeSet attributes) {
        super(context, attributes);
        setOnTouchListener(new CanvasTouchListener(this));
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setRotationGesture();
        finishConstructor();
    }

    public CanvasView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        setOnTouchListener(new CanvasTouchListener(this));
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setRotationGesture();
        finishConstructor();
    }

    private void setRotationGesture() {
        mRotationGesture = new RotationGestureDetector(new RotationGestureDetector.OnRotationGestureListener() {
            @Override
            public boolean OnRotation(RotationGestureDetector rotationDetector) {
                mRotation += -(Math.toRadians(rotationDetector.getAngle()) * 100);
                return false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.rotate(mRotation, canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawColor(R.color.background);
        canvas.drawBitmap(mBitmap, null, mBackgroundRect, null);


        for (CanvasDraggableItem overlay : mOverlays) {
            canvas.drawBitmap(overlay.getBitmap(), null, overlay.getRect(), null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        recalculateSize(w, h, false);
    }

    public void recalculateSize(int width, int height, boolean newImage) {
        int centreX = width / 2;
        int centreY = height / 2;

        int left, top, right, bot;

        double imgXScale = (double)width / mBitmap.getWidth();
        double imgYScale = (double)height / mBitmap.getHeight();

        if (imgXScale > imgYScale) {
            top = 0;
            bot = height;
            left = centreX - (int)(mBitmap.getWidth() * imgYScale / 2.0);
            right = width - left;

            if (!newImage) {
                Rect rect;
                int cX, cY, nX, nY;
                for (CanvasDraggableItem overlay : mOverlays) {
                    rect = overlay.getRect();
                    cX = rect.centerX() - mXOffset;
                    cY = rect.centerY() - mYOffset;

                    nX = (int) (cX / mImgScale * imgYScale) + left;
                    nY = (int) (cY / mImgScale * imgYScale) + top;

                    overlay.move(nX, nY);
                    overlay.resizeImage(imgYScale / mImgScale);
                }
            }
            mImgScale = imgYScale;
        } else {
            left = 0;
            right = width;
            top = centreY - (int)(mBitmap.getHeight() * imgXScale / 2.0);
            bot = height - top;

            if (!newImage) {
                Rect rect;
                int cX, cY, nX, nY;
                for (CanvasDraggableItem overlay : mOverlays) {
                    rect = overlay.getRect();
                    cX = rect.centerX() - mXOffset;
                    cY = rect.centerY() - mYOffset;

                    nX = (int) (cX / mImgScale * imgXScale) + left;
                    nY = (int) (cY / mImgScale * imgXScale) + top;
                    overlay.move(nX, nY);
                    overlay.resizeImage(imgXScale / mImgScale);
                }
            }
            mImgScale = imgXScale;
        }

        if (newImage) {
            int x, y;
            for (CanvasDraggableItem overlay : mOverlays) {
                x = overlay.getRect().centerX();
                y = overlay.getRect().centerY();

                if (x < left)
                    x = left;
                if (x > right)
                    x = right;
                if (y < top)
                    y = top;
                if (y > bot)
                    y = bot;

                overlay.move(x, y);
            }
        }

        mXOffset = left;
        mYOffset = top;
        mBackgroundRect.set(left, top, right, bot);
    }

    public void onTouchDown (int x, int y) {
        /* Loop through the items backwards because the last item in the list is the top most item */
        int length = mOverlays.size();
        CanvasDraggableItem overlay;
        for (int i = length - 1; i >= 0; i--) {
            overlay = mOverlays.get(i);
            if (overlay.isWithinBounds(x, y)) {
                overlay.calcOffsets(x, y);
                mSelected = overlay;
                return;
            }
        }

        this.invalidate();
    }

    public void onTouchMove (int x, int y) {
        if (x < mXOffset ||
            x > (getWidth() - mXOffset) ||
            y < mYOffset ||
            y > (getHeight() - mYOffset))
            return;

        if (mSelected != null) {
            mSelected.move(x, y);
        }

        this.invalidate();
    }

    public void onTouchUp (int x, int y) {
        mSelected = null;
        xOffSet = yOffset = 0;
    }

    public void onPointerUp(int x, int y) {
        if (mSelected != null)
            mSelected.calcOffsets((int)mScaleDetector.getFocusX(), (int)mScaleDetector.getFocusY());
    }

    public void switchPointer(int x, int y) {
        if (mSelected != null) {
            mSelected.calcOffsets(x, y);
        }
    }

    public void setBitmap (Bitmap bitmap) {
        mBitmap = bitmap;

        if (getWidth() != 0) {
            recalculateSize(getWidth(), getHeight(), true);
        }
        Log.v("CanvasView", "Canvas width: " + getWidth());
    }

    public Bitmap getBitmap () {
        return mBitmap;
    }

    public void addDraggable (CanvasDraggableItem item) {
        mOverlays.add(item);
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if(mSelected == null) {
                return true;
            }

            mSelected.setScaleFactor(mSelected.getScaleFactor() * detector.getScaleFactor());

            // Don't let the object get too small or too large.
            mSelected.setScaleFactor(Math.max(0.1f, Math.min(mSelected.getScaleFactor(), 10.0f)));

            mSelected.resizeImage(mSelected.getScaleFactor());

            invalidate();
            return true;

        }
    }
}



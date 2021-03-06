package zwaggerboyz.instaswaggify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.List;

import zwaggerboyz.instaswaggify.dialogs.OverlaySettingsDialog;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    CanvasView.java
 * This file contains the canvas-view that draw the bitmap and overlays.
 */

public class CanvasView extends View {
    private Bitmap mBitmap;
    private Rect mBackgroundRect = new Rect();
    private List<CanvasDraggableItem> mOverlays;
    private CanvasDraggableItem mSelected = null;
    private int mXOffset, mYOffset;
    private onOverlayChangeListener mListener;
    public RotationGestureDetector mRotationGesture;
    public ScaleGestureDetector mScaleDetector;
    private double mImgScale = 1.0;
    private Paint selectedPaint = new Paint();

    public CanvasView(Context context) {
        super(context);
        setup(context);
    }

    public CanvasView(Context context, AttributeSet attributes) {
        super(context, attributes);
        setup(context);
    }

    public CanvasView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        setup(context);
    }

    private void setup(Context context) {
        setOnTouchListener(new CanvasTouchListener(this));
        setRotationGesture();
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        selectedPaint.setColor(Color.CYAN);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(3);
        this.setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.drawColor(R.color.background);
        canvas.drawBitmap(mBitmap, null, mBackgroundRect, null);

        /* draw overlays on canvas */
        for (int i = mOverlays.size() - 1; i >= 0; i--) {
            CanvasDraggableItem overlay = mOverlays.get(i);
            canvas.drawBitmap(overlay.getBitmap(), overlay.getMatrix(), null);
            if (overlay == mSelected)
                canvas.drawRect(overlay.getRect().left, overlay.getRect().top, overlay.getRect().right, overlay.getRect().bottom, selectedPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        recalculateSize(w, h, false);
    }

    /* defines position of manipulated picture */
    public void recalculateSize(int width, int height, boolean newImage) {
        int centreX = width / 2;
        int centreY = height / 2;

        int left, top, right, bot;

        double imgXScale = (double)width / mBitmap.getWidth();
        double imgYScale = (double)height / mBitmap.getHeight();

        /* manage scaling for pictures in portrait format */
        if (imgXScale > imgYScale) {
            top = 0;
            bot = height;
            left = centreX - (int)(mBitmap.getWidth() * imgYScale / 2.0);
            right = width - left;

            if (!newImage) {
                RectF rect;
                int cX, cY, nX, nY;
                for (CanvasDraggableItem overlay : mOverlays) {
                    rect = overlay.getRect();
                    cX = (int)(rect.centerX() - mXOffset);
                    cY = (int)(rect.centerY() - mYOffset);

                    nX = (int) (cX / mImgScale * imgYScale) + left;
                    nY = (int) (cY / mImgScale * imgYScale) + top;

                    overlay.move(nX, nY);
                    overlay.resizeImage(imgYScale / mImgScale);
                }
            }
            mImgScale = imgYScale;
        }
        /* manage scaling for pictures in landscape format */
        else {
            left = 0;
            right = width;
            top = centreY - (int)(mBitmap.getHeight() * imgXScale / 2.0);
            bot = height - top;

            if (!newImage) {
                RectF rect;
                int cX, cY, nX, nY;
                for (CanvasDraggableItem overlay : mOverlays) {
                    rect = overlay.getRect();
                    cX = (int)(rect.centerX() - mXOffset);
                    cY = (int)(rect.centerY() - mYOffset);

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
                x = (int)overlay.getRect().centerX();
                y = (int)overlay.getRect().centerY();

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

        for (int i = 0; i < length; i++) {
            overlay = mOverlays.get(i);
            if (overlay.isWithinBounds(x, y) && !overlay.getLockedState()) {
                overlay.calcOffsets(x, y);
                mSelected = overlay;
                mListener.updateBuffer();
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
        if (mSelected != null) {
            mSelected.resetOffset();
            mSelected.wiggle();
        }
        mSelected = null;
    }

    public void switchPointer(int x, int y) {
        if (mSelected != null) {
            mSelected.calcOffsets(x, y);
        }
    }

    public void setOverlays(List<CanvasDraggableItem> overlays) {
        mOverlays = overlays;
    }

    public void setBitmap (Bitmap bitmap, boolean newImage) {
        mBitmap = bitmap;

        if (newImage && getWidth() != 0) {
            recalculateSize(getWidth(), getHeight(), true);
        }
    }

    public Bitmap getBitmap () {
        buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(getDrawingCache(),
                                                   mXOffset,
                                                   mYOffset,
                                                   getWidth() - 2*mXOffset,
                                                   getHeight() -  2*mYOffset);
        destroyDrawingCache();
        return bitmap;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if(mSelected == null) {
                return true;
            }
            mSelected.setScaleFactor(mSelected.getScaleFactor() * detector.getScaleFactor());

            /* Don't let the object get too small or too large. */
            mSelected.setScaleFactor(Math.max(0.1f, Math.min(mSelected.getScaleFactor(), 10.0f)));
            mSelected.setScaleFactor(mSelected.getScaleFactor());
            invalidate();

            return true;
        }
    }

    private void setRotationGesture() {
        mRotationGesture = new RotationGestureDetector(new RotationGestureDetector.OnRotationGestureListener() {
            @Override
            public boolean OnRotation(RotationGestureDetector rotationDetector) {
                if (mSelected == null)
                    return true;

                mSelected.rotate(rotationDetector.getAngle());
                return false;
            }
        });
    }

    public void setOnOverlayChangeListener(onOverlayChangeListener listener) {
        mListener = listener;
    }

    public void setSelectedOverlay(CanvasDraggableItem selected) {
        mSelected = selected;
    }

    public CanvasDraggableItem getSelectedOverlay() {
        return mSelected;
    }

    public interface onOverlayChangeListener {
        public void updateBuffer();
    }
}

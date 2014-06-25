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

import java.util.ArrayList;
import java.util.List;

import zwaggerboyz.instaswaggify.viewpager.OverlayListAdapter;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    CanvasView.java
 * This file contains the canvas-view that draw the bitmap and overlays.
 */

public class CanvasView extends View  {
    private Bitmap mBitmap;
    private Rect mBackgroundRect = new Rect();
    private List<CanvasDraggableItem> mOverlays;
    private CanvasDraggableItem mSelected = null;
    private int mXOffset, mYOffset;
    private double mImgScale = 1.0;
    private int xOffSet, yOffset;

    public RotationGestureDetector mRotationGesture;
    public ScaleGestureDetector mScaleDetector;

    public CanvasView(Context context) {
        super(context);
        setOnTouchListener(new CanvasTouchListener(this));
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setRotationGesture();
        this.setDrawingCacheEnabled(true);
    }

    public CanvasView(Context context, AttributeSet attributes) {
        super(context, attributes);
        setOnTouchListener(new CanvasTouchListener(this));
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setRotationGesture();
        this.setDrawingCacheEnabled(true);
    }

    public CanvasView(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);
        setOnTouchListener(new CanvasTouchListener(this));
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setRotationGesture();
        this.setDrawingCacheEnabled(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.setDrawingCacheEnabled(false);
        this.setDrawingCacheEnabled(true);

        super.onDraw(canvas);

        canvas.save();
        canvas.drawColor(R.color.background);
        canvas.drawBitmap(mBitmap, null, mBackgroundRect, null);

        for (CanvasDraggableItem overlay : mOverlays) {
            canvas.drawBitmap(overlay.getBitmap(), overlay.getMatrix(), null);
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
        } else {
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
        if (mSelected != null) {
            mSelected.resetOffset();
        }
        mSelected = null;
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

    public void setOverlays(List<CanvasDraggableItem> overlays) {
        mOverlays = overlays;
    }

    public void setBitmap (Bitmap bitmap) {
        mBitmap = bitmap;

        if (getWidth() != 0) {
            recalculateSize(getWidth(), getHeight(), true);
        }
    }

    public Bitmap getBitmap () {
        return this.getDrawingCache();
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
}



package zwaggerboyz.instaswaggify;

import android.view.MotionEvent;
import android.view.View;

/*
 * APP:     InstaSwaggify
 * DATE:    June 2014
 * NAMES:   Mathijs Molenaar, Tristan van Vaalen, David Veenstra, Peter Verkade, Matthijs de Wit,
 *          Arne Zismer
 *
 * FILE:    CanvasTouchListener.java
 * This file contains a listener for touch-events on the canvas.
 */

public class CanvasTouchListener implements View.OnTouchListener {
    private CanvasView mCanvasView;
    private int currentPointer;
    private boolean switchPrimaryPointer;

    public CanvasTouchListener(CanvasView creator) {
        mCanvasView = creator;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mCanvasView.mScaleDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                currentPointer = event.getPointerId(0);
                mCanvasView.onTouchDown((int)event.getX(), (int)event.getY());
                return true;

            case MotionEvent.ACTION_MOVE: {
                mCanvasView.onTouchMove((int) event.getX(), (int) event.getY());

                return true;
            }

            case MotionEvent.ACTION_UP:
                mCanvasView.onTouchUp((int)event.getX(), (int)event.getY());
                return true;

            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (currentPointer == event.getPointerId(event.getActionIndex())) {
                    switchPrimaryPointer = true;
                }
                else {
                    switchPrimaryPointer = false;
                }

                if (event.getPointerCount() == 2) {
                    mCanvasView.onPointerUp((int)event.getX(), (int)event.getY());
                }

                return true;

            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (switchPrimaryPointer && event.getPointerCount() == 2) {
                    int secondPointer = event.getPointerId(event.getActionIndex());
                    mCanvasView.switchPointer((int)event.getX(secondPointer), (int)event.getY(secondPointer));
                }
                return true;

            default:
                return false;
        }
    }
}

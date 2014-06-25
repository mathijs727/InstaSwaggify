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
    private int firstFingerID = INVALID_POINTER_ID;
    private int secondFingerID = INVALID_POINTER_ID;

    private static final int INVALID_POINTER_ID = -1;

    public CanvasTouchListener(CanvasView creator) {
        mCanvasView = creator;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mCanvasView.mScaleDetector.onTouchEvent(event);
        mCanvasView.mRotationGesture.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                firstFingerID = event.getPointerId(0);
                mCanvasView.onTouchDown((int) event.getX(), (int) event.getY());

                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                int pointerIndex;

                if (firstFingerID != INVALID_POINTER_ID)
                    pointerIndex = event.findPointerIndex(firstFingerID);
                else
                    pointerIndex = event.getActionIndex();

                /* The bitmap is moved based on the coordinates of the firstfinger */
                mCanvasView.onTouchMove((int) event.getX(pointerIndex), (int) event.getY(pointerIndex));
                return true;
            }

            case MotionEvent.ACTION_UP: {
                firstFingerID = secondFingerID = INVALID_POINTER_ID;
                mCanvasView.onTouchUp(0, 0);
                return true;
            }

            /* Deprecated events are still being generated
             */
            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_3_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                int pointerId = event.getPointerId(event.getActionIndex());
                if (firstFingerID == pointerId) {
                    firstFingerID = secondFingerID;

                    /* the offsets have to recalculated when
                     * the second finger becomes the first finger
                     */
                    if (secondFingerID != INVALID_POINTER_ID) {
                        int index = event.findPointerIndex(secondFingerID);

                        mCanvasView.switchPointer((int) event.getX(index), (int) event.getY(index));
                    }

                    secondFingerID = INVALID_POINTER_ID;
                }
                else if (secondFingerID == pointerId) {
                    secondFingerID = INVALID_POINTER_ID;
                }

                return true;
            }

            /* Deprecated events are still being generated
             */
            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_3_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                secondFingerID = event.getPointerId(event.getActionIndex());
                return true;
            }

            default:
                return false;
        }
    }
}
package zwaggerboyz.instaswaggify;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

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
    private float oldX, oldY;

    private static final int INVALID_POINTER_ID = -1;

    public CanvasTouchListener(CanvasView creator) {
        mCanvasView = creator;
    }

    float calcCenterX (MotionEvent event) {
        int index1 = event.findPointerIndex(firstFingerID);
        int index2 = event.findPointerIndex(secondFingerID);

        return (event.getX(index1) + event.getX(index2)) / 2.f;
    }

    float calcCenterY (MotionEvent event) {
        int index1 = event.findPointerIndex(firstFingerID);
        int index2 = event.findPointerIndex(secondFingerID);

        return (event.getY(index1) + event.getY(index2)) / 2.f;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mCanvasView.mScaleDetector.onTouchEvent(event);
        mCanvasView.mRotationGesture.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                firstFingerID = event.getPointerId(0);

                oldX = event.getX();
                oldY = event.getY();

                mCanvasView.onTouchDown((int) oldX, (int) oldY);

                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                int pointerIndex;
                float newX, newY, deltaX, deltaY;

                if (firstFingerID != INVALID_POINTER_ID && secondFingerID != INVALID_POINTER_ID) {
                    newX = calcCenterX(event);
                    newY = calcCenterY(event);

                    deltaX = newX - oldX;
                    deltaY = newY - oldY;

                    oldX = newX;
                    oldY = newY;

                    mCanvasView.onTouchMove(deltaX, deltaY);

                    return true;
                }

                if (firstFingerID != INVALID_POINTER_ID)
                    pointerIndex = event.findPointerIndex(firstFingerID);
                else
                    pointerIndex = event.getActionIndex();

                newX = event.getX(pointerIndex);
                newY = event.getY(pointerIndex);

                deltaX = newX - oldX;
                deltaY = newY - oldY;

                oldX = newX;
                oldY = newY;

                /* The bitmap is moved based on the coordinates of the firstfinger */
                mCanvasView.onTouchMove(deltaX, deltaY);
                return true;
            }

            case MotionEvent.ACTION_UP: {
                firstFingerID = secondFingerID = INVALID_POINTER_ID;
                oldX = oldY = 0;

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
                    secondFingerID = INVALID_POINTER_ID;
                }
                else if (secondFingerID == pointerId) {
                    secondFingerID = INVALID_POINTER_ID;
                }

                int index = event.findPointerIndex(firstFingerID);
                oldX = event.getX(index);
                oldY = event.getY(index);

                return true;
            }

            /* Deprecated events are still being generated
             */
            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_3_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                secondFingerID = event.getPointerId(event.getActionIndex());

                oldX = calcCenterX(event);
                oldY = calcCenterY(event);
                return true;
            }

            default:
                return false;
        }
    }
}
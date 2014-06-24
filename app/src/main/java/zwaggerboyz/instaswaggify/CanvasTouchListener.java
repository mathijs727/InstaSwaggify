package zwaggerboyz.instaswaggify;

import android.util.Log;
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
                Log.i("Pevid", "down");
                mCanvasView.onTouchDown((int)event.getX(), (int)event.getY());
                return true;

            case MotionEvent.ACTION_MOVE: {
                //Log.i("Pevid", "move");
                mCanvasView.onTouchMove((int) event.getX(), (int) event.getY());

                return true;
            }

            case MotionEvent.ACTION_UP:
                Log.i("Pevid", "up");
                mCanvasView.onTouchUp((int)event.getX(), (int)event.getY());
                return true;

            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (currentPointer == event.getPointerId(event.getActionIndex())) {
                    Log.i("Pevid", "possible switch");
                    switchPrimaryPointer = true;
                }
                else {
                    switchPrimaryPointer = false;
                }

                if (event.getPointerCount() == 2) {
                    Log.i("Pevid", "pointer up");
                    mCanvasView.onPointerUp((int)event.getX(), (int)event.getY());
                }

                return true;

            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (switchPrimaryPointer && event.getPointerCount() == 2) {
                    int secondPointer = event.getPointerId(event.getActionIndex());
                    mCanvasView.switchPointer((int)event.getX(secondPointer), (int)event.getY(secondPointer));
                }
                Log.i("Pevid", "pointer down");
                //mCanvasView.onPointerUp((int)event.getX(), (int)event.getY());
                return true;

            default:
                Log.i("Pevid", "event " + event.getAction());
                return false;
        }
    }
}

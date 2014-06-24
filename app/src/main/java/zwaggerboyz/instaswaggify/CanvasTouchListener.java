package zwaggerboyz.instaswaggify;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Peter on 19-6-2014.
 */

public class CanvasTouchListener implements View.OnTouchListener {
    private CanvasView mCanvasView;
    private int currentPointer;
    private int mPointer1;
    private int mPointer2;
    private boolean switchPrimaryPointer;

    private static final int INVALID_POINTER_ID = -1;

    public CanvasTouchListener(CanvasView creator) {
        mCanvasView = creator;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mCanvasView.mScaleDetector.onTouchEvent(event);
        mCanvasView.mRotationGesture.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mPointer1 = event.getPointerId(0);
                Log.i("Pevid", "down");
                mCanvasView.onTouchDown((int)event.getX(), (int)event.getY());
                return true;

            case MotionEvent.ACTION_MOVE: {
                Log.i("Pevid", "move");
                int pointerIndex = event.getActionIndex();
                mCanvasView.onTouchMove((int) event.getX(pointerIndex), (int) event.getY(pointerIndex));

                return true;
            }

            case MotionEvent.ACTION_UP:
                Log.i("Pevid", "up");
                mCanvasView.onTouchUp((int)event.getX(), (int)event.getY());
                mPointer1 = mPointer2 = INVALID_POINTER_ID;
                return true;

            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_UP:
                int id = event.getPointerId(event.getActionIndex());
                if (mPointer1 == id) {
                    Log.i("Pevid", "possible switch");
                    switchPrimaryPointer = true;
                    mPointer1 = INVALID_POINTER_ID;
                }
                else if (mPointer2 == id) {
                    mPointer2 = INVALID_POINTER_ID;
                }
                else {
                    switchPrimaryPointer = false;
                }

                if (event.getPointerCount() == 2) {
                    Log.i("Pevid", "pointer up");
                    mCanvasView.onPointerUp((int)event.getX(), (int)event.getY());
                }

                return true;

            /* tijdelijke uitgeschakeld*/
            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (switchPrimaryPointer && event.getPointerCount() == 2 && false) {
                    int secondPointer = mPointer2 = event.getPointerId(event.getActionIndex());
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

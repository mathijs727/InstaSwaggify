package zwaggerboyz.instaswaggify;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Peter on 19-6-2014.
 */

public class CanvasTouchListener implements View.OnTouchListener {
    private CanvasView mCanvasView;
    private int firstFingerID = INVALID_POINTER_ID;
    private int secondFingerID = INVALID_POINTER_ID;
    private boolean switchFinger = false;

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
                firstFingerID = event.getPointerId(0);
                mCanvasView.onTouchDown((int)event.getX(), (int)event.getY());

                return true;

            case MotionEvent.ACTION_MOVE: {
                int pointerIndex;

                if (firstFingerID != INVALID_POINTER_ID)
                    pointerIndex = event.findPointerIndex(firstFingerID);
                else
                    pointerIndex = event.getActionIndex();
                mCanvasView.onTouchMove((int) event.getX(pointerIndex), (int) event.getY(pointerIndex));

                return true;
            }

            case MotionEvent.ACTION_UP:
                firstFingerID = secondFingerID = INVALID_POINTER_ID;
                return true;

            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_3_UP:
            case MotionEvent.ACTION_POINTER_UP:
                int pointerId = event.getPointerId(event.getActionIndex());
                if (firstFingerID == pointerId) {
                    firstFingerID  = secondFingerID;

                    if (secondFingerID != INVALID_POINTER_ID) {
                        int index = event.findPointerIndex(secondFingerID);

                        switchFinger = true;
                        mCanvasView.switchPointer((int)event.getX(index), (int)event.getY(index));
                    }
                    secondFingerID = INVALID_POINTER_ID;
                }
                else if (secondFingerID == pointerId) {
                    secondFingerID = INVALID_POINTER_ID;
                }

                return true;

            /* tijdelijke uitgeschakeld*/

            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_3_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() <= 2)
                    secondFingerID = event.getPointerId(event.getActionIndex());
                return true;

            default:
                return false;
        }
    }
}

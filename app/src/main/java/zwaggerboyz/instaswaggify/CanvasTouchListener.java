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
                return true;

            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_UP:
                Log.i("Pevid", "pointer up");

                return true;

            /* tijdelijke uitgeschakeld*/
            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i("Pevid", "pointer down");
                //mCanvasView.onPointerUp((int)event.getX(), (int)event.getY());
                return true;

            default:
                Log.i("Pevid", "event " + event.getAction());
                return false;
        }
    }
}

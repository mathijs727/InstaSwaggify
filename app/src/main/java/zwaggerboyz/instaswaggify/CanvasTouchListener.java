package zwaggerboyz.instaswaggify;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Peter on 19-6-2014.
 */

public class CanvasTouchListener implements View.OnTouchListener {
    private CanvasView mCanvasView;

    public CanvasTouchListener(CanvasView creator) {
        mCanvasView = creator;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mCanvasView.onTouchDown((int)event.getX(), (int)event.getY());
                return true;

            case MotionEvent.ACTION_MOVE:
                mCanvasView.onTouchMove((int)event.getX(), (int)event.getY());
                return true;

            case MotionEvent.ACTION_UP:
                mCanvasView.onTouchUp();
                return true;

            default:
                return false;
        }
    }
}

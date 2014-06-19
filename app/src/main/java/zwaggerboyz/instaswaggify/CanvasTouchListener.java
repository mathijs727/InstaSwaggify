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
                Log.i("DOWN", "DOWN");
                return true;

            case MotionEvent.ACTION_MOVE:
                Log.i("MOVE","MOVE");
                return true;

            case MotionEvent.ACTION_UP:
                Log.i("UP","UP");
                return true;

            default:
                return false;
        }
    }
}

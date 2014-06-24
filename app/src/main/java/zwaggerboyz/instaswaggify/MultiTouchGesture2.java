package zwaggerboyz.instaswaggify;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zeta on 6/23/14.
 */

public class MultiTouchGesture2 implements View.OnTouchListener {
    private int mPointer1 = INVALID_POINTER_ID;
    private int mPointer2 = INVALID_POINTER_ID;
    float first_x, first_y;
    float oldX, oldY;
    double deltaAngle;

    private static final int INVALID_POINTER_ID = -1;

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        Log.v("MultiTouch", "OnTouch");
        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mPointer1 = ev.getPointerId(0);
                first_x = ev.getX();
                first_y = ev.getY();

                return true;
            }

            case MotionEvent.ACTION_POINTER_2_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int pointerIndex = ev.getActionIndex();
                final int pointerId = ev.findPointerIndex(pointerIndex);

                if (mPointer2 == INVALID_POINTER_ID){
                    mPointer2 = pointerId;

                    float temp_x = ev.getX(pointerIndex);

                    oldX = first_x - temp_x;
                    oldY = first_y - ev.getY(pointerIndex);

                    if (temp_x > first_x) {
                        oldX *= -1;
                        oldY *= -1;
                    }
                }
                else if (mPointer1 == INVALID_POINTER_ID) {
                    mPointer1 = pointerId;

                    float temp_x = ev.getX(pointerIndex);

                    oldX = first_x - temp_x;
                    oldY = first_y - ev.getY(pointerIndex);

                    if (temp_x > first_x) {
                        oldX *= -1;
                        oldY *= -1;
                    }
                }

                return true;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex = ev.getActionIndex();
                final int id = ev.findPointerIndex(pointerIndex);

                if (ev.getPointerCount() >= 2 && id == mPointer1 || id == mPointer2) {
                    Log.i("MultiTouch", "Moving with two fingers");

                    final float newX = ev.getX(pointerIndex);
                    final float newY = ev.getY(pointerIndex);

                    if (oldY > newY)
                        deltaAngle = -1;
                    else
                        deltaAngle = 1;


                    double num = oldX * newX + oldY * newY;
                    double dem = Math.sqrt(oldX * oldX + oldY * oldY) * Math.sqrt(newX * newX + newY * newY);
                    double ratio = Math.min(1, Math.max(-1, num / dem));

                    Log.i("Pevid", "num " + num + " dem " + dem);
                    Log.i("Pevid", "num / dem " + num / dem);
                    Log.i("Pevid", "acos " + Math.acos(num / dem));

                    deltaAngle *= Math.acos(ratio);

                    Log.i("MultiTouch", "deltaAngle: " + deltaAngle);

                    this.oldX = newX;
                    this.oldY = newY;

                }

                return true;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mPointer1 = INVALID_POINTER_ID;
                mPointer2 = INVALID_POINTER_ID;

                return true;
            }

            case MotionEvent.ACTION_POINTER_2_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                // Extract the index of the pointer that left the touch sensor
                final int pointerIndex = ev.getActionIndex();
                final int id = ev.getPointerId(pointerIndex);

                if (id == mPointer1) {
                    mPointer1 = INVALID_POINTER_ID;
                }
                else if (id == mPointer2) {
                    mPointer2 = INVALID_POINTER_ID;
                }

                return true;
            }
        }

        return true;
    }
}

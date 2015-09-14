package io.imoji.photointegration;

import android.view.MotionEvent;

/**
 * Created by sajjadtabib on 9/3/15.
 */
public class SimpleMoveGestureDetector {


    private OnMoveGestureListener mOnMoveGestureListener;

    public SimpleMoveGestureDetector(OnMoveGestureListener onMoveGestureListener) {
        mOnMoveGestureListener = onMoveGestureListener;
    }

    private int mTargetPointerId;

    public boolean onTouchEvent(MotionEvent event) {

        boolean handled = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTargetPointerId = event.getPointerId(event.getActionIndex());
                handled = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerId(event.getActionIndex()) == mTargetPointerId) {
                    float rawX = event.getRawX();
                    float rawY = event.getRawY();
                    float x = event.getX();
                    float y = event.getY();
                    mOnMoveGestureListener.onMove(rawX, rawY, x, y);
                    handled = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTargetPointerId = -1;
                mOnMoveGestureListener.onMoveCancelled();
                break;

        }

        return handled;
    }

    public interface OnMoveGestureListener {
        void onMove(float rawX, float rawY, float x, float y);

        void onMoveCancelled();
    }

}

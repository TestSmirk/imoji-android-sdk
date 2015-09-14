package io.imoji.photointegration;

import android.util.Log;
import android.view.MotionEvent;

//Source: http://stackoverflow.com/a/18276033/361902
public class RotationGestureDetector {
    private static final int INVALID_POINTER_ID = -1;
    private static final String LOG_TAG = RotationGestureDetector.class.getSimpleName();
    private float fX, fY, sX, sY;
    private int ptrID1, ptrID2;
    private float mAngle;
    private boolean mIsRotating;
    private OnRotationGestureListener mListener;

    public RotationGestureDetector(OnRotationGestureListener listener) {
        mListener = listener;
        ptrID1 = INVALID_POINTER_ID;
        ptrID2 = INVALID_POINTER_ID;
    }

    public float getAngle() {
        return mAngle;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                ptrID1 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_OUTSIDE:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                ptrID2 = event.getPointerId(event.getActionIndex());
                sX = event.getX(event.findPointerIndex(ptrID1));
                sY = event.getY(event.findPointerIndex(ptrID1));
                fX = event.getX(event.findPointerIndex(ptrID2));
                fY = event.getY(event.findPointerIndex(ptrID2));
                break;
            case MotionEvent.ACTION_MOVE:
                float nfX, nfY, nsX, nsY;
                if (event.getPointerCount() > 1 && ptrID2 == INVALID_POINTER_ID && event.getHistorySize() > 0) {
                    //lets see if we can get historical data

                    mIsRotating = true;

                    nsX = event.getHistoricalX(0, 0);
                    nsY = event.getHistoricalY(0, 0);
                    nfX = event.getHistoricalX(1, 0);
                    nfY = event.getHistoricalY(1, 0);

                    mAngle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY);

                    if (mListener != null) {
                        mListener.OnRotation(this);
                    }


                } else if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
                    mIsRotating = true;

                    nsX = event.getX(event.findPointerIndex(ptrID1));
                    nsY = event.getY(event.findPointerIndex(ptrID1));
                    nfX = event.getX(event.findPointerIndex(ptrID2));
                    nfY = event.getY(event.findPointerIndex(ptrID2));

                    mAngle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY);

                    if (mListener != null) {
                        mListener.OnRotation(this);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                ptrID1 = INVALID_POINTER_ID;
                mIsRotating = false;
                mListener.onRotateCancelled();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                ptrID2 = INVALID_POINTER_ID;
                mIsRotating = false;
                mListener.onRotateCancelled();
                break;
        }

        return true;
    }

    void printSamples(MotionEvent ev) {
        final int historySize = ev.getHistorySize();
        final int pointerCount = ev.getPointerCount();
        for (int h = 0; h < historySize; h++) {
            Log.d("pr", "At time " + ev.getHistoricalEventTime(h));
            for (int p = 0; p < pointerCount; p++) {
                Log.d("pr", "  pointer " + ev.getPointerId(p) + " : " + "(" +
                        ev.getHistoricalX(p, h) + ", " + ev.getHistoricalY(p, h) + ")");
            }
        }
        System.out.printf("At time %d:", ev.getEventTime());
        for (int p = 0; p < pointerCount; p++) {
            System.out.printf("  pointer %d: (%f,%f)",
                    ev.getPointerId(p), ev.getX(p), ev.getY(p));
        }
    }

    public boolean isRotating() {
        return mIsRotating;
    }

    private float angleBetweenLines(float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
        float angle1 = (float) Math.atan2((fY - sY), (fX - sX));
        float angle2 = (float) Math.atan2((nfY - nsY), (nfX - nsX));

        float angle = ((float) Math.toDegrees(angle1 - angle2)) % 360;
        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        return angle;
    }

    public interface OnRotationGestureListener {
        void OnRotation(RotationGestureDetector rotationDetector);

        void onRotateCancelled();

    }
}

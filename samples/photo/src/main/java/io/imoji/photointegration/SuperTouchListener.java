package io.imoji.photointegration;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by sajjadtabib on 9/3/15.
 */
public class SuperTouchListener implements View.OnTouchListener, RotationGestureDetector.OnRotationGestureListener, ScaleGestureDetector.OnScaleGestureListener, SimpleMoveGestureDetector.OnMoveGestureListener {

    private final Context mContext;
    private RotationGestureDetector mRotationGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private SimpleMoveGestureDetector mSimpleMoveGestureDetector;
    private ImageView mTarget;
    private TouchDelegate mTouchDelegate;

    public SuperTouchListener(Context context, final ImageView mTarget) {
        mContext = context;
        this.mTarget = mTarget;
        mRotationGestureDetector = new RotationGestureDetector(this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mSimpleMoveGestureDetector = new SimpleMoveGestureDetector(this);
    }

    @Override

    public boolean onTouch(View v, MotionEvent event) {
        if (mTouchDelegate == null || ((ViewGroup)mTarget.getParent()).getTouchDelegate() != mTouchDelegate) {
            Rect touchRect = new Rect();
            mTarget.getHitRect(touchRect);
            touchRect.top -= 2000;
            touchRect.left -= 2000;
            touchRect.bottom += 2000;
            touchRect.right += 2000;
            mTouchDelegate = new TouchDelegate(touchRect, mTarget);
            ((ViewGroup) mTarget.getParent()).setTouchDelegate(mTouchDelegate);

        }

        mScaleGestureDetector.onTouchEvent(event);
        mRotationGestureDetector.onTouchEvent(event);
        mSimpleMoveGestureDetector.onTouchEvent(event);

        return true;
    }

    @Override
    public void OnRotation(RotationGestureDetector rotationDetector) {
        float angle = rotationDetector.getAngle();
        if (rotationDetector.isRotating()) {
            if (mTarget.getTag(R.id.super_touch_previous_rotate) != null) {
                float previousAngle = (float) mTarget.getTag(R.id.super_touch_previous_rotate);
                mTarget.getImageMatrix().postRotate(previousAngle - angle, mTarget.getWidth() / 2, mTarget.getHeight() / 2);

            }

            mTarget.setTag(R.id.super_touch_previous_rotate, angle);
            mTarget.invalidate();
        }
    }

    @Override
    public void onRotateCancelled() {
        mTarget.setTag(R.id.super_touch_previous_rotate, null);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scaleFactor = detector.getScaleFactor();
        if (mTarget.getTag(R.id.super_touch_previous_scale_x) != null && mTarget.getTag(R.id.super_touch_previous_scale_y) != null) {
            mTarget.getImageMatrix().postScale(scaleFactor, scaleFactor, mTarget.getWidth() / 2, mTarget.getHeight() / 2);

        }

        mTarget.setTag(R.id.super_touch_previous_scale_x, scaleFactor);
        mTarget.setTag(R.id.super_touch_previous_scale_y, scaleFactor);
        mTarget.invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        mTarget.setTag(R.id.super_touch_previous_scale_x, null);
        mTarget.setTag(R.id.super_touch_previous_scale_y, null);
    }

    @Override
    public void onMove(float rawX, float rawY, float x, float y) {
        if (!mRotationGestureDetector.isRotating() && !mScaleGestureDetector.isInProgress()) {

            if (mTarget.getTag(R.id.super_touch_previous_x) != null && mTarget.getTag(R.id.super_touch_previous_y) != null) {
                float prevX = (float) mTarget.getTag(R.id.super_touch_previous_x);
                float prevY = (float) mTarget.getTag(R.id.super_touch_previous_y);
                float dx = rawX - prevX;
                float dy = rawY - prevY;
                mTarget.setX(mTarget.getX() + dx);
                mTarget.setY(mTarget.getY() + dy);

            }

            mTarget.setTag(R.id.super_touch_previous_x, rawX);
            mTarget.setTag(R.id.super_touch_previous_y, rawY);
            mTarget.invalidate();

        }
    }

    @Override
    public void onMoveCancelled() {
        mTarget.setTag(R.id.super_touch_previous_x, null);
        mTarget.setTag(R.id.super_touch_previous_y, null);
    }

}

package com.study.ratotion;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/4/26 0026.
 */
public class ElementContainerView extends ViewGroup {

    private PointF mBasePoint = new PointF();
    private PointF mCurrentPt = new PointF();
    private PointF mLastPoint = new PointF();
    private ElementView mCapturedView;
    private Matrix mInvertMatrix = new Matrix();

    public ElementContainerView(Context context) {
        super(context);
    }

    public ElementContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

/*    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        MotionEventHandler.HandleMotionEvent(event,ElementContainerView.class.getSimpleName(),"onInterceptTouchEvent");
        return true;
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MotionEventHandler.HandleMotionEvent(event,ElementContainerView.class.getSimpleName(),"onTouchEvent");
        this.handlerMotionEvent(event);
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //MotionEventHandler.HandleMotionEvent(event,ElementContainerView.class.getSimpleName(),"dispatchTouchEvent");
        //this.handlerMotionEvent(event);
        return super.dispatchTouchEvent(event);
    }

    private void handlerMotionEvent(MotionEvent event){
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.mCurrentPt.x = x;
                this.mCurrentPt.y = y;
                this.mLastPoint.x = x;
                this.mLastPoint.y = y;

                this.findCapturedElementView(x, y);
                this.caculateCenterPointer();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentPt.x = x;
                mCurrentPt.y = y;
                this.doRotation();
                this.doScale();
                this.doTranslate(mCurrentPt.x - mLastPoint.x, mCurrentPt.y - mLastPoint.y);
                mLastPoint.x = x;
                mLastPoint.y = y;
                break;
            case MotionEvent.ACTION_UP:
                doRemove();
                break;
        }
    }
    public ElementContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < this.getChildCount(); i++) {
            ElementView elementView = (ElementView) this.getChildAt(i);
            int left = this.getWidth() / 2 - elementView.getCenterX();
            int top = this.getHeight() / 2 - elementView.getCenterY();
            int right = this.getWidth() / 2 + elementView.getCenterX();
            int bot = this.getHeight() / 2 + elementView.getCenterY();
            elementView.layout(left, top, right, bot);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.dispatchDraw(canvas);
    }

    private void doTranslate(float dx, float dy) {
        if (this.mCapturedView != null && this.mCapturedView.isTranslateEnable()) {
            this.mCapturedView.setTranslationX(dx);
            this.mCapturedView.setTranslationY(dy);
        }
    }

    private void doRotation() {
        if (this.mCapturedView != null && this.mCapturedView.isRotateEnable()) {
            float olddx = mLastPoint.x - mBasePoint.x;
            float olddy = mLastPoint.y - mBasePoint.y;
            float newdx = mCurrentPt.x - mBasePoint.x;
            float newdy = mCurrentPt.y - mBasePoint.y;
            float oldd2 = olddx * olddx + olddy * olddy;
            float newd2 = newdx * newdx + newdy * newdy;
            float dot = (float) ((newdy * olddx - newdx * olddy) / Math.sqrt(oldd2 * newd2));
            float rad = (float) Math.asin(dot);
            float deg = (float) Math.toDegrees(rad);
            this.mCapturedView.setRotation(deg);
        }
    }

    private void doScale(){
        if (this.mCapturedView != null && this.mCapturedView.isRotateEnable()) {
            float olddx = mLastPoint.x - mBasePoint.x;
            float olddy = mLastPoint.y - mBasePoint.y;
            float newdx = mCurrentPt.x - mBasePoint.x;
            float newdy = mCurrentPt.y - mBasePoint.y;
            float oldd2 = olddx * olddx + olddy * olddy;
            float newd2 = newdx * newdx + newdy * newdy;
            float scale = (float)Math.sqrt(newd2/oldd2);
            this.mCapturedView.setScale(scale);
        }
    }
    private void doRemove() {
        if (this.mCapturedView != null && this.mCapturedView.isRemoveEnable()) {
            Toast.makeText(this.getContext(), "I Love U,Please don't delete me.", Toast.LENGTH_SHORT).show();
        }
    }

    private void caculateCenterPointer() {
        if (mCapturedView != null) {
            float mapPoints[] = new float[]{mCapturedView.getCenterX(), mCapturedView.getCenterY()};
            //Matrix matrix = mCapturedView.getMatrix();
            //matrix.mapPoints(mapPoints);
            this.mBasePoint.x = mapPoints[0] + mCapturedView.getLeft();
            this.mBasePoint.y = mapPoints[1] + mCapturedView.getTop();
        }
    }

    private void findCapturedElementView(float x, float y) {

        ElementView element = (ElementView) this.getChildAt(this.getChildCount()-1);
        if(isElementCaptured(element,x,y))return;
        for (int i = 0; i < this.getChildCount(); i++) {
            element = (ElementView) this.getChildAt(i);
            if (isElementCaptured(element,x,y)) {
                this.bringElementToFront(mCapturedView);
                return;
            }
        }
        mCapturedView = null;
    }

    private boolean isElementCaptured(ElementView element,float x,float y){
        float mapPoints[] = new float[]{x - element.getLeft(), y - element.getTop()};
        Matrix matrix = element.getMatrix();
        matrix.invert(mInvertMatrix);
        mInvertMatrix.mapPoints(mapPoints);
        boolean contains = element.contains(mapPoints[0], mapPoints[1]);
        if(contains){ mCapturedView = element;}
        return contains;
    }
    public void bringElementToFront(ElementView elementView) {
        this.removeView(elementView);
        this.addView(elementView);
    }
}

package com.study.ratotion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2015/4/26 0026.
 */
public class ElementView extends View{

    private int mHalfWidth;
    private int mHalfLineW;
    private Bitmap mRemoveButton;
    private Bitmap mRotationButton;
    private BitmapShader mBitmapShaderH,mBitmapShaderV;
    private Bitmap mImage;
    private float mDegre;
    private float mScale = 1.f;
    private boolean mRemoveEnable;
    private boolean mRotateEnable;
    private boolean mTranslateEnable;
    private float mTranslateX,mTranslateY;
    public ElementView(Context context) {
        super(context);
        this.init();
    }

    public ElementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ElementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init(){
        this.mRemoveButton = BitmapFactory.decodeResource(getResources(),R.drawable.delete);
        this.mRotationButton = BitmapFactory.decodeResource(getResources(),R.drawable.rotate);
        this.mHalfWidth = this.mRemoveButton.getWidth()/2;
        this.mImage = BitmapFactory.decodeResource(getResources(),R.drawable.icon_anna);
        Bitmap mLineH = BitmapFactory.decodeResource(getResources(),R.drawable.icon_border_h);
        Bitmap mLineV = BitmapFactory.decodeResource(getResources(),R.drawable.icon_border_v);
        mBitmapShaderH = new BitmapShader(mLineH, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mBitmapShaderV = new BitmapShader(mLineV, Shader.TileMode.CLAMP, Shader.TileMode.REPEAT);
        mHalfLineW = 1;
    }
    public void setBitmapSource(int res){
        this.mImage = BitmapFactory.decodeResource(getResources(),res);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(mImage,mHalfWidth,mHalfWidth,paint);
        paint.setShader(mBitmapShaderH);
        canvas.drawRect(new RectF(mHalfWidth-1,mHalfWidth-1,this.getWidth()-mHalfWidth+1,mHalfWidth+1),paint);
        canvas.drawRect(new RectF(mHalfWidth-1,this.getHeight()-mHalfWidth-1,this.getWidth()-mHalfWidth+1,this.getHeight()-mHalfWidth+mHalfLineW),paint);
        paint.setShader(mBitmapShaderV);
        canvas.drawRect(new RectF(mHalfWidth-1,mHalfWidth-1,this.mHalfWidth+1,this.getHeight()-mHalfWidth+1),paint);
        canvas.drawRect(new RectF(this.getWidth()-this.mHalfLineW-mHalfWidth,mHalfWidth-1,this.getWidth()-mHalfWidth+1,this.getHeight()+mHalfLineW-mHalfWidth),paint);
        //if(this.mRemoveEnable)
        canvas.drawBitmap(mRemoveButton,0,0,paint);
        //if(this.mRotateEnable)
        canvas.drawBitmap(mRotationButton,this.getWidth()-2*mHalfLineW-2*mHalfWidth,this.getHeight()-2*mHalfWidth-2*mHalfLineW,paint);
    }

    public int getCenterX(){
        return this.mImage.getWidth()/2+this.mHalfWidth;
    }
    public int getCenterY(){
        return this.mImage.getHeight()/2+this.mHalfWidth;
    }

    public boolean isRotateEnable() {
        return mRotateEnable;
    }

    public boolean isTranslateEnable() {
        return mTranslateEnable;
    }

    public boolean isRemoveEnable() {
        return mRemoveEnable;
    }

    public boolean contains(float x,float y){
        if(x<=0 || x>=this.getWidth() || y<=0 || y>=this.getHeight()){
            this.mRemoveEnable=false;
            this.mRotateEnable = false;
            this.mTranslateEnable = false;
            return false;
        }else{
            if(x>=0 && x <=2*this.mHalfWidth && y>=0 && y<=2*this.mHalfWidth){
                this.mRemoveEnable = true;
                this.mRotateEnable = false;
                this.mTranslateEnable = false;
                return true;
            }else if(x>=this.getWidth()-2*this.mHalfWidth && x<=this.getWidth() && y>=this.getHeight()-2*this.mHalfWidth && y<=this.getHeight()){
                this.mRotateEnable = true;
                this.mRemoveEnable=false;
                this.mTranslateEnable = false;
            }else{
                this.mTranslateEnable = true;
                this.mRemoveEnable=false;
                this.mRotateEnable = false;
            }
        }
        return true;
    }

    public void setScale(float scaleFactor){
        mScale = mScale * scaleFactor;
        mScale = mScale <=0.5f ? 0.5f: mScale;
        mScale = mScale >=1.5f ? 1.5f : mScale;
        Log.d("debug","setScale ->"+mScale);
        super.setScaleY(mScale);
        super.setScaleX(mScale);
    }
    @Override
    public void setRotation(float rotation) {
        this.mDegre+=rotation;
        super.setRotation(mDegre);
    }

    @Override
    public void setTranslationX(float translationX) {
        mTranslateX+=translationX;
        super.setTranslationX(mTranslateX);
    }

    @Override
    public void setTranslationY(float translationY) {
        this.mTranslateY +=translationY;
        super.setTranslationY(mTranslateY);
    }

/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MotionEventHandler.HandleMotionEvent(event,ElementView.class.getSimpleName(),"onTouchEvent");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        MotionEventHandler.HandleMotionEvent(event,ElementView.class.getSimpleName(),"dispatchTouchEvent");
        return super.dispatchTouchEvent(event);
    }*/

    public void setRemoveEnable(boolean mRemoveEnable) {
        this.mRemoveEnable = mRemoveEnable;
    }

    public void setRotateEnable(boolean mRotateEnable) {
        this.mRotateEnable = mRotateEnable;
    }

    public void setTranslateEnable(boolean mTranslateEnable) {
        this.mTranslateEnable = mTranslateEnable;
    }
}

package com.sage.hedonicmentality.view;

/**
 * Created by Administrator on 2015/10/29.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;


@SuppressLint("ClickableViewAccessibility")
public class TuneWheelGF extends View {

    public interface OnValueChangeListener {
        public void onValueChange(float value);
    }

    public static final int MOD_TYPE_HALF = 2;
    public static final int MOD_TYPE_ONE = 1;

    private static final int TEXT_SIZE = 10;

    private float mDensity;
    private int minValue=5;
    private int mMaxValue = 30, mModType = 1, mLineDivider = 9;
    public  int mValue = 15;

    private int mLastX, mMove;
    private int mWidth, mHeight;

    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    float xPosition , textWidth;
    private OnValueChangeListener mListener;

    @SuppressWarnings("deprecation")
    public TuneWheelGF(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new Scroller(getContext());
        mDensity = getContext().getResources().getDisplayMetrics().density;
        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
    }

    /**
     * 设置用于接收结果的监听器
     *
     * @param listener
     */
    public void setValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    /**
     * 获取当前刻度值
     *
     * @return
     */
    public float getValue() {
        return mValue;
    }
    /**
     * 设置当前刻度值
     *
     * @return
     */
    public void setValue(int Value) {
        this.mValue = Value;
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawScaleLine(canvas);
    }

    /**
     * 从中间往两边开始画
     *
     * @param canvas
     */
    private void drawScaleLine(Canvas canvas) {
        canvas.save();


        Paint linePaints = new Paint();
        linePaints.setStrokeWidth(2);

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(TEXT_SIZE * mDensity);
        textPaint.setColor(Color.BLACK);

        TextPaint centerText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        centerText.setTextSize(15 * mDensity);
        centerText.setColor(Color.WHITE);


        int width = mWidth, drawCount = 0;
         xPosition = 0;
         textWidth = Layout.getDesiredWidth("0", textPaint);
        for (int i = 0; drawCount <= 4 * width; i++) {
            int numSize = String.valueOf(mValue + i).length();
            xPosition = (width / 2 - mMove) + i * mLineDivider * mDensity;
            if (xPosition + getPaddingRight() < mWidth) {
                    if ((mValue + i) % 5 == 0) {
                        if (mValue + i>mMaxValue||mValue + i<minValue) {

                        }else {
                            if ((mValue + i)!=mValue) {
                                if(mValue + i == 25){
                                    canvas.drawText(String.valueOf((30)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, textPaint);
                                }else if(mValue + i == 30){
                                    canvas.drawText(String.valueOf((60)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, textPaint);
                                }else {
                                    canvas.drawText(String.valueOf((mValue + i)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, textPaint);
                                }
                            }else{
                                if(mValue + i == 25){
                                    canvas.drawText(String.valueOf((30)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, centerText);
                                }else if(mValue + i == 30){
                                    canvas.drawText(String.valueOf((60)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, centerText);
                                }else {
                                    canvas.drawText(String.valueOf((mValue + i)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, centerText);
                                }
                            }
                            Log.e("LOG", "mValue:" + mValue + " I:" + i + " mValue + i:" + (mValue + i));
                        }
                }
            }

            xPosition = (width / 2 - mMove) - i * mLineDivider * mDensity;
        if (xPosition > getPaddingLeft()) {
                if ((mValue - i) % 5 == 0) {
                    if (mValue - i>mMaxValue||mValue - i<minValue) {

                    }else {
                        if ((mValue - i)!=mValue) {
                            if(mValue - i == 25){
                                canvas.drawText(String.valueOf((30)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, textPaint);
                            }else if(mValue - i == 30){
                                canvas.drawText(String.valueOf((60)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, textPaint);
                            }else {
                                canvas.drawText(String.valueOf((mValue - i)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, textPaint);
                            }
                        }else{
                            if(mValue - i == 25){
                                canvas.drawText(String.valueOf((30)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, centerText);
                            }else if(mValue - i == 30){
                                canvas.drawText(String.valueOf((60)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, centerText);
                            }else {
                                canvas.drawText(String.valueOf((mValue - i)), xPosition - (textWidth * numSize / 2), getHeight() - textWidth, centerText);
                            }

                        }
                        Log.e("LOG", "mValue:" + mValue + " I:" + i + " mValue - i:" + (mValue - i));
                    }
                }
            }

            drawCount += 2 * mLineDivider * mDensity;

        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int xPosition = (int) event.getX();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mScroller.forceFinished(true);

                mLastX = xPosition;
                mMove = 0;

                break;
            case MotionEvent.ACTION_MOVE:
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker(event);
                return false;
            // break;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private void countVelocityTracker(MotionEvent event) {
        mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = mVelocityTracker.getXVelocity();
//        Log.e("Math.abs(xVelocity):",Math.abs(xVelocity)+""+" mMinVelocity:"+mMinVelocity);
        if (Math.abs(xVelocity) > mMinVelocity) {

            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);

        }
    }

    private void changeMoveAndValue() {
        int tValue = (int) (mMove / (mLineDivider * mDensity));
            if (Math.abs(tValue) > 0) {
                mValue += tValue;
                mMove -= tValue * mLineDivider * mDensity;
                if (mValue <= 0 || mValue > mMaxValue) {
                    mValue = mValue <= 0 ? minValue : mMaxValue;
                    mMove = 0;
                    mScroller.forceFinished(true);
                }
                notifyValueChange();
            }
            postInvalidate();
    }

    private void countMoveEnd() {
        int roundMove = Math.round(mMove/ (mLineDivider * mDensity));
        int  mv = mValue;
        if(mv%5<=2.5){
            mValue = mValue-(mv%5);
        }else
        if(mv%5>2.5){
            mValue = mValue-(mv%5)+5;
        }
//        mValue = mValue +5;
        mValue = mValue <= 0 ? minValue : mValue;
        mValue = mValue > mMaxValue ? mMaxValue : mValue;
        mLastX = 0;
        mMove = 0;

        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            if (mModType == MOD_TYPE_ONE) {
                mListener.onValueChange(mValue);
            }
            if (mModType == MOD_TYPE_HALF) {
                mListener.onValueChange(mValue / 2f);
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() == mScroller.getFinalX()) { // over
                countMoveEnd();
            } else {
                int xPosition = mScroller.getCurrX();
                mMove += (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }
}

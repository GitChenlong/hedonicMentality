package com.sage.hedonicmentality.view;

/**
 * Created by Administrator on 2016/5/27.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;


/**
 * Created by Administrator on 2015/10/29.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

import com.lidroid.xutils.util.LogUtils;


/**
 * 卷尺控件类。<br>
 * 细节问题包括滑动过程中widget边缘的刻度显示问题等<br>
 *
 *
 * @author ttdevs
 */
@SuppressLint("ClickableViewAccessibility")
public class DateView extends View {


    public interface OnValueChangeListener {
        public void onValueChange(float value);
    }

    public static final int MOD_TYPE_HALF = 2;
    public static final int MOD_TYPE_ONE = 100;

    private static final int TEXT_SIZE = 18;
    private static final int ITEM_MAX_HEIGHT = 50;
    private static final int ITEM_MIN_HEIGHT = 20;
    private float mDensity;
    private int minValue = 0;
    private static int  mMaxValue = 10, mModType = 1, mLineDivider = 30;
    public static int mValue = 5;
    private int mLastX, mMove;
    private int mLastY, mMoveY;
    private int mWidth, mHeight;

    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    private OnValueChangeListener mListener;
    private static String dates[]={} ;

    @SuppressWarnings("deprecation")
    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(getContext());
        mDensity = getContext().getResources().getDisplayMetrics().density;
        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();//触发移动时间的最短距离
//         setBackgroundResource(R.mipmap.ic_launcher);
//        setBackgroundDrawable(createBackground());
    }
    public static void setDates(String[] date) {
        dates = date;
        mMaxValue=dates.length;
    }
    private GradientDrawable createBackground() {
        float strokeWidth = 4 * mDensity; // 边框宽度
        float roundRadius = 6 * mDensity; // 圆角半径
        int strokeColor = Color.parseColor("#ffffff");// 边框颜色
        // int fillColor = Color.parseColor("#DFDFE0");// 内部填充颜色

        setPadding((int)strokeWidth, (int)strokeWidth, (int)strokeWidth, 0);

        int colors[] = { 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF };// 分别为开始颜色，中间夜色，结束颜色
        GradientDrawable bgDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);// 创建drawable
        // bgDrawable.setColor(fillColor);
        bgDrawable.setCornerRadius(roundRadius);
        bgDrawable.setStroke((int)strokeWidth, strokeColor);
        // setBackgroundDrawable(gd);
        return bgDrawable;
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
    public int getValue() {
        return mValue;
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
        if (dates==null) {
            return;
        }
        drawScaleLine(canvas);
        getValue();
//         drawWheel(canvas);
//        drawMiddleLine(canvas);
    }

    /*private void drawWheel(Canvas canvas) {
        Drawable wheel = getResources().getDrawable(R.mipmap.a1);
        wheel.setBounds(0, 0, getWidth(), getHeight());
        wheel.draw(canvas);
    }*/

    /**
     * 从中间往两边开始画刻度线
     *
     * @param canvas
     */
    private void drawScaleLine(Canvas canvas) {
        canvas.save();
        RectF rect = new RectF();
        Paint mPaint = new Paint();

        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.BLACK);
        Paint linePaints = new Paint();
        linePaints.setStrokeWidth(2);

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(TEXT_SIZE * mDensity);

        TextPaint centerText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        centerText.setTextSize(15 * mDensity);
        centerText.setColor(Color.GREEN);


        int width = mWidth, drawCount = 0;
        int height = mHeight;
        float xPosition = 0, textWidth = Layout.getDesiredWidth("0", textPaint);
        float yPosition = 0;

        for (int i = 0; drawCount <= 4 * width; i++) {

            int numSize = String.valueOf(mValue + i).length();
//
            yPosition = (height / 2 - mMoveY) + i * mLineDivider * mDensity;
//
            if (yPosition + getPaddingBottom()< mHeight) {
//
                if ((mValue + i) % 1 == 0) {
                    if (mValue + i >= mMaxValue || mValue + i < minValue) {
//
                    } else {
                        if (mValue + i != mValue) {
                            canvas.drawText(String.valueOf(dates[mValue + i]), getWidth() / 2, yPosition - (textWidth * numSize / 2) + 30, textPaint);
                        } else {
                            canvas.drawText(String.valueOf(dates[mValue + i]), getWidth() / 2, yPosition - (textWidth * numSize / 2) + 30, centerText);
                        }
                    }
                }
            }
            yPosition = (height / 2 - mMoveY) - i * mLineDivider * mDensity;
            if (yPosition > getPaddingTop()) {
                if ((mValue - i) % 1 == 0) {
                    if (mValue - i>=mMaxValue||mValue - i<minValue) {
//
                    }else {
                        if ((mValue - i) != mValue) {
                            canvas.drawText(String.valueOf(dates[mValue - i]), getWidth() / 2, yPosition - (textWidth * numSize / 2) + 30, textPaint);
                        } else {
                            canvas.drawText(String.valueOf(dates[mValue - i]), getWidth() / 2, yPosition - (textWidth * numSize / 2) + 30, centerText);
//
                        }
                    }
                }
            }
//
            drawCount += 2 * mLineDivider * mDensity;
        }
        canvas.restore();
    }

    /**
     * 计算没有数字显示位置的辅助方法
     *
     * @param value
     * @param yPosition
     * @param textWidth
     * @return
     */
    private float countLeftStart(int value, float yPosition, float textWidth) {
//        float xp = 0f;
        float yp = 0f;

        if (value < 20) {
            yp = yPosition - (textWidth * 1 / 2);
        } else {
            yp = yPosition - (textWidth * 2 / 2);
        }
        return yp;
    }

    /**
     * 画中间的红色指示线、阴影等。指示线两端简单的用了两个矩形代替
     *
     * @param canvas
     */
    private void drawMiddleLine(Canvas canvas) {
        // TOOD 常量太多，暂时放这，最终会放在类的开始，放远了怕很快忘记
        int gap = 12, indexWidth = 2, indexTitleWidth = 10, indexTitleHight =2, shadow = 2;
//        int gap = 12, indexWidth = 8, indexTitleWidth = 24, indexTitleHight = 10, shadow = 6;
        String color = "#66999999";

        canvas.save();


        //中间固定红线两头
        Paint ovalPaint = new Paint();
        ovalPaint.setColor(Color.RED);
        ovalPaint.setStrokeWidth(indexTitleWidth);
//       canvas.drawLine(mWidth / 2, 0, mWidth / 2, indexTitleHight, ovalPaint);
//        canvas.drawLine(mWidth / 2, mHeight - indexTitleHight, mWidth / 2, mHeight, ovalPaint);

//         RectF ovalRectF = new RectF(mWidth / 2 - 10, 0, mWidth / 2 + 10, 4 *
//         mDensity); //TODO 椭圆
        RectF ovalRectF = new RectF(mWidth / 2 - 10, mHeight - 8 * mDensity, mWidth / 2 +
                10, mHeight/2); //TODO
        canvas.drawOval(ovalRectF, ovalPaint);

//        Paint shadowPaint = new Paint();
//        shadowPaint.setStrokeWidth(shadow);
//        shadowPaint.setColor(Color.parseColor(color));
//        canvas.drawLine(mWidth / 2 + gap, 0, mWidth / 2 + gap, mHeight, shadowPaint);

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
//        int xPosition = (int) event.getX();
        int yPosition = (int) event.getY();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mScroller.forceFinished(true);

                mLastY = yPosition;

                mLastY=0;
                mMoveY = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveY += (mLastY -yPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker(event);
                return false;
            default:
                break;
        }

        mLastY = yPosition;
        return true;
    }

    private void countVelocityTracker(MotionEvent event) {
        mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = mVelocityTracker.getXVelocity();
        float yVelocity = mVelocityTracker.getYVelocity();
        /**
         * public void fling (int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY)
         　　在fling（译者注：快滑，用户按下触摸屏、快速移动后松开）手势基础上开始滚动。滚动的距离取决于fling的初速度。
         　　参数
         　　startX 滚动起始点X坐标
         　　startY 滚动起始点Y坐标
         　　velocityX   当滑动屏幕时X方向初速度，以每秒像素数计算
         　　velocityY   当滑动屏幕时Y方向初速度，以每秒像素数计算
         　　minX    X方向的最小值，scroller不会滚过此点。
         　　maxX    X方向的最大值，scroller不会滚过此点。
         　　minY    Y方向的最小值，scroller不会滚过此点。
         　　maxY    Y方向的最大值，scroller不会滚过此点。*/
        if (Math.abs(yVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, 0, (int) yVelocity,0, 0,  Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
//        if (Math.abs(xVelocity) > mMinVelocity) {
//            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
//        }
    }

    private void changeMoveAndValue() {
        int tValue = (int) (mMove / (mLineDivider * mDensity));

        int tValueY = (int) (mMoveY / (mLineDivider * mDensity));
        if (Math.abs(tValueY) > 0) {
            mValue += tValueY;
            mMoveY -= tValueY * mLineDivider * mDensity;
            if (mValue <= 0 || mValue > mMaxValue) {
                mValue = mValue <= 0 ? 0 : mMaxValue;
                mMoveY = 0;
                mScroller.forceFinished(true);
            }
            notifyValueChange();
        }
//        if (Math.abs(tValue) > 0) {
//            mValue += tValue;
//            mMove -= tValue * mLineDivider * mDensity;
//            if (mValue <= 0 || mValue > mMaxValue) {
//                mValue = mValue <= 0 ? 0 : mMaxValue;
//                mMove = 0;
//                mScroller.forceFinished(true);
//            }
//            notifyValueChange();
//        }
        postInvalidate();
    }

    private void countMoveEnd() {
        int roundMove = Math.round(mMove / (mLineDivider * mDensity));
        int roundMoveY = Math.round(mMoveY / (mLineDivider * mDensity));

        mValue = mValue + roundMoveY;
        mValue = mValue <= 0 ? 0 : mValue;
        mValue = mValue > mMaxValue ? mMaxValue : mValue;

//        mValue = mValue + roundMove;
//        mValue = mValue <= 0 ? 0 : mValue;
//        mValue = mValue > mMaxValue ? mMaxValue : mValue;

        mLastY = 0;
        mMoveY= 0;
//        mLastX = 0;
//        mMove = 0;

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
            if (mScroller.getCurrY() == mScroller.getFinalY()){
                countMoveEnd();
            }else{
                int yPosition = mScroller.getCurrY();
                mMoveY += (mLastY - yPosition);
                changeMoveAndValue();
                mLastY = yPosition;
            }
//            if (mScroller.getCurrX() == mScroller.getFinalX()) { // over
//                countMoveEnd();
//            } else {
//                int xPosition = mScroller.getCurrX();
//                mMove += (mLastX - xPosition);
//                changeMoveAndValue();
//                mLastX = xPosition;
//            }
        }
    }
}

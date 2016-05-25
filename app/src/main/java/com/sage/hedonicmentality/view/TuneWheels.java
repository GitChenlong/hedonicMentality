package com.sage.hedonicmentality.view;

/**
 * Created by Administrator on 2015/10/29.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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

import com.sage.hedonicmentality.R;


/**
 * 卷尺控件类。<br>
 * 细节问题包括滑动过程中widget边缘的刻度显示问题等<br>
 *
 * 周末有时间会继续更新<br>
 *
 * @author ttdevs
 * @version create：
 */
@SuppressLint("ClickableViewAccessibility")
public class TuneWheels extends View {

    public interface OnValueChangeListener {
        public void onValueChange(float value);
    }

    public static final int MOD_TYPE_HALF = 2;
    public static final int MOD_TYPE_ONE = 10;

    private static final int ITEM_HALF_DIVIDER = 20;
    private static final int ITEM_ONE_DIVIDER = 10;

    private static final int ITEM_MAX_HEIGHT = 50;
    private static final int ITEM_MIN_HEIGHT = 20;

    private static final int TEXT_SIZE = 18;

    private float mDensity;
    private int mMaxValue = 200, mModType = MOD_TYPE_ONE, mLineDivider = ITEM_ONE_DIVIDER;
    public  static int mValue = 60;
    private int interval=60;
    // private int mValue = 50, mMaxValue = 500, mModType = MOD_TYPE_ONE,
    // mLineDivider = ITEM_ONE_DIVIDER;

    private int mLastX, mMove;
    private int mWidth, mHeight;

    private int mMinVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    float xPosition , textWidth;
    private OnValueChangeListener mListener;

    @SuppressWarnings("deprecation")
    public TuneWheels(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new Scroller(getContext());
        mDensity = getContext().getResources().getDisplayMetrics().density;

        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

        // setBackgroundResource(R.drawable.bg_wheel);
//        setBackgroundDrawable(createBackground());
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

    /*public void initViewParam(int defaultValue, int maxValue, int model) {
        switch (model) {
            case MOD_TYPE_HALF:
                mModType = MOD_TYPE_HALF;
                mLineDivider = ITEM_HALF_DIVIDER;
                mValue = defaultValue * 2;
                mMaxValue = maxValue * 2;
                break;
            case MOD_TYPE_ONE:
                mModType = MOD_TYPE_ONE;
                mLineDivider = ITEM_ONE_DIVIDER;
                mValue = defaultValue;
                mMaxValue = maxValue;
                break;

            default:
                break;
        }
        invalidate();

        mLastX = 0;
        mMove = 0;
        notifyValueChange();
    }*/

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
//         drawWheel(canvas);
        drawMiddleLine(canvas);
    }

    /*private void drawWheel(Canvas canvas) {
        Drawable wheel = getResources().getDrawable(R.mipmap.a1);
        Log.e("LOG","getWidth():"+getWidth()+"getHeight():"+getHeight());
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

//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.BLACK);
        Paint linePaints = new Paint();
        linePaints.setStrokeWidth(2);
        linePaints.setColor(getResources().getColor(R.color.linegreen));

//        Paint linePaint = new Paint();
//        linePaint.setStrokeWidth(2);
//        linePaint.setColor(Color.RED);

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(TEXT_SIZE * mDensity);

        int width = mWidth, drawCount = 0;
         xPosition = 0;
         textWidth = Layout.getDesiredWidth("0", textPaint);

        for (int i = 0; drawCount <= 4 * width; i++) {
//            System.out.print(getValue()+"当前刻度");
            int numSize = String.valueOf(mValue + i).length();

            xPosition = (width / 2 - mMove) + i * mLineDivider * mDensity;
            if (xPosition + getPaddingRight() < mWidth) {
                if ((mValue + i) % mModType == 0) {
                    canvas.drawLine(xPosition, getHeight()-textWidth-interval, xPosition, getHeight()-mDensity * ITEM_MAX_HEIGHT-textWidth-interval, linePaints);
                    if (mValue + i <= mMaxValue) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(String.valueOf((mValue + i) / 2), countLeftStart(mValue + i, xPosition, textWidth), getHeight() - textWidth, textPaint);

                                break;
                            case MOD_TYPE_ONE:
                                canvas.drawText(String.valueOf(mValue + i)+"kg", xPosition - (textWidth * numSize / 2), getHeight() - textWidth, textPaint);

                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    canvas.drawLine(xPosition,getHeight()-textWidth-interval, xPosition, getHeight()-mDensity * ITEM_MIN_HEIGHT-textWidth-interval, linePaint);
                }
            }

            xPosition = (width / 2 - mMove) - i * mLineDivider * mDensity;
            if (xPosition > getPaddingLeft()) {
                if ((mValue - i) % mModType == 0) {
                    canvas.drawLine(xPosition,getHeight()-textWidth-interval, xPosition, getHeight()-mDensity * ITEM_MAX_HEIGHT-textWidth-interval, linePaints);
                    if (mValue - i >= 0) {
                        switch (mModType) {
                            case MOD_TYPE_HALF:
                                canvas.drawText(String.valueOf((mValue - i) / 2), countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                                break;
                            case MOD_TYPE_ONE:
                                canvas.drawText(String.valueOf(mValue - i)+"kg", xPosition - (textWidth * numSize / 2), getHeight() - textWidth, textPaint);
                                break;

                            default:
                                break;
                        }
                    }
                } else {
                    canvas.drawLine(xPosition, getHeight()-textWidth-interval, xPosition, getHeight()-mDensity * ITEM_MIN_HEIGHT-textWidth-interval, linePaint);
                }
            }

            drawCount += 2 * mLineDivider * mDensity;
        }

        canvas.restore();
    }

    /**
     * 计算没有数字显示位置的辅助方法
     *
     * @param value
     * @param xPosition
     * @param textWidth
     * @return
     */
    private float countLeftStart(int value, float xPosition, float textWidth) {
        float xp = 0f;
        if (value < 20) {
            xp = xPosition - (textWidth * 1 / 2);
        } else {
            xp = xPosition - (textWidth * 2 / 2);
        }
        return xp;
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

        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(indexWidth);
        redPaint.setColor(getResources().getColor(R.color.linered));
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight - interval - textWidth, redPaint);//中间固定的红线

        /*Paint triangle = new Paint();
        triangle.setColor(getResources().getColor(R.color.linered));
        canvas.drawText("画三角形：", 10, 200, triangle);
        // 绘制这个三角形,你可以绘制任意多边形
        Path path = new Path();
        path.moveTo(80, 200);// 此点为多边形的起点
        path.lineTo(120, 250);
        path.lineTo(80, 250);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, triangle);*/

        //中间固定红线两头
//        Paint ovalPaint = new Paint();
//        ovalPaint.setColor(Color.RED);
//        ovalPaint.setStrokeWidth(indexTitleWidth);
//       canvas.drawLine(mWidth / 2, 0, mWidth / 2, indexTitleHight, ovalPaint);
//        canvas.drawLine(mWidth / 2, mHeight - indexTitleHight, mWidth / 2, mHeight, ovalPaint);

//         RectF ovalRectF = new RectF(mWidth / 2 - 10, 0, mWidth / 2 + 10, 4 *
//         mDensity); //TODO 椭圆
//         canvas.drawOval(ovalRectF, ovalPaint);
//         ovalRectF.set(mWidth / 2 - 10, mHeight - 8 * mDensity, mWidth / 2 +
//         10, mHeight); //TODO

//        Paint shadowPaint = new Paint();
//        shadowPaint.setStrokeWidth(shadow);
//        shadowPaint.setColor(Color.parseColor(color));
//        canvas.drawLine(mWidth / 2 + gap, 0, mWidth / 2 + gap, mHeight, shadowPaint);

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
                mValue = mValue <= 0 ? 0 : mMaxValue;
                mMove = 0;
                mScroller.forceFinished(true);
            }
            notifyValueChange();
        }
        postInvalidate();
    }

    private void countMoveEnd() {
        int roundMove = Math.round(mMove / (mLineDivider * mDensity));
        mValue = mValue + roundMove;
        mValue = mValue <= 0 ? 0 : mValue;
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

package com.sage.hedonicmentality.widget;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sage.hedonicmentality.R;

/**
 * Created by Sage on 2015/8/12.
 */
public class DrawerTopLayout extends LinearLayout {
    private View mDragView;
    private View showLayout;
    private ImageView iv_show;
    Point originalPoint=new Point();
    Point hiddenPoint=new Point();
    public DrawerTopLayout(Context context) {
        this(context, null);
    }

    public DrawerTopLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerTopLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ViewDragHelper mDragger;
    private void init(){
        mDragger=ViewDragHelper.create(this, 1, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child==mDragView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return getPaddingLeft();
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                 int topBound = getPaddingTop();
                int bottomBound=getHeight();
                if(showLayout!=null){
                   bottomBound=hiddenPoint.y;
                }
                return Math.min(Math.max(top, topBound), bottomBound);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
               show_hidden();
            }

            @Override
            public int getViewHorizontalDragRange(View child)
            {
                return getMeasuredWidth()-child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child)
            {
                return getMeasuredHeight()-child.getMeasuredHeight();
            }
        });
    }

    public void show_hidden(){
        float offset = (mDragView.getTop()-originalPoint.y) * 1.0f / (hiddenPoint.y-originalPoint.y);
        mDragger.settleCapturedViewAt(originalPoint.x,offset > 0.5f ?   hiddenPoint.y:originalPoint.y);
        if(iv_show!=null){
            iv_show.setImageResource(offset>0.5f?R.mipmap.jiantou_up:R.mipmap.jiantou_down);
        }
            postInvalidate();
    }

    public void show_hidden_click(){
        float offset = (mDragView.getTop()-originalPoint.y) * 1.0f / (hiddenPoint.y-originalPoint.y);
        mDragger.smoothSlideViewTo(mDragView, originalPoint.x, offset < 0.5f ? hiddenPoint.y : originalPoint.y);
        if(iv_show!=null){
            iv_show.setImageResource(offset<0.5f?R.mipmap.jiantou_up:R.mipmap.jiantou_down);
        }
        postInvalidate();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        mDragger.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll()
    {
        if(mDragger.continueSettling(true))
        {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        originalPoint.x=mDragView.getLeft();
        originalPoint.y=mDragView.getTop();
       if(showLayout!=null){
           hiddenPoint.x=mDragView.getLeft();
           hiddenPoint.y=b-t-getPaddingTop()-showLayout.getHeight();
       }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        showLayout=findViewById(R.id.show_layout);
        iv_show= (ImageView) findViewById(R.id.iv_show);
    }

}

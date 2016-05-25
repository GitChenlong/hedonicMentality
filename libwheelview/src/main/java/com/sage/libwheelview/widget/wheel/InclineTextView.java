package com.sage.libwheelview.widget.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Sage on 2015/7/28.
 */
public class InclineTextView extends TextView {
    public InclineTextView(Context context) {
        super(context);
    }

    public InclineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InclineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        //倾斜度45,上下左右居中
       // canvas.rotate(15, getMeasuredWidth()/3, getMeasuredHeight()/3);
//        if(getMeasuredWidth()!= 0&&getMeasuredHeight()!=0)
//        canvas.scale(0,getMeasuredHeight()*0.1f,getMeasuredWidth()/2,getMeasuredHeight()/2);
        super.onDraw(canvas);
    }
}

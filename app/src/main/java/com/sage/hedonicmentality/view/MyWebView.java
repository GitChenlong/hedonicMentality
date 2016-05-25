package com.sage.hedonicmentality.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by Administrator on 2015/12/29.
 */
public class MyWebView extends WebView {

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyWebView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        Log.d("touchevent", "touchevent" + super.onTouchEvent(ev));
//            return super.onTouchEvent(ev);
        return false;
    }
}
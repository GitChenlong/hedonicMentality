package com.sage.hedonicmentality.view;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/11/13.
 */
public class MyViewpager extends ViewPager {

    public MyViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyViewpager(Context context) {
        super(context);
    }

    @Override
    public void removeView(View view) {
//        super.removeView(view);
    }
}

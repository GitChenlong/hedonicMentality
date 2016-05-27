package com.sage.hedonicmentality.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/5/26.
 */
public class MyGridView extends GridView {
    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MIN_VALUE <<1,
                MeasureSpec.AT_MOST);
//        int expandSpec = MeasureSpec.makeMeasureSpec(1,
//                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

package com.sage.hedonicmentality.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import com.sage.libwheelview.widget.wheel.adapter.NumericWheelAdapter;

/**
 * Created by Sage on 2015/7/28.
 */
public class BreathWheelAdapter extends NumericWheelAdapter {

    public int currentItem;
    public int currentValue;
    private int minValue;
    public BreathWheelAdapter(Context context, int minValue, int maxValue,int currentValue) {
        super(context, minValue, maxValue);
        this.currentValue=currentValue;
        this.minValue=minValue;
        setTextColor(Color.WHITE);
        setTextSize(20);

    }
    protected void configureTextView(TextView view) {
        super.configureTextView(view);
        view.setTypeface(Typeface.SANS_SERIF);
    }

    public CharSequence getItemText(int index) {
        currentItem = index;
        currentValue=minValue+index;
        return super.getItemText(index);
    }
}

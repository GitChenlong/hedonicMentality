package com.sage.hedonicmentality.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by Sage on 2015/7/16.
 */
public class UtilSnackbar {

    public static void showSimple(View view,String toast){
        show(view,toast, Color.RED);
    }
    public static void show(View view,String toast,int color){
        if(view==null|| TextUtils.isEmpty(toast)){
            return;
        }
        Snackbar snackbar=Snackbar.make(view, toast, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }
}

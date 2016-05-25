package com.sage.hedonicmentality.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.FragmentMain;

/**
 * Created by Sage on 2015/7/16.
 */
public class ActivityMain extends ActivityBase {

//    @Override
//    public int getLayout() {
//        return R.layout.activity_main;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mToolbar.setBackgroundColor(Color.TRANSPARENT);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentMain(),FragmentMain.TAG).commit();
            setBackground(R.drawable.u18);
        }

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return false;
//    }

}

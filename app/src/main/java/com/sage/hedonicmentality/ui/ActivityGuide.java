package com.sage.hedonicmentality.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.adapter.MyFragmentPagerAdapter;
import com.sage.hedonicmentality.myviewpager.ViewPager;
import com.sage.hedonicmentality.ui.simple.BreathSetting;
import com.sage.hedonicmentality.utils.Contact;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.utils.SharedPreferencesHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sage on 2015/8/6.
 */
public class ActivityGuide extends AppCompatActivity {

    @Bind(R.id.myViewpager)
    ViewPager myViewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        init();
    }


    private void init(){
        SPHelper.putDefaultString(this, SPHelper.KEY_LAST_VERSION, getString(R.string.version));
        myViewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
    }


}

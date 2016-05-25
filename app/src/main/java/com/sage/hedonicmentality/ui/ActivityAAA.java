package com.sage.hedonicmentality.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.breath.FragmentDiaPrompt;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/28.
 */
public class ActivityAAA extends AppCompatActivity  {

    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aaa);
        ButterKnife.bind(this);
//        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
    }

    @OnClick({R.id.btn1,R.id.btn2})
    void aaaClick(View view){
    switch (view.getId()){
    case R.id.btn1:
        FragmentDiaPrompt diaPrompt=FragmentDiaPrompt.create(0);
        diaPrompt.show(getSupportFragmentManager(),"000");
        break;
    case R.id.btn2:
        FragmentDiaPrompt diaPrompt1=FragmentDiaPrompt.create(1);
        diaPrompt1.show(getSupportFragmentManager(),"1111");
        break;
}
    }


}

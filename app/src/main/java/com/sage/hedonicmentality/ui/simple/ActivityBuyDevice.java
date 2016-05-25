package com.sage.hedonicmentality.ui.simple;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sage.hedonicmentality.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/8/11.
 */
public class ActivityBuyDevice extends AppCompatActivity {

    @Bind(R.id.tv_intro)
    TextView tv_intro;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_device);
        ButterKnife.bind(this);
        tv_title.setText("周边产品助养生");
        tv_intro.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_left,R.id.btn_go_buy})
    void buy_click(View view){
        switch (view.getId()){
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_go_buy:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}

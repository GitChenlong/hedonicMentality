package com.sage.hedonicmentality.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sage.hedonicmentality.R;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/12/31.
 */
public class ActivityAgreement extends ActivityBase {
//    @Bind(R.id.tv_title)
    TextView tv_title;
//    @Bind(R.id.btn_left)
    ImageView btn_left;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
//           resetBreath();
            setContentView(R.layout.fragment_agreement);
            tv_title = (TextView)this.findViewById(R.id.tv_title);
            btn_left = (ImageView)this.findViewById(R.id.btn_left);
            tv_title.setText(getString(R.string.title_agreement));
            btn_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }
}

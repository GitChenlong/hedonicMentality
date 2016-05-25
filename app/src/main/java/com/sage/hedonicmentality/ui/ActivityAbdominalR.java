package com.sage.hedonicmentality.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.abdominal.FragmentHao;
import com.sage.hedonicmentality.fragment.abdominal.FragmentJian;
import com.sage.hedonicmentality.fragment.abdominal.FragmentYuan;
import com.sage.hedonicmentality.fragment.abdominal.FragmentZuo;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ActivityAbdominalR extends AppCompatActivity {

//    @Bind(R.id.btn_left)
    ImageView btn_left;
//    @Bind(R.id.tv_title)
    TextView tv_title;
//    @Bind(R.id.btn_right)
    ImageView btn_right;
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
//    @Bind(R.id.bt_hao)
    ImageView bt_hao;
//    @Bind(R.id.bt_yuan)
    ImageView bt_yuan;
//    @Bind(R.id.bt_zuo)
    ImageView bt_zuo;
//    @Bind(R.id.bt_jian)
    ImageView bt_jian;

    TextView tv_hao;
    TextView tv_yuan;
    TextView tv_zuo;
    TextView tv_jian;
    private FragmentManager fragmentManager;
    private FragmentHao fragmentHao;
    private FragmentYuan fragmentYuan;
    private FragmentZuo fragmentZuo;
    private FragmentJian fragmentJian;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityabdominalr);
        fragmentManager = getSupportFragmentManager();
        initView();
    }
    private void initView() {
        bt_hao = (ImageView)findViewById(R.id.bt_hao);
        bt_yuan = (ImageView)findViewById(R.id.bt_yuan);
        bt_zuo = (ImageView)findViewById(R.id.bt_zuo);
        bt_jian = (ImageView)findViewById(R.id.bt_jian);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_hao = (TextView)findViewById(R.id.tv_hao);
        tv_yuan = (TextView)findViewById(R.id.tv_yuan);
        tv_zuo = (TextView)findViewById(R.id.tv_zuo);
        tv_jian = (TextView)findViewById(R.id.tv_jian);

        btn_left = (ImageView)findViewById(R.id.btn_left);
        layout_actionbar = (RelativeLayout)findViewById(R.id.layout_actionbar);
        bt_hao.setOnClickListener(this.MyOnclickListenter);
        bt_yuan.setOnClickListener(this.MyOnclickListenter);
        bt_zuo.setOnClickListener(this.MyOnclickListenter);
        bt_jian.setOnClickListener(this.MyOnclickListenter);
        btn_left.setOnClickListener(this.MyOnclickListenter);
        bt_hao.setRotation(180);
        setTabSelection(1);
        setBtnBg(1);
        tv_title.setText("腹式呼吸");
        layout_actionbar.setBackgroundColor(getResources().getColor(R.color.dark_green));
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
    }
    private void setTabSelection(int index) {
        transaction = fragmentManager.beginTransaction();
        switch (index){
            case 1:
                setBtnBg(1);
                if (fragmentHao == null){
                    fragmentHao = new FragmentHao();
                }
                transaction.replace(R.id.fl_ar,fragmentHao);
                break;
            case 2:
                setBtnBg(2);
                if (fragmentYuan == null){
                    fragmentYuan = new FragmentYuan();
                }
                transaction.replace(R.id.fl_ar,fragmentYuan);
                break;
            case 3:
                setBtnBg(3);
                if (fragmentZuo == null){
                    fragmentZuo = new FragmentZuo();
                }
                transaction.replace(R.id.fl_ar,fragmentZuo);
                break;
            case 4:
                setBtnBg(4);
                if (fragmentJian == null){
                    fragmentJian = new FragmentJian();
                }
                transaction.replace(R.id.fl_ar, fragmentJian);
                break;
        }
        transaction.commit();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setBtnBg(int type){
        if (type!=1) {
            bt_hao.setBackground(getResources().getDrawable(R.mipmap.btn_hao));
            tv_hao.setTextColor(getResources().getColor(R.color.account_focus));
        }else{
            bt_hao.setBackground(getResources().getDrawable(R.mipmap.btn_hao_bg));
            tv_hao.setTextColor(getResources().getColor(R.color.whi));
        }
        if (type!=2) {
            bt_yuan.setBackground(getResources().getDrawable(R.mipmap.btn_yuan));
            tv_yuan.setTextColor(getResources().getColor(R.color.account_focus));
        }else{
            bt_yuan.setBackground(getResources().getDrawable(R.mipmap.btn_yuan_bg));
            tv_yuan.setTextColor(getResources().getColor(R.color.whi));
        }
        if (type!=3) {
            bt_zuo.setBackground(getResources().getDrawable(R.mipmap.btn_yuan));
            tv_zuo.setTextColor(getResources().getColor(R.color.account_focus));
        }else{
            bt_zuo.setBackground(getResources().getDrawable(R.mipmap.btn_yuan_bg));
            tv_zuo.setTextColor(getResources().getColor(R.color.whi));
        }
        if (type!=4) {
            bt_jian.setBackground(getResources().getDrawable(R.mipmap.btn_hao));
            tv_jian.setTextColor(getResources().getColor(R.color.account_focus));
        }else{
            bt_jian.setBackground(getResources().getDrawable(R.mipmap.btn_hao_bg));
            tv_jian.setTextColor(getResources().getColor(R.color.whi));
        }
    }
    public View.OnClickListener MyOnclickListenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_hao:
                    setTabSelection(1);
                    break;
                case R.id.bt_yuan:
                    setTabSelection(2);
                    break;
                case R.id.bt_zuo:
                    setTabSelection(3);
                    break;
                case R.id.bt_jian:
                    setTabSelection(4);
                    break;
                case R.id.btn_left:
                    finish();
                    break;
            }
        }
    };
}

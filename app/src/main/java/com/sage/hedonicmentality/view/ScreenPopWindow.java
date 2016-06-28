package com.sage.hedonicmentality.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.Indicate;
import com.sage.hedonicmentality.bean.ScreenBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/30.
 */
public class ScreenPopWindow extends PopupWindow  {
    private final View mMenuView;
    private final ViewFlipper viewfipper;
    @Bind(R.id.rb_price_two)
    TextView rb_price_two;
    @Bind(R.id.rb_price_one)
    TextView rb_price_one;
    @Bind(R.id.rb_price_three)
    TextView rb_price_three;
    @Bind(R.id.rb_price_four)
    TextView rb_price_four;
    @Bind(R.id.rb_age_one)
    TextView rb_age_one;
    @Bind(R.id.rb_age_two)
    TextView rb_age_two;
    @Bind(R.id.rb_age_three)
    TextView rb_age_three;
    @Bind(R.id.rb_age_four)
    TextView rb_age_four;
    @Bind(R.id.rb_age_five)
    TextView rb_age_five;
    @Bind(R.id.rb_age_six)
    TextView rb_age_six;
    @Bind(R.id.rb_age_seven)
    TextView rb_age_seven;
    @Bind(R.id.rb_sex_one)
    TextView rb_sex_one;
    @Bind(R.id.rb_sex_two)
    TextView rb_sex_two;
    @Bind(R.id.rb_sex_three)
    TextView rb_sex_three;
    @Bind(R.id.rb_oneline_one)
    TextView rb_oneline_one;
    @Bind(R.id.rb_oneline_two)
    TextView rb_oneline_two;
    @Bind(R.id.tv_reset)
    TextView tv_reset;
    @Bind(R.id.tv_affirm)
    TextView tv_affirm;
    private Context mcontext;
    private Handler mHandler;
    private List<String> mlist = new ArrayList<>();
    private ScreenBean screenBean;
    @Bind(R.id.rl_screen)
    RelativeLayout rl_screen;

    public ScreenPopWindow(Context context,Handler handler) {
        super(context);
//        setAnimationStyle(R.style.cnsultAnimation);
        this.mcontext = context;
        this.mHandler = handler;
        screenBean = new ScreenBean();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.screenpopwindow, null);
        ButterKnife.bind(this, mMenuView);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);
        this.setContentView(viewfipper);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(false);
        this.update();
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        viewfipper.startFlipping();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        viewfipper.startFlipping();
    }
    public void sendMessageAffirm(){
        Message message = new Message();
        message.what=3;
        Bundle bundle = new Bundle();
        bundle.putSerializable("screen",screenBean);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    @OnClick({R.id.rb_price_one,R.id.rb_price_two,R.id.rb_price_three,R.id.rb_price_four,
            R.id.rb_age_one,R.id.rb_age_two,R.id.rb_age_three,R.id.rb_age_four,R.id.rb_age_five,
            R.id.rb_age_six,R.id.rb_age_seven,R.id.tv_affirm,R.id.tv_reset,R.id.rb_sex_one,
            R.id.rb_sex_two,R.id.rb_sex_three,R.id.rb_oneline_one,R.id.rb_oneline_two
    })
    public void screenOnclick(View view){
        if (view.getId()==R.id.tv_affirm) {
            sendMessageAffirm();
            dismiss();
        }
        if (view.getId()==R.id.tv_reset) {
            setAgebackground(rb_price_one,true);
            setAgebackground(rb_price_two,false);
            setAgebackground(rb_price_three,false);
            setAgebackground(rb_price_four,false);
            setAgebackground(rb_age_one,true);
            setAgebackground(rb_age_two,false);
            setAgebackground(rb_age_three,false);
            setAgebackground(rb_age_four,false);
            setAgebackground(rb_age_five,false);
            setAgebackground(rb_age_six,false);
            setAgebackground(rb_age_seven,false);

            screenBean = new ScreenBean("不限","不限","不限","是");
        }
        switch (view.getId()){
            case R.id.rb_sex_one:
                setAgebackground(rb_sex_one,true);
                setAgebackground(rb_sex_two,false);
                setAgebackground(rb_sex_three,false);
                screenBean.setSex("不限");
                break;
            case R.id.rb_sex_two:
                setAgebackground(rb_sex_one,false);
                setAgebackground(rb_sex_two,true);
                setAgebackground(rb_sex_three,false);
                screenBean.setSex("男");
                break;
            case R.id.rb_sex_three:
                setAgebackground(rb_sex_one,false);
                setAgebackground(rb_sex_two,false);
                setAgebackground(rb_sex_three,true);
                screenBean.setSex("女");
                break;
            case R.id.rb_oneline_one:
                setAgebackground(rb_oneline_one,true);
                setAgebackground(rb_oneline_two,true);
                screenBean.setIsOnLine("是");
                break;
            case R.id.rb_oneline_two:
                setAgebackground(rb_oneline_one,false);
                setAgebackground(rb_oneline_two,true);
                screenBean.setIsOnLine("否");
                break;
            case R.id.rb_price_one:
                setAgebackground(rb_price_one,true);
                setAgebackground(rb_price_two,false);
                setAgebackground(rb_price_three,false);
                setAgebackground(rb_price_four,false);
                screenBean.setPrice("不限");
                break;
            case R.id.rb_price_two:
                setAgebackground(rb_price_one,false);
                setAgebackground(rb_price_two,true);
                setAgebackground(rb_price_three,false);
                setAgebackground(rb_price_four,false);
                screenBean.setPrice("0-99");
                break;
            case R.id.rb_price_three:
                setAgebackground(rb_price_one,false);
                setAgebackground(rb_price_two,false);
                setAgebackground(rb_price_three,true);
                setAgebackground(rb_price_four,false);
                screenBean.setPrice("100-199");
                break;
            case R.id.rb_price_four:
                setAgebackground(rb_price_one,false);
                setAgebackground(rb_price_two,false);
                setAgebackground(rb_price_three,false);
                setAgebackground(rb_price_four,true);
                screenBean.setPrice("200-299");
                break;
            case R.id.rb_age_one:
                setAgebackground(rb_age_one,true);
                setAgebackground(rb_age_two,false);
                setAgebackground(rb_age_three,false);
                setAgebackground(rb_age_four,false);
                setAgebackground(rb_age_five,false);
                setAgebackground(rb_age_six,false);
                setAgebackground(rb_age_seven,false);
                screenBean.setAge("不限");
                break;
            case R.id.rb_age_two:
                setAgebackground(rb_age_one,false);
                setAgebackground(rb_age_two,true);
                setAgebackground(rb_age_three,false);
                setAgebackground(rb_age_four,false);
                setAgebackground(rb_age_five,false);
                setAgebackground(rb_age_six,false);
                setAgebackground(rb_age_seven, false);
                screenBean.setAge("40后");
                break;
            case R.id.rb_age_three:
                setAgebackground(rb_age_one,false);
                setAgebackground(rb_age_two,false);
                setAgebackground(rb_age_three,true);
                setAgebackground(rb_age_four,false);
                setAgebackground(rb_age_five,false);
                setAgebackground(rb_age_six,false);
                setAgebackground(rb_age_seven, false);
                screenBean.setAge("50后");

                break;
            case R.id.rb_age_four:
                setAgebackground(rb_age_one,false);
                setAgebackground(rb_age_two,false);
                setAgebackground(rb_age_three,false);
                setAgebackground(rb_age_four,true);
                setAgebackground(rb_age_five,false);
                setAgebackground(rb_age_six,false);
                setAgebackground(rb_age_seven, false);
                screenBean.setAge("60后");
                break;
            case R.id.rb_age_five:
                setAgebackground(rb_age_one,false);
                setAgebackground(rb_age_two,false);
                setAgebackground(rb_age_three,false);
                setAgebackground(rb_age_four,false);
                setAgebackground(rb_age_five,true);
                setAgebackground(rb_age_six,false);
                setAgebackground(rb_age_seven,false);
                screenBean.setAge("70后");
                break;
            case R.id.rb_age_six:
                setAgebackground(rb_age_one,false);
                setAgebackground(rb_age_two,false);
                setAgebackground(rb_age_three,false);
                setAgebackground(rb_age_four,false);
                setAgebackground(rb_age_five,false);
                setAgebackground(rb_age_six,true);
                setAgebackground(rb_age_seven,false);
                screenBean.setAge("80后");
                break;
            case R.id.rb_age_seven:
                setAgebackground(rb_age_one,false);
                setAgebackground(rb_age_two,false);
                setAgebackground(rb_age_three,false);
                setAgebackground(rb_age_four,false);
                setAgebackground(rb_age_five,false);
                setAgebackground(rb_age_six,false);
                setAgebackground(rb_age_seven,true);
                screenBean.setAge("90后");
                break;
        }
    }
    public void setAgebackground(TextView view ,boolean isChecked) {
        Drawable drawable = isChecked ? mcontext.getResources().getDrawable(R.drawable.btn_screen_check) : mcontext.getResources().getDrawable(R.drawable.btn_screen);
        int color = isChecked ? mcontext.getResources().getColor(R.color.orange_secondary_color) : mcontext.getResources().getColor(R.color.txt_annotation_color);
        view.setBackground(drawable);
        view.setTextColor(color);


    }
}

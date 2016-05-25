package com.sage.hedonicmentality.fragment.breath;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.adapter.BreathWheelAdapter;
import com.sage.hedonicmentality.fragment.BaseFragment;
import com.sage.libwheelview.widget.wheel.OnWheelChangedListener;
import com.sage.libwheelview.widget.wheel.WheelView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Sage on 2015/7/28.
 */
public class FragmentBreath extends BaseFragment {

    @Bind(R.id.viewAnimator)
    ViewAnimator viewAnimator;
    @Bind(R.id.tv_breath_count)
    TextView tv_breath_count;
    @Bind(R.id.tv_breath_popup)
    TextView tv_breath_popup;
    @Bind(R.id.tv_start)
    TextView tv_start;
    @Bind(R.id.wheel_breath)
    WheelView  wheel_breath;
    private int minValue=4;
    private int defaultValue=6;
    private BreathWheelAdapter wheelAdapter;
    private boolean start=false;
    private Animation animation;


    @Override
    public int getLayout() {
        return R.layout.fragment_breath;
    }

    @Override
    public void initActionbar() {
        tv_title.setText("00:00");
        tv_breath_count.setText(getString(R.string.breath_count_format, defaultValue));
        wheelAdapter=new BreathWheelAdapter(getActivity(),minValue,10,defaultValue);
        wheel_breath.setViewAdapter(wheelAdapter);
        wheel_breath.setWheelBackground(R.drawable.wheel_bg_white);
        wheel_breath.setWheelForeground(R.drawable.wheel_fore_white);
        wheel_breath.setDrawShadows(false);
        wheel_breath.setCurrentItem(defaultValue - minValue);
        wheel_breath.addChangingListener(wheelListener);


        animation= AnimationUtils.loadAnimation(getActivity(), R.anim.scale1to0);
        animation.setDuration(5000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                LogUtils.i("onAnimationStart"+ "+++++++");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LogUtils.i("end"+"+++++++");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private OnWheelChangedListener wheelListener=new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            defaultValue=newValue+minValue;
            tv_breath_count.setText(getString(R.string.breath_count_format,defaultValue));
        }
    };


    @OnClick({R.id.btn_right,R.id.btn_changeBreath,R.id.tv_start})
    void breathClick(View view){
        switch(view.getId()){
            case R.id.btn_right:
                //((ActivityBreath)getActivity()).goBreathInfo();
                break;
            case R.id.btn_changeBreath:
                viewAnimator.setDisplayedChild(1);
                break;
            case R.id.tv_start:
                if(start){
                    animation.cancel();
                }else{
                    tv_start.startAnimation(animation);
                }
                viewAnimator.setDisplayedChild(start?0:2);
                tv_breath_popup.setText("静下心来.."+defaultValue);
                start=!start;
                break;

        }
    }



}

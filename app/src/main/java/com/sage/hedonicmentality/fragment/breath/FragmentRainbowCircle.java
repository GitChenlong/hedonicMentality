package com.sage.hedonicmentality.fragment.breath;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.HRVData;
import com.sage.hedonicmentality.bean.SoundPoolData;
import com.sage.hedonicmentality.ui.ActivityBreath;
import com.sage.hedonicmentality.utils.SPHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/11/12.
 */
public class FragmentRainbowCircle extends FragmentHRBase{
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Bind(R.id.yuandian1)
    ImageView yuandian1;
    @Bind(R.id.yuandian2)
    ImageView yuandian2;
    @Bind(R.id.yuandian3)
    ImageView yuandian3;
    @Bind(R.id.yuandian4)
    ImageView yuandian4;
    @Bind(R.id.yuandian5)
    ImageView yuandian5;
    @Bind(R.id.yuandian6)
    ImageView yuandian6;
    @Bind(R.id.yuandian7)
    ImageView yuandian7;
    @Bind(R.id.yuandian8)
    ImageView yuandian8;
    @Bind(R.id.yuandian9)
    ImageView yuandian9;
    @Bind(R.id.yuandian10)
    ImageView yuandian10;
    @Bind(R.id.yuandian11)
    ImageView yuandian11;
    @Bind(R.id.yuandian12)
    ImageView yuandian12;
    @Bind(R.id.yuandian13)
    ImageView yuandian13;
    @Bind(R.id.yuandian14)
    ImageView yuandian14;
    @Bind(R.id.yuandian15)
    ImageView yuandian15;
    @Bind(R.id.yuandian16)
    ImageView yuandian16;
    @Bind(R.id.yuandian17)
    ImageView yuandian17;
    @Bind(R.id.yuandian18)
    ImageView yuandian18;
    @Bind(R.id.yuandian19)
    ImageView yuandian19;
    @Bind(R.id.yuandian20)
    ImageView yuandian20;
    @Bind(R.id.yuandian21)
    ImageView yuandian21;
    @Bind(R.id.yuandian22)
    ImageView yuandian22;
    @Bind(R.id.yuandian23)
    ImageView yuandian23;
    @Bind(R.id.yuandian24)
    ImageView yuandian24;
    @Bind(R.id.yuandian25)
    ImageView yuandian25;
    @Bind(R.id.yuandian26)
    ImageView yuandian26;
    @Bind(R.id.yuandian27)
    ImageView yuandian27;
    @Bind(R.id.yuandian28)
    ImageView yuandian28;
    @Bind(R.id.yuandian29)
    ImageView yuandian29;
    @Bind(R.id.yuandian30)
    ImageView yuandian30;
    @Bind(R.id.yuandian31)
    ImageView yuandian31;
    @Bind(R.id.yuandian32)
    ImageView yuandian32;
    @Bind(R.id.yuandian33)
    ImageView yuandian33;
    @Bind(R.id.yuandian34)
    ImageView yuandian34;
    @Bind(R.id.yuandian35)
    ImageView yuandian35;
    @Bind(R.id.yuandian36)
    ImageView yuandian36;
    @Bind(R.id.caihongquan)
    ImageView caihongquan;
    @Bind(R.id.iv_level)
    ImageView iv_level;
    @Bind(R.id.tv_hrxl)
    TextView tv_hrxl;
    @Bind(R.id.tv_hp)
    TextView tv_hp;
    @Bind(R.id.tv_mytime)
    TextView tv_mytime;
    @Bind(R.id.tv_state)
    TextView tv_state;
    private MyReceivers myreceiver = new MyReceivers();
    private List<ImageView> ivs = new ArrayList<ImageView>();

    private Animation animation;/**呼吸的收缩动画*/

    public static final String ACTION = "com.hrv.data";
    //小点总数
    private int num = 0;
    //红色小点
    private int ired = 0;
    //蓝色小点
    private int iblue = 0;
    //绿色小点
    private int igreen = 0;
    //生命值
    private int HP = 0;

    //计算区间的总分值
    private int sumhp = 88;
    //等级
    private int level = 0;

    @Override
    public int getLayout() {
        return R.layout.fragment_rainbow;
    }

    private View view;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if(null == view){
//            view = inflater.inflate(R.layout.breath_main, container, false);
//        }
//        return view;
//    }

    @Override
    public void onTaskComplete(HRVData data) {
//        setdian(data);
//        Toast.makeText(getActivity(),"数据来了"+data.toString(),Toast.LENGTH_SHORT).show();
    }

    //计算颜色小点的个数
    private void setdian(HRVData data){
        if(0 == data.getHf()||0 == data.getLf()){
            return;
        }
        getLevel(HP);
        float a  = data.getHf()/data.getLf();
        if(num < 36){
            if(a >= 0 && a < 0.5){
                setText(3);
                ired++;
            }else
            if(a >=0.5 && a < 1){
                setText(2);
                HP = HP+1;
                iblue++;
            }else
            if(a > 1){
                setText(1);
                HP = HP+2;
                igreen++;
            }
        }else
        if(num == 36){
            if(a >= 0 && a < 0.5){
                setText(3);
                if(igreen!=0&&iblue!=0){
                    ired++;
                    iblue--;
                }else
                if(iblue == 0 && ired!=36){
                    ired++;
                    igreen--;
                }else
                if(ired == 36){

                }else
                if(iblue == 36){
                    ired++;
                    iblue--;
                }else
                if(igreen == 0 && iblue !=0){
                    ired++;
                    iblue--;
                }else if(iblue == 0 && igreen !=0){
                    ired++;
                    igreen--;
                }
            }else
            if(a >=0.5 && a < 1){
                setText(2);
                HP = HP+1;
                if(igreen!=0&&ired!=0){
                    iblue++;
                    igreen--;
                }else if(igreen == 0&&iblue != 36){
                    iblue++;
                    ired--;
                }else if(igreen == 36){
                    iblue++;
                    igreen--;
                }else if(iblue == 36){

                }else if(igreen == 0&& ired!=0){
                    iblue++;
                    ired --;
                }else if(ired ==0&&igreen!=0){
                    iblue++;
                    igreen--;
                }
            }else
            if(a > 1){
                setText(1);
                HP = HP+2;
                if(iblue!=0&&ired!=0){
                    igreen++;
                    ired--;
                }else if(ired == 0&&igreen!=36){
                    igreen++;
                    iblue--;
                }else if(ired ==36 ){
                    igreen++;
                    ired--;
                }else if(igreen == 36){

                }else if(ired ==0&&iblue!=0){
                    igreen++;
                    iblue--;
                }else if(iblue ==0&&ired!=0){
                    igreen++;
                    ired--;
                }
            }
        }
        tv_hp.setText(HP+"");
        num = ired+iblue+igreen;
        LogUtils.e("num:" + num + "+ired:" + ired + "+iblue:" + iblue + "+igreen:" +igreen+"+a:"+a);
        setivCaiHong();
    }

    public void setText(int id){
        if(id == 1){
            tv_state.setText(R.string.reminder1);
            tv_state.setTextColor(getResources().getColor(R.color.gre));
        }
        if(id == 2){
            tv_state.setText(R.string.reminder2);
            tv_state.setTextColor(getResources().getColor(R.color.ye));
        }
        if(id == 3){
            tv_state.setText(R.string.reminder3);
            tv_state.setTextColor(getResources().getColor(R.color.red));
        }
    }
    private void  setivCaiHong(){
        for (int i = 0;i<ivs.size();i++){
            ivs.get(i).setImageResource(R.mipmap.hei);
        }
        if(num < 36){
            for (int i = 0;i<ivs.size();i++){
                if(i < ired){
                    ivs.get(i).setImageResource(R.mipmap.hongdian);
                }
                if(i<(ired+iblue)&&i>(ired-1)){
                    ivs.get(i).setImageResource(R.mipmap.lan);
                }
//                if(ired==0&&iblue==0&&igreen==1){
//                    ivs.get(0).setImageResource(R.mipmap.lvdian);
//                }else
                if(i > (ired+iblue-1)&&i<num){
                    ivs.get(i).setImageResource(R.mipmap.lvdian);
                }
            }
        }else if (num == 36){
            for (int i = 0;i<ivs.size();i++){
                if(i < ired){
                    ivs.get(i).setImageResource(R.mipmap.hongdian);
                }
                if(i<(ired+iblue)&&i>(ired-1)){
                    ivs.get(i).setImageResource(R.mipmap.lan);
                }
                if(i > (ired+iblue-1)){
                    ivs.get(i).setImageResource(R.mipmap.lvdian);
                }
            }
        }
    }

    /**通过HP 计算等级*/
    public void getLevel(int hp){
        int  time  = SPHelper.getDefaultInt(getActivity(), SPHelper.KEY_DURATION, 5);
        if(5 == time){
            sumhp = 88;
        }
        if(10 == time){
            sumhp = 198;
        }
        if(15 == time){
            sumhp = 308;
        }
        if(20 == time){
            sumhp = 418;
        }
        if(30 == time){
            sumhp = 638;
        }
        if(60 == time){
            sumhp = 1298;
        }
        int le1,le2,le3,le4,le5,le6,le7,le8,le9;
        le1 = sumhp/10; le2 = sumhp*2/10; le3 = sumhp*3/10; le4 = sumhp*4/10;  le5 = sumhp*5/10;
        le6 = sumhp*58/100; le7 = sumhp*66/100; le8 = sumhp*74/100; le9 = sumhp*82/100;

        if(hp < le1) {
            level = 0;
            iv_level.setImageResource(R.mipmap.level0);
        }else
        if (hp > (le1 - 1) && hp < le2) {
            level = 1;
            iv_level.setImageResource(R.mipmap.level1);
        } else if (hp > (le2 - 1) && hp < le3) {
            level = 2;
            iv_level.setImageResource(R.mipmap.level2);
        } else if (hp > (le3 - 1) && hp < le4) {
            level = 3;
            iv_level.setImageResource(R.mipmap.level3);
        } else if (hp > (le4 - 1) && hp < le5) {
            level = 4;
            iv_level.setImageResource(R.mipmap.level4);
        } else if (hp > (le5 - 1) && hp < le6) {
            level = 5;
            iv_level.setImageResource(R.mipmap.level5);
        } else if (hp > (le6 - 1) && hp < le7) {
            level = 6;
            iv_level.setImageResource(R.mipmap.level6);
        } else if (hp > (le7 - 1) && hp < le8) {
            level = 7;
            iv_level.setImageResource(R.mipmap.level7);
        } else if (hp > (le8 - 1) && hp < le9) {
            level = 8;
            iv_level.setImageResource(R.mipmap.level8);
        } else
        if (hp > (le9 - 1) && hp < (sumhp + 1)){
            level = 9;
            iv_level.setImageResource(R.mipmap.level9);
        }
        LogUtils.e("+++++++++++++++++++++++++++++++++++level:"+level+"sumhp+"+sumhp+":le1:"+le1+":le2:"+le2+":le3:"+le3+":le4:"+le4+":le5:"+le5+":le6:"+le6+":le7:"+le7+":le8:"+le8+":le9:"+le9);
    }

    private  int  isduang = 0;
    /**获取第一条数据，开启动画或者别的操作*/
    public void startShows(){
//        for (int i = 0;i<ivs.size();i++){
//            ivs.get(i).setImageResource(R.mipmap.hei);
//        }
//        FragmentBreath2.ONE_OR_TWO =2;

//        initSound(pace);
        animation= AnimationUtils.loadAnimation(getActivity(), R.anim.scale0to1);
        animation.setDuration(60 * 1000 / pace / 2);
        caihongquan.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                LogUtils.e("执行中111111111111111111111");
//                if(activityBreath.guide_start_finish&&!activityBreath.guide_end_start){
//                    if(isduang == 0){
//                        activityBreath.updateData();
//                        isduang = 1;
//                    }
//                }
            }
        });
    }

    public void startan(){
        caihongquan.startAnimation(animation);
    }
    public ActivityBreath activityBreath;
//    private boolean breath=true;
//    private  void updateData(){
////        if(tv_start!=null)
////            tv_start.setText(breath ? "吸" : "呼");
//        if(activityBreath.guide_start_finish&&!activityBreath.guide_end_start){
//            AudioManager mgr = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
//            float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
//            mSoundPool.play(!breath ? soundPoolData.huID : soundPoolData.xiID, streamVolumeCurrent, streamVolumeCurrent, 1, 0, 1f);
//        }
//        breath = !breath;
//    }

    @Override
    public void initActionbar() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        getActivity().registerReceiver(myreceiver, filter);

        layout_actionbar.setBackgroundResource(R.color.bg_title);
        btn_left.setImageResource(R.mipmap.back_01);
        tv_title.setText(getString(R.string.chengji));
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        ivs.add(yuandian1);
        ivs.add(yuandian2);
        yuandian2.setRotation(10.0f);
        ivs.add(yuandian3);
        yuandian3.setRotation(20.0f);
        ivs.add(yuandian4);
        yuandian4.setRotation(30.0f);
        ivs.add(yuandian5);
        yuandian5.setRotation(40.0f);
        ivs.add(yuandian6);
        yuandian6.setRotation(50.0f);
        ivs.add(yuandian7);
        yuandian7.setRotation(60.0f);
        ivs.add(yuandian8);
        yuandian8.setRotation(70.0f);
        ivs.add(yuandian9);
        yuandian9.setRotation(80.0f);
        ivs.add(yuandian10);
        yuandian10.setRotation(90.0f);
        ivs.add(yuandian11);
        yuandian11.setRotation(100.0f);
        ivs.add(yuandian12);
        yuandian12.setRotation(110.0f);
        ivs.add(yuandian13);
        yuandian13.setRotation(120.0f);
        ivs.add(yuandian14);
        yuandian14.setRotation(130.0f);
        ivs.add(yuandian15);
        yuandian15.setRotation(140.0f);
        ivs.add(yuandian16);
        yuandian16.setRotation(150.0f);
        ivs.add(yuandian17);
        yuandian17.setRotation(160.0f);
        ivs.add(yuandian18);
        yuandian18.setRotation(170.0f);
        ivs.add(yuandian19);
        yuandian19.setRotation(180.0f);
        ivs.add(yuandian20);
        yuandian20.setRotation(190.0f);
        ivs.add(yuandian21);
        yuandian21.setRotation(200.0f);
        ivs.add(yuandian22);
        yuandian22.setRotation(210.0f);
        ivs.add(yuandian23);
        yuandian23.setRotation(220.0f);
        ivs.add(yuandian24);
        yuandian24.setRotation(230.0f);
        ivs.add(yuandian25);
        yuandian25.setRotation(240.0f);
        ivs.add(yuandian26);
        yuandian26.setRotation(250.0f);
        ivs.add(yuandian27);
        yuandian27.setRotation(260.0f);
        ivs.add(yuandian28);
        yuandian28.setRotation(270.0f);
        ivs.add(yuandian29);
        yuandian29.setRotation(280.0f);
        ivs.add(yuandian30);
        yuandian30.setRotation(290.0f);
        ivs.add(yuandian31);
        yuandian31.setRotation(300.0f);
        ivs.add(yuandian32);
        yuandian32.setRotation(310.0f);
        ivs.add(yuandian33);
        yuandian33.setRotation(320.0f);
        ivs.add(yuandian34);
        yuandian34.setRotation(330.0f);
        ivs.add(yuandian35);
        yuandian35.setRotation(340.0f);
        ivs.add(yuandian36);
        yuandian36.setRotation(350.0f);
        activityBreath = (ActivityBreath)getActivity();
    }

//    SoundPoolData soundPoolData=new SoundPoolData();
//    SoundPool mSoundPool;
//    private void initSound(int pace) {
//        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
//        soundPoolData.duration = 60 * 1000 / pace / 2;
//        LogUtils.e("-------------pace:" + pace);
//        switch (pace) {
//            case 3:
//                soundPoolData.huID = mSoundPool.load(getActivity(), R.raw.t_bpm31_down, 0);
//                soundPoolData.xiID = mSoundPool.load(getActivity(), R.raw.t_bpm31_up, 0);
//                break;
//            case 4:
//                soundPoolData.huID = mSoundPool.load(getActivity(), R.raw.t_bpm41_down, 0);
//                soundPoolData.xiID = mSoundPool.load(getActivity(), R.raw.t_bpm41_up, 0);
//                break;
//            case 5:
//                soundPoolData.huID = mSoundPool.load(getActivity(), R.raw.t_bpm51_down, 0);
//                soundPoolData.xiID = mSoundPool.load(getActivity(), R.raw.t_bpm51_up, 0);
//                break;
//            case 6:
//                soundPoolData.huID = mSoundPool.load(getActivity(), R.raw.t_bpm61_down, 0);
//                soundPoolData.xiID = mSoundPool.load(getActivity(), R.raw.t_bpm61_up, 0);
//                break;
//            case 7:
//                soundPoolData.huID = mSoundPool.load(getActivity(), R.raw.t_bpm71_down, 0);
//                soundPoolData.xiID = mSoundPool.load(getActivity(), R.raw.t_bpm71_up, 0);
//                break;
//            case 8:
//                soundPoolData.huID = mSoundPool.load(getActivity(), R.raw.t_bpm81_down, 0);
//                soundPoolData.xiID = mSoundPool.load(getActivity(), R.raw.t_bpm81_up, 0);
//                break;
//            case 9:
//                soundPoolData.huID = mSoundPool.load(getActivity(), R.raw.t_bpm91_down, 0);
//                soundPoolData.xiID = mSoundPool.load(getActivity(), R.raw.t_bpm91_up, 0);
//                break;
//        }
//    }

    class MyReceivers extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int cmd = intent.getIntExtra("cmd", 0);
            if(cmd == 1 ){
                HRVData data = (HRVData)intent.getSerializableExtra("data");
                setdian(data);
            }
            if(cmd == 2){
                pace = intent.getIntExtra("pace",1);
                startShows();
            }
            if(cmd == 3){
                ired = 0;
                iblue = 0;
                igreen = 0;
                num = 0;
                num = ired+iblue+igreen;
                tv_hrxl.setText("0BMP");
                tv_hp.setText("0");
            }
            if(cmd==4){
                int hr =  intent.getIntExtra("hr",0);
//                if(!hr.equals(null) && !hr.equals("null")){
                tv_hrxl.setText(hr+"BMP");
//                }
            }
            if(cmd == 5){
                String time = intent.getStringExtra("time");
                tv_mytime.setText(time);
            }
        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if(myreceiver!=null){
            getActivity().unregisterReceiver(myreceiver);
        }
    }

}

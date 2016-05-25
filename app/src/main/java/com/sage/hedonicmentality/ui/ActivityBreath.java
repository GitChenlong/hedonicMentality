package com.sage.hedonicmentality.ui;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.bean.SoundPoolData;
import com.sage.hedonicmentality.fragment.Me.FragmentMeSetting;
import com.sage.hedonicmentality.fragment.breath.FragmentBreath2;
import com.sage.hedonicmentality.fragment.breath.FragmentRainbowCircle;
import com.sage.hedonicmentality.service.ServiceBlueTooth;
import com.sage.hedonicmentality.utils.SPHelper;
import com.sage.hedonicmentality.view.MyViewpager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sage on 2015/7/28.
 */
public class ActivityBreath extends ActivityBase {
    private MyViewpager mPager;
    private ArrayList<Fragment> fragmentList;
    public int pace;//ÿ���Ӻ�����bpm
    public boolean breath=true;
//    private MediaPlayer mpBG;
    private MediaPlayer mpStart;
    private MediaPlayer mpEnd;
    public static boolean guide_start_finish=false;
    public static boolean guide_end_start=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e("++++onCreateaaa;+");
        if(savedInstanceState==null){
//           resetBreath();
            InitViewPager();
        }

    }
    FragmentBreath2 fragmentBreath2;
    FragmentRainbowCircle fragmentRainbowCircle;
    /*
        * ViewPager
        */
    public void InitViewPager(){
        mPager = (MyViewpager)findViewById(R.id.viewpager);
        fragmentList = new ArrayList<Fragment>();

        fragmentBreath2 = new FragmentBreath2();
        fragmentRainbowCircle = new FragmentRainbowCircle();
        fragmentList.add(fragmentBreath2);
        fragmentList.add(fragmentRainbowCircle);
        mPager.setOffscreenPageLimit(2);
        //��ViewPager
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);//
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                fragmentBreath2.showduang();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        pace= SPHelper.getDefaultInt(ActivityBreath.this, SPHelper.KEY_BREATH_PER_MIN, 5);
        initSound(pace);
//        diaoduang();
    }
//    public void distroyWebview(){
//
//    }

//    public void updateData(){
//        if(guide_start_finish&&!guide_end_start){
//            AudioManager mgr = (AudioManager)ActivityBreath.this.getSystemService(Context.AUDIO_SERVICE);
//            float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
//            mSoundPool.play(!breath ? soundPoolData.huID : soundPoolData.xiID, streamVolumeCurrent, streamVolumeCurrent, 1, 0, 1f);
//            HandlerThread thread=new HandlerThread("playData");
//            thread.start();
//            mSoundPool.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mSoundPool.start();
//                }
//            });
//            diaoduang();
//            new Handler(thread.getLooper()).postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        mSoundPool.setDataSource(ActivityBreath.this, Uri.parse(breath ? soundPoolData.huID : soundPoolData.xiID));
//                        mSoundPool.prepareAsync();
//                    } catch (IOException e) {
//                        mSoundPool.release();
//                        mSoundPool=null;
//                        e.printStackTrace();
//                    }
//                }
//            },100);
//        }
//    }
    public Thread thread;
    public int istrue = 0;
    public void  diaoduang(){
        if(thread!=null){
            return;
        }
         thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (istrue == 0 ){
                    try {
                        if(guide_start_finish&&!guide_end_start&&istrue==0){
                            mSoundPool.release();
                            mSoundPool = new MediaPlayer();
                            mSoundPool.setDataSource(ActivityBreath.this, Uri.parse(breath ? soundPoolData.huID : soundPoolData.xiID));
                            mSoundPool.prepareAsync();
                            mSoundPool.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    mSoundPool.start();
                                }
                            });
                            Thread.sleep(soundPoolData.duration);
                            breath = !breath;
                            LogUtils.e("++++breath+"+breath+"ss"+thread+"pace+:"+pace+"duration:"+soundPoolData.duration);
                            if(breath){
                                fragmentRainbowCircle.startan();
                            }else if(!breath){
                                fragmentBreath2.starlodurlp();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (Exception e){

                    }
                }
            }
        });
        thread.start();
    }


    SoundPoolData soundPoolData=new SoundPoolData();
    MediaPlayer mSoundPool = new MediaPlayer();
    private void initSound(int pace){
//        mSoundPool=new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundPoolData.duration=60*1000/pace/2;
        LogUtils.e("-------------pace:" + pace);
        switch (pace) {
            case 3:
                soundPoolData.huID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm31_down;
                soundPoolData.xiID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm31_up;
                break;
            case 4:
                soundPoolData.huID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm41_down;
                soundPoolData.xiID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm41_up;
                break;
            case 5:
                soundPoolData.huID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm51_down;
                soundPoolData.xiID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm51_up;
                break;
            case 6:
                soundPoolData.huID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm61_down;
                soundPoolData.xiID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm61_up;
                break;
            case 7:
                soundPoolData.huID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm71_down;
                soundPoolData.xiID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm71_up;
                break;
            case 8:
                soundPoolData.huID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm81_down;
                soundPoolData.xiID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm81_up;
                break;
            case 9:
                soundPoolData.huID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm91_down;
                soundPoolData.xiID = "android.resource://" + ActivityBreath.this.getPackageName()+ "/" + R.raw.t_bpm91_up;
                break;
        }

    }

    public void resetBreath(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new FragmentBreath2(),FragmentBreath2.class.getSimpleName())
                .commit();
    }


    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()==0){
//           FragmentBreath2 fragmentBreath2=(FragmentBreath2)getSupportFragmentManager().findFragmentById(R.id.container);
           FragmentBreath2 fragmentBreath2= (FragmentBreath2)fragmentList.get(0);
            if(fragmentBreath2!=null){
                fragmentBreath2.back();
            }else{
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

    public void playStart(){
        HandlerThread thread=new HandlerThread("playStart");
        thread.start();
        mpStart=new MediaPlayer();
        mpStart.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpStart.start();
            }
        });
        mpStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                guide_start_finish = true;
                mpStart.release();
                mpStart = null;
            }
        });
        new Handler(thread.getLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mpStart.setDataSource(ActivityBreath.this, Uri.parse("android.resource://" + ActivityBreath.this.getPackageName()
                            + "/" + R.raw.guide_start));
                    mpStart.prepareAsync();
                } catch (IOException e) {
                    mpStart.release();
                    mpStart=null;
                    e.printStackTrace();
                }
            }
        },100);
    }
    public void playEnd(){
        istrue = 1;
//        thread.stop();
        HandlerThread thread=new HandlerThread("playEnd");
        thread.start();
        mpEnd=new MediaPlayer();
        mpEnd.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpEnd.start();
            }
        });
        new Handler(thread.getLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mpEnd.setDataSource(ActivityBreath.this, Uri.parse("android.resource://" + ActivityBreath.this.getPackageName()
                            + "/" + R.raw.guide_end));
                    mpEnd.prepareAsync();
                } catch (IOException e) {
                    mpEnd.release();
                    mpEnd=null;
                    e.printStackTrace();
                }
            }
        },100);
    }
    public void releaseMP(){
//        if(mpBG!=null){
//            mpBG.release();
//            mpBG=null;
//        }
        if(mpStart!=null){
            mpStart.release();
            mpStart=null;
        }
        if(mpEnd!=null){
            mpEnd.release();
            mpEnd=null;
        }
    }

    @Override
    protected void onDestroy() {
        if(thread!=null){
            istrue = 1;
//            thread.stop();
        }
        guide_start_finish = false;
        guide_end_start = false;
        stopService(new Intent(this, ServiceBlueTooth.class));
        if(mSoundPool!=null){
//            mSoundPool.autoPause();
            mSoundPool.release();
            mSoundPool=null;
        }
        super.onDestroy();
    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter{
        ArrayList<Fragment> list;
        public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
            super(fm);
            this.list = list;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }
    }
}

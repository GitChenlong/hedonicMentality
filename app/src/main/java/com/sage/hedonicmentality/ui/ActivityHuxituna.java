package com.sage.hedonicmentality.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.ui.simple.BreathSetting;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/11/19.
 */
public class ActivityHuxituna extends ActivityBase {
    @Bind(R.id.layout_actionbar)
    RelativeLayout layout_actionbar;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.btn_left)
    ImageView btn_left;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout_actionbar.setBackgroundColor(getResources().getColor(R.color.dark_green));
        tv_title.setTextColor(getResources().getColor(R.color.whi));
        btn_left.setImageResource(R.mipmap.back_01);
        tv_title.setText(R.string.tuna_title);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_huxituna;
    }
    @OnClick({R.id.tv_ruhe,R.id.tv_starthuxi,R.id.iv_tunamusic,R.id.iv_tahuihuxi,R.id.btn_left})
    void set_click(View v) {
        switch (v.getId()) {
            case R.id.tv_ruhe:
                Intent intent = new Intent(ActivityHuxituna.this, ActivityAbdominalR.class);
                startActivity(intent);
                break;
            case R.id.tv_starthuxi:
                Intent intent1 = new Intent();
//                intent1.setAction(BreathSetting.ACTION);
//                this.sendBroadcast(intent1);
                setResult(RESULT_OK,intent1);
                finish();
                break;
            case R.id.iv_tunamusic:
                if(!hg) {
                    if (mpTG != null) {
                        mpTG.release();
                        mpTG = null;
                        tg = false;
                    }
                    hg = true;
                    playHG();
                }
                break;
            case R.id.iv_tahuihuxi:
                if(!tg){
                    if(mpHG!=null){
                        mpHG.release();
                        mpHG=null;
                        hg = false;
                    }
                    tg = true;
                    playTG();
                }
                break;
            case R.id.btn_left:
                    finish();
                break;
        }
    }
    private boolean hg = false;
    private MediaPlayer mpHG;
    private void playHG(){
        HandlerThread thread=new HandlerThread("playHUG");
        thread.start();
        mpHG=new MediaPlayer();
        mpHG.setLooping(true);
        mpHG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpHG.start();
            }
        });
        new Handler(thread.getLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mpHG.setDataSource(ActivityHuxituna.this, Uri.parse("android.resource://" +ActivityHuxituna.this.getPackageName()
                            + "/" + R.raw.tunasuyuan));
                    mpHG.prepareAsync();
                } catch (IOException e) {
                    mpHG.release();
                    e.printStackTrace();
                }
            }
        }, 100);
    }
    private boolean tg = false;
    private MediaPlayer mpTG;
    private void playTG(){
        HandlerThread thread=new HandlerThread("playTUG");
        thread.start();
        mpTG=new MediaPlayer();
        mpTG.setLooping(true);
        mpTG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpTG.start();
            }
        });
        new Handler(thread.getLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mpTG.setDataSource(ActivityHuxituna.this, Uri.parse("android.resource://" +ActivityHuxituna.this.getPackageName()
                            + "/" + R.raw.tahuihuxi));
                    mpTG.prepareAsync();
                } catch (IOException e) {
                    mpTG.release();
                    e.printStackTrace();
                }
            }
        }, 100);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mpHG!=null){
            mpHG.release();
            mpHG=null;
        }
        if(mpTG!=null){
            mpTG.release();
            mpTG=null;
        }
    }
}

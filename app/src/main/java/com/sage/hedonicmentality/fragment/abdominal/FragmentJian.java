package com.sage.hedonicmentality.fragment.abdominal;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sage.hedonicmentality.R;

import java.io.IOException;

/**
 * Created by Administrator on 2015/11/3.
 */
public class FragmentJian extends Fragment{
    private ImageView iv_jiany;
    private boolean jg=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragmentjian,null);
        iv_jiany = (ImageView)v.findViewById(R.id.iv_jiany);
        iv_jiany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!jg){
                    playJG();
                    jg=true;
                }
            }
        });
        return v;
    }

    private MediaPlayer mpJG;
    private void playJG(){
        HandlerThread thread=new HandlerThread("playJG");
        thread.start();
        mpJG=new MediaPlayer();
        mpJG.setLooping(false);
        mpJG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpJG.start();
            }
        });
        mpJG.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                jg = false;
            }
        });
        new Handler(thread.getLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mpJG.setDataSource(getActivity(), Uri.parse("android.resource://" + getActivity().getPackageName()
                            + "/" + R.raw.jianyi));
                    mpJG.prepareAsync();
                } catch (IOException e) {
                    mpJG.release();
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mpJG!=null){
            mpJG.release();
            mpJG=null;
            jg = false;
        }
    }
}

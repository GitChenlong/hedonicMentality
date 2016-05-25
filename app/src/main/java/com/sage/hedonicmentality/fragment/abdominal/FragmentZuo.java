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
public class FragmentZuo extends Fragment{
    private ImageView iv_zuoy;
    private boolean zg = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragmentzuo,null);
        iv_zuoy = (ImageView)v.findViewById(R.id.iv_zuoy);
        iv_zuoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!zg){
                    playZG();
                    zg=true;
                }
            }
        });
        return v;

    }

    private MediaPlayer mpZG;
    private void playZG(){
        HandlerThread thread=new HandlerThread("playZG");
        thread.start();
        mpZG=new MediaPlayer();
        mpZG.setLooping(false);
        mpZG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpZG.start();
            }
        });
        mpZG.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                zg = false;
            }
        });
        new Handler(thread.getLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mpZG.setDataSource(getActivity(), Uri.parse("android.resource://" + getActivity().getPackageName()
                            + "/" + R.raw.zuofa));
                    mpZG.prepareAsync();
                } catch (IOException e) {
                    mpZG.release();
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mpZG!=null){
            mpZG.release();
            mpZG=null;
            zg = false;
        }
    }
}

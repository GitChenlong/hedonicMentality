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
public class FragmentYuan extends Fragment{
    private ImageView iv_yuany;
    private boolean yg = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragmentyuan,null);
        iv_yuany = (ImageView)v.findViewById(R.id.iv_yuany);
        iv_yuany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!yg){
                    playYG();
                    yg = true;
                }
            }
        });
        return v;
    }
    private MediaPlayer mpYG;
    private void playYG(){
        HandlerThread thread=new HandlerThread("playYG");
        thread.start();
        mpYG=new MediaPlayer();
        mpYG.setLooping(false);
        mpYG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpYG.start();
            }
        });
        mpYG.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                yg = false;
            }
        });
        new Handler(thread.getLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mpYG.setDataSource(getActivity(), Uri.parse("android.resource://" + getActivity().getPackageName()
                            + "/" + R.raw.yuanli));
                    mpYG.prepareAsync();
                } catch (IOException e) {
                    mpYG.release();
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mpYG!=null){
            mpYG.release();
            mpYG=null;
            yg = false;
        }
    }
}

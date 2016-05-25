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
public class FragmentHao extends Fragment{
    private ImageView iv_haoy;
    private boolean hg = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.fragmenthao,null);
        iv_haoy = (ImageView)v.findViewById(R.id.iv_haoy);
        iv_haoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hg){
                    playHG();
                    hg = true;
                }
            }
        });
        return v;

    }
    private MediaPlayer mpHG;
    private void playHG(){
        HandlerThread thread=new HandlerThread("playYG");
        thread.start();
        mpHG=new MediaPlayer();
        mpHG.setLooping(false);
        mpHG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpHG.start();
            }
        });
        mpHG.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                hg= false;
            }
        });
        new Handler(thread.getLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mpHG.setDataSource(getActivity(), Uri.parse("android.resource://" + getActivity().getPackageName()
                            + "/" + R.raw.haochu));
                    mpHG.prepareAsync();
                } catch (IOException e) {
                    mpHG.release();
                    mpHG = null;
                    e.printStackTrace();
                }
            }
        }, 100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mpHG!=null){
            mpHG.release();
            mpHG=null;
            hg = false;
        }
    }
}

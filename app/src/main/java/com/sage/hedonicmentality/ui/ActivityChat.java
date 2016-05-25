package com.sage.hedonicmentality.ui;

import android.os.Bundle;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.chat.FragmentChatMain;

/**
 * Created by Sage on 2015/8/4.
 */
public class ActivityChat extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentChatMain(),FragmentChatMain.class.getSimpleName()).commit();
            setBackgroundColor(getResources().getColor(R.color.bg_chat_main));
        }
    }
}

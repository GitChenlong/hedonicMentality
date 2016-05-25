package com.sage.hedonicmentality.ui;

import android.os.Bundle;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.worry.FragmentWorry;

/**
 * Created by Sage on 2015/8/4.
 */
public class ActivityWorry extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentWorry(),FragmentWorry.class.getSimpleName()).commit();
            setBackground(R.drawable.u18);
        }
    }

}

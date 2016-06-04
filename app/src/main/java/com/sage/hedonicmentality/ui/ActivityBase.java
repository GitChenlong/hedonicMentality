package com.sage.hedonicmentality.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.app.Common;

import butterknife.ButterKnife;

/**
 * Created by Sage on 2015/7/17.
 */
public class ActivityBase extends AppCompatActivity {
//    @Bind(R.id.id_toolbar)
//    public Toolbar mToolbar;
//    @Bind(R.id.tv_title)
//    public TextView tv_title;

    public int getLayout(){
        return 0;
    }

    public boolean registerLogin(){
        return true;
    }
    public Boolean stateBarTransParent(){
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout() == 0 ? R.layout.activity_base : getLayout());
        ButterKnife.bind(this);
//        if(mToolbar!=null){
//            setSupportActionBar(mToolbar);
//        }
        if(registerLogin()){
            receiver=new LoginOutBroadcast();
            IntentFilter filter=new IntentFilter(Common.ACTION_LOGIN_OUT);
            registerReceiver(receiver,filter);
        }
        if(stateBarTransParent()){
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
            tintManager.setNavigationBarTintEnabled(true);
            //tintManager.setStatusBarAlpha(0.1f);
            //tintManager.setStatusBarTintColor(Color.parseColor("#000000"));
            tintManager.setTintColor(Color.parseColor("#77ff0000"));
        }


    }

    public void setBackground(int resID){
        FrameLayout layout= (FrameLayout) findViewById(R.id.container);
        if(layout!=null){
            layout.setBackgroundResource(resID);
        }
    }
    public void setBackgroundColor(int color){
        FrameLayout layout= (FrameLayout) findViewById(R.id.container);
        if(layout!=null){
            layout.setBackgroundColor(color);
        }
    }
//    public void setTitle(String title){
//        tv_title.setText(title);
//    }
//    public void setTitle(int title){
//        tv_title.setText(title);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==android.R.id.home){
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


//    public void updateActionbar(int title,boolean show_home){
//        getSupportActionBar().setDisplayHomeAsUpEnabled(show_home);
//        invalidateOptionsMenu();
//        setTitle(title);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
    }

    private LoginOutBroadcast receiver;
    public class LoginOutBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Common.ACTION_LOGIN_OUT)){
                finish();
            }
        }
    }
}

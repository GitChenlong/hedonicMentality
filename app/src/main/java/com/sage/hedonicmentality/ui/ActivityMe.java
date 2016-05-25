package com.sage.hedonicmentality.ui;

import android.os.Bundle;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.Me.FragmentAddressme;
import com.sage.hedonicmentality.fragment.Me.FragmentAdvice;
import com.sage.hedonicmentality.fragment.Me.FragmentBirthdayme;
import com.sage.hedonicmentality.fragment.Me.FragmentEducation;
import com.sage.hedonicmentality.fragment.Me.FragmentHeight;
import com.sage.hedonicmentality.fragment.Me.FragmentMNA;
import com.sage.hedonicmentality.fragment.Me.FragmentMSG;
import com.sage.hedonicmentality.fragment.Me.FragmentMe;
import com.sage.hedonicmentality.fragment.Me.FragmentMeSetting;
import com.sage.hedonicmentality.fragment.Me.FragmentMyHM;
import com.sage.hedonicmentality.fragment.Me.FragmentMyHMCurve;
import com.sage.hedonicmentality.fragment.Me.FragmentRecord;
import com.sage.hedonicmentality.fragment.Me.FragmentSecret;
import com.sage.hedonicmentality.fragment.Me.FragmentSexme;
import com.sage.hedonicmentality.fragment.Me.FragmentSystem;
import com.sage.hedonicmentality.fragment.Me.FragmentUpName;
import com.sage.hedonicmentality.fragment.Me.FragmentUpdatepwd;
import com.sage.hedonicmentality.fragment.Me.FragmentWeight;

/**
 * Created by Sage on 2015/7/17.
 */
public class ActivityMe extends ActivityBase {

    @Override
    public int getLayout() {
        return R.layout.activity_me;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentMe(),FragmentMe.TAG).commit();
        }
    }

    public void changePage(int i){
        switch (i){
            case 1:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentMeSetting(),FragmentMeSetting.class.getSimpleName())
                        .addToBackStack("FragmentMeSetting").commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentRecord(),FragmentRecord.class.getSimpleName())
                        .addToBackStack("FragmentRecord").commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container, new FragmentSecret(), FragmentSecret.class.getSimpleName())
                        .addToBackStack("FragmentSecret").commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentMSG(),FragmentMSG.class.getSimpleName())
                        .addToBackStack("FragmentMSG").commit();

                break;
            case 5:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentAdvice(),FragmentAdvice.class.getSimpleName())
                        .addToBackStack("FragmentAdvice").commit();
                break;
            case 6:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentSystem(),FragmentSystem.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 7:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentUpdatepwd(),FragmentUpdatepwd.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 8:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentUpName(),FragmentUpName.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 9:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentMyHM(),FragmentMyHM.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 11:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentMNA(),FragmentMNA.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 12:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentEducation(),FragmentEducation.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 13:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentAddressme(),FragmentAddressme.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 14:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentBirthdayme(),FragmentBirthdayme.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 15:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentSexme(),FragmentSexme.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 16:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentMyHMCurve(),FragmentMyHMCurve.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 17:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentHeight(),FragmentHeight.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
            case 20:
                getSupportFragmentManager().beginTransaction().
                        add(R.id.container,new FragmentWeight(),FragmentWeight.class.getSimpleName())
                        .addToBackStack("FragmentSystem").commit();
                break;
        }
    }
    public void setmeName(){

        FragmentMeSetting setting= (FragmentMeSetting) getSupportFragmentManager().findFragmentByTag(FragmentMeSetting.class.getSimpleName());
        setting.setName();
    }
    public void seteducation(){
        FragmentMeSetting setting= (FragmentMeSetting) getSupportFragmentManager().findFragmentByTag(FragmentMeSetting.class.getSimpleName());
        setting.setEduca();
    }
    public void setaddress(){
        FragmentMeSetting setting= (FragmentMeSetting) getSupportFragmentManager().findFragmentByTag(FragmentMeSetting.class.getSimpleName());
        setting.setaddress();
    }
    public void setbirthday(){
        FragmentMeSetting setting= (FragmentMeSetting) getSupportFragmentManager().findFragmentByTag(FragmentMeSetting.class.getSimpleName());
        setting.setbirth();
    }
    public void setSex(){
        FragmentMeSetting setting= (FragmentMeSetting) getSupportFragmentManager().findFragmentByTag(FragmentMeSetting.class.getSimpleName());
        setting.setSex();
    }
    public void setNamePhone(){
        FragmentMe setting= (FragmentMe) getSupportFragmentManager().findFragmentByTag(FragmentMe.class.getSimpleName());
        if(setting!=null)
            setting.setNameP();
    }
    public void setHegit(){
        FragmentMeSetting setting= (FragmentMeSetting) getSupportFragmentManager().findFragmentByTag(FragmentMeSetting.class.getSimpleName());
        setting.setHegit();
    }
    public void setWeight(){
        FragmentMeSetting setting= (FragmentMeSetting) getSupportFragmentManager().findFragmentByTag(FragmentMeSetting.class.getSimpleName());
        setting.setWeight();
    }
}

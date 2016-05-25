package com.sage.hedonicmentality.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.fragment.account.FragmentAddress;
import com.sage.hedonicmentality.fragment.account.FragmentAgreement;
import com.sage.hedonicmentality.fragment.account.FragmentBirthday;
import com.sage.hedonicmentality.fragment.account.FragmentForget;
import com.sage.hedonicmentality.fragment.account.FragmentInfo;
import com.sage.hedonicmentality.fragment.account.FragmentLogin;
import com.sage.hedonicmentality.fragment.account.FragmentRegister;
import com.sage.hedonicmentality.fragment.account.FragmentSex;

public class ActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container_login,new FragmentLogin(),FragmentLogin.class.getSimpleName())
                    .commit();
        }

    }
    public void GoRegister(){
        getSupportFragmentManager().beginTransaction().
                add(R.id.container_login,new FragmentRegister(),FragmentRegister.class.getSimpleName())
                .addToBackStack(FragmentRegister.class.getSimpleName()).commit();

    }
    public void GoForgetPsw(){
        getSupportFragmentManager().beginTransaction().
                add(R.id.container_login,new FragmentForget(),FragmentForget.class.getSimpleName())
                .addToBackStack(FragmentForget.class.getSimpleName()).commit();

    }
    public void GoAddInfo(){
        int count=getSupportFragmentManager().getBackStackEntryCount();
        for(int i=0;i<count;i++){
            getSupportFragmentManager().popBackStackImmediate();
        }
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container_login, new FragmentInfo(), FragmentInfo.class.getSimpleName())
                .commit();

    }
    public void GoSex(){
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container_login, new FragmentSex(), FragmentSex.class.getSimpleName())
                .commit();
    }
    public void GoAgreement(){
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container_login, new FragmentAgreement(), FragmentAgreement.class.getSimpleName())
                .commit();
    }
    public void GoBirthday(){
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container_login, new FragmentBirthday(), FragmentBirthday.class.getSimpleName())
                .commit();
    }
    public void GoAddress(){
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container_login, new FragmentAddress(), FragmentAddress.class.getSimpleName())
                .commit();
    }

}

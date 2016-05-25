package com.sage.hedonicmentality.entity;

import android.content.Context;
import android.text.TextUtils;

import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.utils.Util;

/**
 * Created by Sage on 2015/7/16.
 */
public class RegisterParams {
    public String number;
    public String code;
    public String psw;
    public boolean isCheck;
    public String check(Context context){
        if(TextUtils.isEmpty(number)){
            return context.getString(R.string.phoneNull);
        }
        if(!Util.isMobileNO(number)){
            return context.getString(R.string.phoneWrong);
        }
        if(TextUtils.isEmpty(code)){
            return context.getString(R.string.codeNull);
        }
        if(code.length()!=6){
            return context.getString(R.string.codeLengthWrong);
        }
        if(TextUtils.isEmpty(psw)){
            return context.getString(R.string.pswNull);
        }
        if(psw.length()<6){
            return context.getString(R.string.pswLengthWrong);
        }

//        if(!isCheck){
//            return context.getString(R.string.mustAgreeDeal);
//        }
        return null;
    }

    /**忘记密码那里的参数就3个*/
    public String check_forget(Context context){
        if(TextUtils.isEmpty(number)){
            return context.getString(R.string.phoneNull);
        }
        if(!Util.isMobileNO(number)){
            return context.getString(R.string.phoneWrong);
        }
        if(TextUtils.isEmpty(code)){
            return context.getString(R.string.codeNull);
        }
        if(code.length()!=6){
            return context.getString(R.string.codeLengthWrong);
        }
        if(TextUtils.isEmpty(psw)){
            return context.getString(R.string.pswNull);
        }
        if(psw.length()<6){
            return context.getString(R.string.pswLengthWrong);
        }

        return null;
    }




}

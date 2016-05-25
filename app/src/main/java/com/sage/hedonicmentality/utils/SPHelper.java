package com.sage.hedonicmentality.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sage on 2015/8/6.
 */
public class SPHelper {

    public static final String NAME_DEFAULT="default";
    public static final String KEY_IS_FIRST_LOGIN="is_first_login";/**是否第一次登陆，主要是用于引导页的显示与否*/
    public static final String KEY_HAVE_LOGIN="have_login";/**是否登陆成功，主要是用于跳转到主页面还是登陆页面*/
    public static final String KEY_LAST_VERSION="last_version";/**上次登陆的版本号，如果和本次不同，则弹出引导页*/

    public static final String KEY_FIRST_CAMERA="first_camera";/**第一次使用摄像头，给予使用提示*/
    public static final String KEY_DURATION="breath_duration";/**练习时间 5到30*/
    public static final String KEY_DURATION_item="breath_duration_item";/**练习时间索引*/
    public static final String  KEY_BREATH_PER_MIN="BPM";/**每分钟呼吸数3到9*/
    public static final String  KEY_BREATH_PER_MIN_ITEM="BPM_ITRM";/**每分钟呼吸数的索引*/
    public static final String KEY_DEVICE="device";/**设备类型，摄像头的话为0，外设的话为1*/

    public static final String KEY_USER_PHONE="user_phone";/**用户登陆的手机号*/

    public static final String KEY_UUID="device_uuid";/**手机唯一设备号*/

    public static final String KEY_SCENE_HTML="scene_html";/**获得html名字*/
    public static final String KEY_SCENE_NAME="scene_name";/**获得名字*/
    public static final String KEY_SCENE_JS="scene_jsname";/**获得js方法名字名字*/
    public static final String KEY_SCENE_JSHX="scene_jshx";/**获得呼吸频率的js方法*/


    public static SharedPreferences getDefaultSP(Context context){
        return context.getSharedPreferences(NAME_DEFAULT,Context.MODE_PRIVATE);
    }

    public static void putDefaultString(Context context,String key,String value){
        SharedPreferences sp=getDefaultSP(context);
        sp.edit().putString(key,value).commit();
    }
    public static void putDefaultBoolean(Context context,String key,Boolean value){
        SharedPreferences sp=getDefaultSP(context);
        sp.edit().putBoolean(key, value).commit();
    }
    public static void putDefaultInt(Context context,String key,int value){
        SharedPreferences sp=getDefaultSP(context);
        sp.edit().putInt(key, value).commit();
    }




    public static  String getDefaultString(Context context,String key,String defValue){
        SharedPreferences sp=getDefaultSP(context);
       return  sp.getString(key, defValue);
    }
    public static  boolean getDefaultBoolean(Context context,String key,Boolean defValue){
        SharedPreferences sp=getDefaultSP(context);
       return  sp.getBoolean(key,defValue);
    }

    public static  int getDefaultInt(Context context,String key,int defValue){
        SharedPreferences sp=getDefaultSP(context);
       return  sp.getInt(key,defValue);
    }



}

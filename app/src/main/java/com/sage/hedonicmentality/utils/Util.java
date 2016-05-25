package com.sage.hedonicmentality.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.sage.hedonicmentality.R;
import com.sage.hedonicmentality.view.LoadingDiaLog;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Sage on 2015/7/16.
 */
public class Util {
    private static LoadingDiaLog diaLog;
    public static void hiddenInputMethod(Activity activity,View editText){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

//    public static boolean isPhone(String number){
//        return !TextUtils.isEmpty(number) && number.length() == 11 && android.util.Patterns.PHONE.matcher(number).matches();
//    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        if (TextUtils.isEmpty(mobiles))
//            return false;
//        else
//            return mobiles.matches(telRegex);
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    public static  CountDownTimer timer(final TextView tv_code,int time){
        final String original_show=tv_code.getText().toString();
        CountDownTimer timer=new CountDownTimer(time*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_code.setClickable(false);
                tv_code.setText((int) (millisUntilFinished/1000)+" s");
            }

            @Override
            public void onFinish() {
                tv_code.setText(original_show);
                tv_code.setClickable(true);

            }
        };
        timer.start();
        return timer;
    }

    public static void showToast(Context context,String msg){
        if(context==null){
            return;
        }
        Toast.makeText(context,""+msg,Toast.LENGTH_SHORT).show();
    }

    public static DbUtils getDbUtils(Context context){
        SharedPreferencesHelper.getInstance().Builder(context);
        return DbUtils.create(context, SharedPreferencesHelper.getInstance().getString(Contact.SH_ID) + "hrv.db", 2, new DbUtils.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbUtils dbUtils, int i, int i1) {
                try {
                   LogUtils.i("old=" + i + " new=" + i1);
                   if(i1>i&&i==1)
                    dbUtils.dropDb();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void install(File file,Context context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void showProgressFor(Context context,String string) {
        if(diaLog!=null&&diaLog.isShowing()){
            diaLog.dismiss();
            diaLog=null;
        }
        diaLog = new LoadingDiaLog(context,string);
        diaLog.setCanceledOnTouchOutside(false);
        diaLog.show();
    }
    public static void cancelProgressFor(Context context) {
        diaLog.dismiss();
    }

    public static int[] getTime(){
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int minute = time.minute;
        int hour = time.hour;
        int sec = time.second;
        int [] a = new int[20];
        a[0] = year;
        a[1] = month;
        a[2] = day;
        a[3] = minute;
        a[4] = hour;
        a[5] = sec;
        return a;
    }

    public static void createDirectory(){
        File file = new File(Environment.getExternalStorageDirectory()+"/"+"HRVHTML");
        if(file.exists()){
            if(!file.isDirectory()){
                file.mkdir();
            }
        }else
        if(!file.exists()){
            file.mkdir();
        }
    }

    /**获得当前Activity的截图*/
    public static Bitmap myShot(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;
        Display display = activity.getWindowManager().getDefaultDisplay();

        // 获取屏幕宽和高
        int widths = display.getWidth();
        int heights = display.getHeight();

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                statusBarHeights, widths, heights - statusBarHeights);

        // 销毁缓存信息
        view.destroyDrawingCache();

        return bmp;
    }
}

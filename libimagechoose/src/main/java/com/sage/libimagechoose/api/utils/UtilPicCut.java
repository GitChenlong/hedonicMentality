package com.sage.libimagechoose.api.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Sage on 2015/7/27.
 */
public class UtilPicCut {
    private static final String CROP_ACTION = "com.android.camera.action.CROP";
    public static final int REQUEST_DO_CROP = 778;
    public static final String temp_crop_dir=Environment.getExternalStorageDirectory().getAbsolutePath()+"/temp_crop_images";
    public static File doCropAction(Activity activity, File file,
                                    boolean sameAspect,int outputX,int outputY,File dstFile) {
        if(dstFile==null){
            return null;
        }
        Intent intent = new Intent(CROP_ACTION);
        intent.setType("image/*");
        List<ResolveInfo> list = activity.getApplicationContext()
                .getPackageManager().queryIntentActivities(intent, 0);
        intent.setData(Uri.fromFile(file));
        intent.putExtra("crop", "true");
        if (sameAspect) {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("scaleUpIfNeeded", true);// source code
        intent.putExtra("max-width", 300);
        intent.putExtra("max-height", 300);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(dstFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        if (list.size() > 0) {
            ResolveInfo res = list.get(0);
            intent.setComponent(new ComponentName(res.activityInfo.packageName,
                    res.activityInfo.name));
            activity.startActivityForResult(intent, REQUEST_DO_CROP);
        } else {
            Toast.makeText(activity.getApplicationContext(),
                    "没有找到裁剪程序", Toast.LENGTH_SHORT).show();
            return null;
        }
        return dstFile;
    }
    public static File doCropAction(Activity activity, File file,
                                    boolean sameAspect) {
        File dstFile = getTempFile();
        if (dstFile == null) {
            Toast.makeText(activity.getApplicationContext(),
                    "sd卡不存在", Toast.LENGTH_SHORT).show();
            return null;
        }
        return  doCropAction(activity, file, sameAspect, 200, 200,dstFile);
    }
    public static File doCropAction(Fragment fragment, File file,
                                    boolean sameAspect,int outputX,int outputY,File dstFile) {
        if(dstFile==null){
            return null;
        }
        Intent intent = new Intent(CROP_ACTION);
        intent.setType("image/*");
        List<ResolveInfo> list = fragment.getActivity().getApplicationContext()
                .getPackageManager().queryIntentActivities(intent, 0);
        intent.setData(Uri.fromFile(file));
        intent.putExtra("crop", "true");
        if (sameAspect) {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("scaleUpIfNeeded", true);// source code
        intent.putExtra("max-width", 300);
        intent.putExtra("max-height", 300);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(dstFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        if (list.size() > 0) {
            ResolveInfo res = list.get(0);
            intent.setComponent(new ComponentName(res.activityInfo.packageName,
                    res.activityInfo.name));
            fragment.startActivityForResult(intent, REQUEST_DO_CROP);
        } else {
            Toast.makeText(fragment.getActivity().getApplicationContext(),
                    "没有找到裁剪程序", Toast.LENGTH_SHORT).show();
            return null;
        }
        return dstFile;
    }
    public static File doCropAction(Fragment fragment, File file,
                                    boolean sameAspect) {
        File dstFile = getTempFile();
        if (dstFile == null) {
            Toast.makeText(fragment.getActivity().getApplicationContext(),
                    "sd卡不存在", Toast.LENGTH_SHORT).show();
            return null;
        }
        return  doCropAction(fragment, file, sameAspect, 200, 200,dstFile);
    }

    private static boolean isSDCARDMounted() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
    private static File getTempFile() {
        if (isSDCARDMounted()) {
            File Dic = new File(temp_crop_dir);
            if (!Dic.exists()){
               if(!Dic.mkdirs()){
                   return null;
               }

            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS")
                    .format(new Date());
            String imageFileName = timeStamp + "_.jpg";
            return new File(Dic.getAbsolutePath(), imageFileName);
        }
        return null;
    }

}

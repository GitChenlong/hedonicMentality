package com.sage.hedonicmentality.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	/**
	 * 获取现在日期
	 * 
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

    /**
     * 获取现在时间精确到秒
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateSecond() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 字符串格式 yyyy-MM-dd HH:mm:ss转换为毫秒格式
     * @param time 字符串格式 yyyy-MM-dd HH:mm:ss
     * @return 传入时间对应的时间戳，若时间格式错误则返回-1.
     */
    public static long getTimeMill(String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(time);
            return date.getTime();
        } catch (Exception e) {
            return -1;
        }
    }

	/**
	 * 
	 * @return返回字符串格式 yyyy-MM-dd
	 */
	public static String getStringNowDate(long time) {
		Date currentTime = new Date(time*1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取年份
	 * 
	 * @return返回字符串格式 yyyy
	 */
	public static String getStringNowYear(long time) {
		Date currentTime = new Date(time*1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	/**
	 * 获取月份
	 *
	 * @return返回字符串格式 MM
	 */
	public static String getStringNowMin(long time) {
		Date currentTime = new Date(time*1000);
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取照片名字,以时间为名
	 * 
	 * @return返回字符串格式 yyyyMMddHHmmss
	 */
	public static String getPictureDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取显示用途时间
	 * 
	 * @return返回字符串格式 0:00:00
	 */
	public static String getTimeForShow(int time) {
		int totalSec = 0;
		int hour = 0;
		int min = 0;
		int sec = 0;
		totalSec = time;
		min = (totalSec / 60);
		sec = (totalSec % 60);
		if (min >= 60) {
			hour = min / 60;
			min = min % 60;
		}
		String time_string = String.format("%1$01d:%2$02d:%3$02d", hour, min,
                sec);
		return time_string;
	}
}

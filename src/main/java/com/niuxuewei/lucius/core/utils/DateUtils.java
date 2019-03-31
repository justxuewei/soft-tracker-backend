package com.niuxuewei.lucius.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 为传入的date增加n月
     */
    public static Date addMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();
    }

    /**
     * 为传入的date增加n天
     */
    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 为传入的date增加n分钟
     */
    public static Date addMinute(Date date, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, min);
        return calendar.getTime();
    }

    /**
     * 为传入的date增加n秒
     */
    public static Date addSecond(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 格式化时间
     * @param format 格式化格式
     *               yyyy 年, MM 月, dd 日
     *               hh 1~12小时制(1-12), HH 24小时制(0-23), mm 分, ss 秒
     *               更多请参考https://blog.csdn.net/qq_27093465/article/details/53034427
     * @return 格式化的字符串
     */
    public static String formatData(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}

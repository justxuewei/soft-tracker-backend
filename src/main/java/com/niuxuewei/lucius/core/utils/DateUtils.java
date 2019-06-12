package com.niuxuewei.lucius.core.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.util.ISO8601DateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Slf4j
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
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取两天之间相差了多少天
     */
    public static long getDifferenceDays(Date before, Date behind) {
        long diff = behind.getTime() - before.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    /**
     * 格式化ISO 8601格式字符串
     * 例如: 2019-03-14T12:53:52.000Z
     */
    public static Date parseISO8601(String dateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            log.error("解析ISO8601字符串失败，原因: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 判断一个日期是否在两个日期之间
     * @param since 启示
     * @param until 结束
     * @param date 需要判断的日期
     * @return 如果是则返回true，否则false
     */
    public static boolean isBetween(Date since, Date until, Date date) {
        return date.getTime() >= since.getTime() && date.getTime() <= until.getTime();
    }

}

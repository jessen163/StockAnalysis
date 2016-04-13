package com.ryd.stockanalysis.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.util
 * 创建人：songby
 * 创建时间：2016/4/13 9:59
 */
public class DateUtils {

    public final static String DATE_FORMAT ="yyyy-MM-dd";
    public final static String TIME_FORMAT= "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_FORMAT= "yyyy-MM-dd HH:mm";

    /**
     * 得到按指定格式的系统当前时间
     *
     * @return 格式化的日期字符串
     */
    public static String getSysDate(String dateFormat) {
        if (dateFormat == null || "".equals(dateFormat)) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String dateStr = sdf.format(date.getTime());
        return dateStr;
    }

    /**
     * 功能：日期格式成字符串
     *
     * @param date      Date 日期
     * @param strFormat String 日期格式化类型
     * @return String
     */
    public static String formatDateToStr(Date date, String strFormat) {
        if (date == null)
            return null;
        SimpleDateFormat format1 = new SimpleDateFormat(strFormat);
        String str = null;
        try {
            str = format1.format(date);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return str;
    }

    /**
     * 功能：日期格式成字符串
     *
     * @param timestamp      Timestamp 日期
     * @return String
     */
    public static String formatTimestampToStr(Timestamp timestamp) {
        if (timestamp == null)
            return null;
        SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT);
        String str = null;
        try {
            str = format1.format(timestamp);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return str;
    }

    /**
     * 功能：字符串转化成日期
     *
     * @param dateStr   String 日期字符串
     * @param strFormat String 日期格式化类型
     * @return 如果返回为null，则说明字符串格式不对，或者为空
     */
    public static Date formatStrToDate(String dateStr, String strFormat) {
        if (dateStr == null)
            return null;
        SimpleDateFormat format1 = new SimpleDateFormat(strFormat);
        Date date = null;
        try {
            date = format1.parse(dateStr);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return date;
    }

    /**
     * 功能：日期格式成字符串
     *
     * @param date      Long 日期
     * @param strFormat String 日期格式化类型
     * @return String
     */
    public static String formatLongToStr(Long date, String strFormat) {
        if (date == null)
            return null;
        SimpleDateFormat format1 = new SimpleDateFormat(strFormat);
        String str = null;
        try {
            str = format1.format(date);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return str;
    }
    /**
     * 获取周号
     * @param date
     * @return
     */
    public static int getDayOfWeekNum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDayOfWeekStr(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekNum = cal.get(Calendar.DAY_OF_WEEK);
        String rStr = "日";
        if(weekNum==2) rStr ="一";
        if(weekNum==3) rStr ="二";
        if(weekNum==4) rStr ="三";
        if(weekNum==5) rStr ="四";
        if(weekNum==6) rStr ="五";
        if(weekNum==7) rStr ="六";
        return rStr;
    }

    public static int getMonthNum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getYearNum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getDayOfMonthNum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * nums天以前
     * @param nums
     * @return
     */
    public static Date getNumsDaysAgo(Integer nums) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1 * nums);

        return cal.getTime();
    }

    public static Integer getHour() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * nums天以前
     * @param nums
     * @return
     */
    public static Date getNumsDaysAgo(Date date, Integer nums) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, -1 * nums);

        return cal.getTime();
    }

    /**
     * nums天以前---到23:59:59
     * @param nums
     * @return
     */
    public static Long getNumsDaysStartAgo(Integer nums) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1*nums);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime().getTime();
    }

    /**
     * nums天以前---到23:59:59
     * @param nums
     * @return
     */
    public static Long getNumsDaysEndAgo(Integer nums) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1*nums);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime().getTime();
    }

    /**
     * nums天以前(返回值：Date)---到0:0:0
     * @param nums
     * @return
     */
    public static Date getNumsAgoStartDate(Integer nums) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1*nums);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    /**
     * nums天以前(返回值：Date)---到23:59:59
     * @param nums
     * @return
     */
    public static Date getNumsAgoEndDate(Integer nums) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1*nums);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }


    /**
     * 获得每天固定时间
     * @param hourStr
     * @return
     */
    public static Date getSetHourTime(String hourStr){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);//每天

        String[] strarr = hourStr.split(":");

        calendar.set(year, month, day, Integer.parseInt(strarr[0]), Integer.parseInt(strarr[1]), Integer.parseInt(strarr[2]));

        return calendar.getTime();
    }
    /**
     * nums小时以前
     * @param nums
     * @return
     */
    public static Date getNumsHoursAgo(Integer nums) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -1 * nums);

        return cal.getTime();
    }

    /**
     *
     * @param date
     * @return
     */
    public static int getDaysFromDate(Date date) {
        long times = System.currentTimeMillis()-date.getTime();

        return (int)(times/1000/60/60/24);
    }

    /**
     * 获取距上次时间多少天了
     * @param lastTime
     * @return
     */
    public static int getLastDays(Long lastTime) {
        Long now = System.currentTimeMillis();
        int days = (int) ((now - lastTime)/1000/60/60/24);
        return days;
    }

    /**
     * 判断是否为今天
     * @param d
     * @return
     */
    public static boolean isToday(Date d) {
        String oldTime=formatDateToStr(d, "yyyy-MM-dd");
        String newTime=formatDateToStr(new Date(), "yyyy-MM-dd");
	    if(oldTime.equals(newTime)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception{
        System.out.print(getNumsHoursAgo(-36));
        System.out.print(getNumsDaysAgo(-10));
        System.out.println(new Date(getNumsDaysEndAgo(10)));

        System.out.println("hour:"+getHour());

        System.out.printf("start:"+getNumsAgoStartDate(0));
        System.out.printf("start:"+getNumsAgoEndDate(0));

        System.out.println("isToday:"+isToday(new Date()));
    }
}

package com.ryd.stockanalysis.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ryd.stockanalysis.bean.DateSchedule;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.common.DataConstant;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.*;

/**
 * <p>标题:判断是否为工作日</p>
 * <p>描述:判断是否为工作日</p>
 * 包名：com.ryd.stockanalysis.util
 * 创建人：songby
 * 创建时间：2016/4/5 10:25
 */

public class FestivalDateUtil {

    public FestivalDateUtil(){}

    /**
     * 判断一个日期是否日节假日 法定节假日只判断月份和天，不判断年
     *
     * @param date
     * @return
     */
    public static boolean isFestival(Date date, List<Date> list) {
        boolean festival = false;
        Calendar fcal = Calendar.getInstance();
        Calendar dcal = Calendar.getInstance();
        dcal.setTime(date);
        for (Date dt : list) {
            fcal.setTime(dt);
            // 法定节假日判断
            if (fcal.get(Calendar.YEAR) == dcal.get(Calendar.YEAR)
                    && fcal.get(Calendar.MONTH) == dcal.get(Calendar.MONTH)
                    && fcal.get(Calendar.DATE) == dcal.get(Calendar.DATE)) {
                festival = true;
            }
        }
        return festival;
    }

    /**
     * 周六周日判断
     *
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
        boolean weekend = false;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            weekend = true;
        }
        return weekend;
    }

    /**
     * 是否是工作日 法定节假日和周末为非工作日
     *
     * @param date
     * @return
     */
    public static boolean isWorkDay(Date date, List<Date> workDayList, List<Date> festivalDayList) {
        boolean workday = true;
        if (isFestival(date,festivalDayList) || isWeekend(date)) {
            workday = false;
        }

        /* 特殊工作日判断 */
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        Calendar cal2 = Calendar.getInstance();
        for (Date dt : workDayList) {
            cal2.setTime(dt);
            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                    && cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE)) { // 年月日相等为特殊工作日
                workday = true;
            }
        }
        return workday;
    }

}


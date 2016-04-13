package com.ryd.stockanalysis.service.impl;

import com.ryd.stockanalysis.bean.DateSchedule;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.service.StDateScheduleServiceI;
import com.ryd.stockanalysis.util.DateUtils;
import com.ryd.stockanalysis.util.FestivalDateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/4/13 12:05
 */
public class StDateScheduleServiceImpl implements StDateScheduleServiceI {

    @Override
    public int dateAndTimeJudge() {
        //当前时间
        Calendar calNow = Calendar.getInstance();
        List<Date> festival = getDateList(Constant.DATE_SCHEDULE_FESTIVALDAY);
        List<Date> workday = getDateList(Constant.DATE_SCHEDULE_SEPCIAL_WORKDAY);
        //如果当前时间是工作日
        if (FestivalDateUtil.isWorkDay(calNow.getTime(), workday, festival)) {

            long tnow = calNow.getTime().getTime();
            //开盘时间
            long t9 = DateUtils.getSetHourTime(Constant.STOCK_OPEN_TIME).getTime();
            //休盘时间开始
            long t11 = DateUtils.getSetHourTime(Constant.STOCK_REST_TIME_START).getTime();
            //休盘时间结束
            long t13 = DateUtils.getSetHourTime(Constant.STOCK_REST_TIME_END).getTime();
            //收盘时间
            long t15 = DateUtils.getSetHourTime(Constant.STOCK_CLOSE_TIME).getTime();

            //如果当前时间在上午9：30~11：30之间，或者下午1：00~3：00之间，可以交易和报价
            if ((t9 < tnow && tnow < t11) || (t13 < tnow && tnow < t15)) {
                return Constant.STQUOTE_TRADE_TIMECOMPARE_1;
            } else if ((t11 < tnow && tnow < t13)) {//如果当前时间在11：30~13：00之间只允许报价
                return Constant.STQUOTE_TRADE_TIMECOMPARE_2;
            } else {//之外的时间，不允许报价，不允许交易
                return Constant.STQUOTE_TRADE_TIMECOMPARE_3;
            }
        }
        return Constant.STQUOTE_TRADE_TIMECOMPARE_3;
    }

    @Override
    public boolean dateIsWorkDayJudge() {
        Calendar calNow = Calendar.getInstance();
        List<Date> festival = getDateList(Constant.DATE_SCHEDULE_FESTIVALDAY);
        List<Date> workday = getDateList(Constant.DATE_SCHEDULE_SEPCIAL_WORKDAY);
        //如果当前时间是工作日
        return FestivalDateUtil.isWorkDay(calNow.getTime(), workday, festival);
    }


    private List<Date> getDateList(Integer type){
        List<Date> list = new ArrayList<Date>();
        List<DateSchedule> schedules = null;
        if(Constant.DATE_SCHEDULE_FESTIVALDAY.intValue() == type.intValue()) {
            schedules= DataConstant.dateScheduleMap.get("festivalDay");
        }else{
            schedules = DataConstant.dateScheduleMap.get("sepcialWorkDay");
        }

        for (DateSchedule fds : schedules) {
            list.add(fds.getDate());
        }

        return list;
    }
}
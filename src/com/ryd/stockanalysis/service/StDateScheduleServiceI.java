package com.ryd.stockanalysis.service;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service
 * 创建人：songby
 * 创建时间：2016/4/13 12:04
 */
public interface StDateScheduleServiceI {

    /**
     * 判断交易日交易时间区间
     * @return
     */
    public int dateAndTimeJudge();

    /**
     * 当前时间是否是工作日
     * @return
     */
    public boolean dateIsWorkDayJudge();
}

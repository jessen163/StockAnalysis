package com.ryd.stockanalysis.service;

import com.ryd.stockanalysis.bean.StTradeRecord;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/3/30 13:36
 */
public interface StTradeRecordServiceI {

    /**
     * 添加交易记录
     * @param record
     * @return
     */
    public StTradeRecord addStTradeRecord(StTradeRecord record);
}

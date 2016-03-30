package com.ryd.stockanalysis.service.impl;

import com.ryd.stockanalysis.bean.StTradeRecord;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.service.StTradeRecordServiceI;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/3/30 13:36
 */
public class StTradeRecordServiceImpl implements StTradeRecordServiceI {

    @Override
    public StTradeRecord addStTradeRecord(StTradeRecord record){
        //添加交易记录列表
        Constant.recordList.add(record);
        return record;
    }
}

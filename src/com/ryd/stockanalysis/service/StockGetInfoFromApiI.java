package com.ryd.stockanalysis.service;

import com.ryd.stockanalysis.bean.StStock;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service
 * 创建人：songby
 * 创建时间：2016/4/6 17:03
 */
public interface StockGetInfoFromApiI {


    /**
     * 获取股票信息
     * @param st
     * @param stockCode
     * @return
     */
    public StStock getStStockInfo(String st, String stockCode);
}

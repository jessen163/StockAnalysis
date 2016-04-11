package com.ryd.stockanalysis.service;

import com.ryd.stockanalysis.bean.StStock;

import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface StStockServiceI {
    /**
     * 查询股票信息
     * @return
     */
    public List<StStock> findStockList();
}

package com.ryd.stockanalysis.service.impl;

import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.service.StStockServiceI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class StStockServiceImpl implements StStockServiceI {
    @Override
    public List<StStock> findStockList() {
        List<StStock> stStockList = new ArrayList<StStock>();
        for (String k: DataConstant.stockTable.keySet()) {
            stStockList.add(DataConstant.stockTable.get(k));
        }
        return stStockList;
    }
}

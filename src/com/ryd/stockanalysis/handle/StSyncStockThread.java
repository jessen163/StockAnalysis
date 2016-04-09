package com.ryd.stockanalysis.handle;

import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.service.StockGetInfoFromApiI;
import org.apache.log4j.Logger;

/**
 *
 * 同步股票实时价格信息
 * Created by Administrator on 2016/4/9.
 */
public class StSyncStockThread implements Runnable {

    private static Logger logger = Logger.getLogger(StSyncStockThread.class);

    private StockGetInfoFromApiI stockGetInfoFromApiI;
    private StockAnalysisServiceI stockAnalysisServiceI;

    public StSyncStockThread(StockGetInfoFromApiI stockGetInfoFromApiI, StockAnalysisServiceI stockAnalysisServiceI) {
        this.stockGetInfoFromApiI = stockGetInfoFromApiI;
        this.stockAnalysisServiceI = stockAnalysisServiceI;
    }

    @Override
    public void run() {
        while (true) {
            logger.info("更新股票实时信息.............start...........");
//            StStock stock = null;
//            for (String k :DataConstant.stockTable.keySet()) {
//                StStock stStock = DataConstant.stockTable.get(k);
//                stock = stockGetInfoFromApiI.getStStockInfo(stStock.getStockType(), stStock.getStockCode());
//                if (stock!=null) {
//                    stock.setStockId(stStock.getStockId());
//                    stockAnalysisServiceI.quotePriceBySimulation(DataConstant.accountList,stock);
//                }
//            }
            stockAnalysisServiceI.quotePriceBySimulation();

            logger.info("更新股票实时信息.............end...........");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

package com.ryd.stockanalysis.util;

import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.service.StockGetInfoFromApiI;
import com.ryd.stockanalysis.service.impl.StockGetInfoFromApiImpl;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 股票实时数据获取-测试
 * Created by Administrator on 2016/4/11.
 */
public class StockApplicationUtil {

    private static Logger logger = Logger.getLogger(StockApplicationUtil.class);

    private static LinkedList<StStock> stStockList= new LinkedList<StStock>();

    public static void main(String[] args) {
        StockGetInfoFromApiI stockGetInfoFromApiI = new StockGetInfoFromApiImpl();
        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    StStock stock=stockGetInfoFromApiI.getStStockInfo("sh", "601318");
                    if (stStockList.size()==5) {
                        stStockList.removeFirst();
                    }
                    stStockList.add(stock);
                    logger.info(stStockList);
                    try {
                        TimeUnit.MINUTES.sleep(1);
//                        TimeUnit.SECONDS.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
//        logger.info("stock:" + stock);
    }

}

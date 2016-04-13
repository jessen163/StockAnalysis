package com.ryd.stockanalysis;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.common.DataInitTool;
import com.ryd.stockanalysis.handle.StockSettleTask;
import com.ryd.stockanalysis.net.StockServer;
import com.ryd.stockanalysis.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.handle.StTradeThread;
import com.ryd.stockanalysis.handle.StockTradeThread;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.service.impl.StockAnalysisServiceImpl;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis
 * 创建人：songby
 * 创建时间：2016/3/28 12:00
 */
public class StockAnalysis {

    private static Logger logger = Logger.getLogger(StockAnalysis.class);


    public static void main(String[] args) throws Exception {

        StockAnalysisServiceI serviceI = new StockAnalysisServiceImpl();
        // 创建一个可重用固定线程数的线程池
        ExecutorService pool = Executors.newFixedThreadPool(50);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                StockServer stockServer = new StockServer(8888);
                try {
                    stockServer.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //创建基础数据
        boolean rs = DataInitTool.createBaseData();

        //数据监测
        DataInitTool.dataCheck("初始", DateUtils.formatLongToStr(System.currentTimeMillis(),DateUtils.TIME_FORMAT));
        //初始数据
        for (String key : DataConstant.stAccounts.keySet()) {
            StAccount uu = DataConstant.stAccounts.get(key);
            DataInitTool.printAccountInfo(uu, "初始");
        }

        if(rs) {

            // 往线程池中放入用户报价（买入/卖出）信息------------StockTradeThread
            pool.execute(new StTradeThread());

//            DataInitTool.initQuotePriceMap(pool, serviceI);

            Timer timer = new Timer();
            StockSettleTask settleTask = new StockSettleTask(serviceI);

            Date settledate = DateUtils.getSetHourTime(Constant.STOCK_SETTLE_TIME);
            Date closedate = DateUtils.getSetHourTime(Constant.STOCK_CLOSE_TIME);

            //每天的date时刻执行task, 仅执行一次
            if( settledate.getTime() < closedate.getTime() ){
                timer.schedule(settleTask, closedate);
            }else{
                timer.schedule(settleTask, settledate);
            }


        }
    }
}

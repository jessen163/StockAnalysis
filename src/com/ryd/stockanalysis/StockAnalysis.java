package com.ryd.stockanalysis;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.common.DataInitTool;
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


    public static void main(String[] args) {
        logger.info("股票分析---------------开始--------------------");

        StockAnalysisServiceI serviceI = new StockAnalysisServiceImpl();
        // 创建一个可重用固定线程数的线程池
        ExecutorService pool = Executors.newFixedThreadPool(50);

        //创建基础数据
        boolean rs = DataInitTool.createBaseData();

        //数据监测
        DataInitTool.dataCheck("open开盘前");
        //初始数据
        for (String key : DataConstant.stAccounts.keySet()) {
            StAccount uu = DataConstant.stAccounts.get(key);
            DataInitTool.printAccountInfo(uu, "初始");
        }

        if(rs) {

            // 往线程池中放入用户报价（买入/卖出）信息------------StockTradeThread
            pool.execute(new StTradeThread());

            DataInitTool.initQuotePriceMap(pool, serviceI);

            try {
                //30秒后结算
                Thread.sleep(1000 * Constant.TRADE_LATER_TIME);
                //结算前用户信息
                for (String key : DataConstant.stAccounts.keySet()) {
                    StAccount uu = DataConstant.stAccounts.get(key);
                    DataInitTool.printAccountInfo(uu, "结算前");
                }
                //结算信息
                serviceI.settleResult();

            } catch (InterruptedException e) {

            }


            //结算后用户信息
            for (String key : DataConstant.stAccounts.keySet()) {
                StAccount uu = DataConstant.stAccounts.get(key);
                DataInitTool.printAccountInfo(uu, "结算后");
            }


            for (StTradeRecord stt : DataConstant.recordList) {
                StStock stock = DataConstant.stockTable.get(stt.getStockId());
                logger.info("记录分析--交易买方->" + stt.getBuyerAccountId() + "--卖方->" + stt.getSellerAccountId() + "--交易股票->" + stock.getStockName() + "--股票编码->" + stock.getStockCode() + "--交易价格->" + stt.getQuotePrice() + "--交易数量->" + stt.getAmount() + "--交易总额->" + stt.getDealMoney() + "--买卖家佣金总合->" + stt.getDealFee() + "--印花税->" + stt.getDealTax());
            }

            //数据监测
            DataInitTool.dataCheck("settle结算后");

        }
        logger.info("股票分析---------------结束---------------------");
    }



}

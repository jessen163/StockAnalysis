package com.ryd.stockanalysis.service;

import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.bean.StTradeQueue;

import java.util.Map;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service
 * 创建人：songby
 * 创建时间：2016/3/29 10:02
 */
public interface StockAnalysisServiceI {


    /**
     * 交易
     * @param buyQuote
     * @param sellQuote
     * @param sts
     */
    public void dealTrading(StTradeQueue stTradeQueueMap, StQuote buyQuote, StQuote sellQuote, StStock sts);
    /**
     * 结算结果
     * @return
     */
    public boolean settleResult();


    /**
     * 买卖钱数
     * @param qutoPrice
     * @param amount
     * @param type
     * @return
     */
    public Map<String, Object> buyOrSellStockMoney(Double qutoPrice, Integer amount, Integer type);

    /**
     * 委托买卖股票
     * @param stQuote
     * @param closePrice
     * @return
     */
    public boolean trusteeStockBuySale(StQuote stQuote,double closePrice);
    /**
     * 报价
     * @param stQuote
     * @return
     */
    public StQuote quotePrice(StQuote stQuote);

    /**
     * 撤单
     * @param stQuote
     * @return
     */
    public boolean cancelStQuote(StQuote stQuote);


    /**
     * 计算股票涨跌幅
     * @param closePrice 昨日收盘价
     * @return
     */
    public Map stockUpAndDownScope(double closePrice);
}

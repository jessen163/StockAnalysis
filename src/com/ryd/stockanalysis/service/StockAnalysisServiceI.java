package com.ryd.stockanalysis.service;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.bean.StTradeQueue;

import java.util.List;
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
     * 报价--委托买卖股票
     * @param stQuote
     * @return
     */
    public boolean quotePrice(StQuote stQuote);

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
     * 撤单
     * @param stQuote
     * @return
     */
    public boolean cancelStQuote(StQuote stQuote);

    /**
     * 模拟报价
     * @param stAccountList
     * @param stStock
     * @return
     */
    public boolean quotePriceBySimulation(List<StAccount> stAccountList, StStock stStock);
}

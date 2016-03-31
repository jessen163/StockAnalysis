package com.ryd.stockanalysis.service;

import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;

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
     * @param tradeStockAmount 股票实际交易量
     * @param sts
     */
    public void trading(StQuote buyQuote, StQuote sellQuote, int tradeStockAmount,StStock sts);
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
     * 报价
     * @param stQuote
     * @return
     */
    public StQuote quotePrice(StQuote stQuote);


}

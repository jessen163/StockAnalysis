package com.ryd.stockanalysis.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.service.StAccountServiceI;
import com.ryd.stockanalysis.service.StPositionServiceI;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import org.apache.log4j.Logger;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/3/29 10:00
 */
public class StockAnalysisServiceImpl implements StockAnalysisServiceI {

    private static Logger logger = Logger.getLogger(StockAnalysisServiceImpl.class);

    private StAccountServiceI stAccountServiceI;

    private StPositionServiceI stPositionServiceI;

    public StockAnalysisServiceImpl() {
        stAccountServiceI = new StAccountServiceImpl();
        stPositionServiceI = new StPositionServiceImpl();
    }

    @Override
    public boolean settleResult(){
        //卖家结算
        LinkedList<StQuote> sellLinkList = Constant.sellList.getList();
        Iterator iterator = sellLinkList.iterator();
        while (iterator.hasNext())
        {
            StQuote stq = (StQuote)iterator.next();

            //撤回托管为卖的股票，持仓数量增加
            stPositionServiceI.operateStPosition(stq.getAccountId(), stq.getStockId(), stq.getAmount(), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

            StStock sto = Constant.stockTable.get(stq.getStockId());

            logger.info("卖家结算--卖家->" + stq.getAccountId() + "--股票名称->" + sto.getStockName() + "--股票编码->" + sto.getStockCode() + "--退还股票数--" +stq.getAmount());
       }
        //清除卖家队列
        Constant.sellList.clear();


        //买家结算，退还未交易报价费用
        LinkedList<StQuote> buyLinkList =  Constant.buyList.getList();
        Iterator iteratorb = buyLinkList.iterator();
        while (iteratorb.hasNext())
        {
            StQuote stqb = (StQuote)iteratorb.next();

            //买家报价开销
            double oinmoney = stqb.getFrozeMoney();

            //撤回托管买股票费用,帐户增加资产
            stAccountServiceI.opearteUseMoney(stqb.getAccountId(), oinmoney, Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

            StStock sto = Constant.stockTable.get(stqb.getStockId());

            logger.info("买家结算--买家->"+stqb.getAccountId()+"--交易股票->"+sto.getStockName()+"--股票编码->"+sto.getStockCode()+"--退还金额->"+oinmoney);
        }
        //清除买家队列
        Constant.buyList.clear();

        return true;
    }

    @Override
    public double buyOrSellStockMoney(Double qutoPrice, Integer amount, Integer type) {

        double dealMoney = 0d;
        //金额
        BigDecimal volMoney = null;

        BigDecimal qutoPriceb = new BigDecimal(qutoPrice==null ? 0 : 0d);
        BigDecimal amountb = new BigDecimal(amount == null ? 0 : 0);

        volMoney = qutoPriceb.multiply(amountb);

        //买股票
        if (type == Constant.STOCK_STQUOTE_TYPE_BUY) {

            //佣金
            double commissionFee = 0d;
            //佣金比例
            BigDecimal cratio = new BigDecimal(Constant.STOCK_COMMINSSION_MONEY);
            //计算佣金
            commissionFee = volMoney.multiply(cratio).doubleValue();

            dealMoney = volMoney.doubleValue() + commissionFee;

        }else if (type == Constant.STOCK_STQUOTE_TYPE_SELL) {//卖股票
            //佣金
            double bcommissionFee = 0d;
            //印花税
            double bstampFax = 0d;
            //佣金比例
            BigDecimal bcratio = new BigDecimal(Constant.STOCK_COMMINSSION_MONEY);
            //印花税比例
            BigDecimal bsratio = new BigDecimal(Constant.STOCK_STAMP_TAX);
            //计算佣金和印花税
            bcommissionFee = volMoney.multiply(bcratio).doubleValue();
            bstampFax = volMoney.multiply(bsratio).doubleValue();

            dealMoney = volMoney.doubleValue() - (bcommissionFee+bstampFax);

        }else{}

        return dealMoney;
    }


    @Override
    public synchronized StQuote quotePrice(StQuote stQuote) {

        if(stQuote == null){
            return null;
        }

        boolean rs = false;

        //买股票
        if (stQuote.getType() == Constant.STOCK_STQUOTE_TYPE_BUY) {
            //委托买股票，减少资产

            //冻结资金
            double frozenMoney = buyOrSellStockMoney(stQuote.getQuotePrice(),stQuote.getAmount(),stQuote.getType());
            stQuote.setFrozeMoney(frozenMoney);

            rs = stAccountServiceI.opearteUseMoney(stQuote.getAccountId(),frozenMoney, Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE);

        }else if (stQuote.getType() == Constant.STOCK_STQUOTE_TYPE_SELL) {//卖股票
            //委托卖股票，减少股票持仓数量
            rs = stPositionServiceI.operateStPosition(stQuote.getAccountId(),stQuote.getStockId(),stQuote.getAmount(),Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE);
        }else{}

        if(rs) {
            stQuote.setQuoteId(UUID.randomUUID().toString());
            // 用于排序的字段
            long quotePriceSort = Long.parseLong("100000000") * (int)(stQuote.getQuotePrice()*100)+Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(5));
            stQuote.setQuotePriceForSort(quotePriceSort);

            StTradeQueue stTradeQueue = Constant.stTradeQueueMap.get(stQuote.getStockId());
            if (stTradeQueue==null) {
                stTradeQueue = new StTradeQueue();
            }
            if (stQuote.getType() == Constant.STOCK_STQUOTE_TYPE_BUY) {
            	Constant.buyList.add(stQuote);
                stTradeQueue.buyList.put(stQuote.getQuotePriceForSort(), stQuote);
            } else {
            	Constant.sellList.add(stQuote);
                stTradeQueue.sellList.put(stQuote.getQuotePriceForSort(), stQuote);
            }
            Constant.stTradeQueueMap.put(stQuote.getStockId(), stTradeQueue);
        }

        return stQuote;
    }
}

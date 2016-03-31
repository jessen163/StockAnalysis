package com.ryd.stockanalysis.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.service.StAccountServiceI;
import com.ryd.stockanalysis.service.StPositionServiceI;
import com.ryd.stockanalysis.service.StTradeRecordServiceI;
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

    private StTradeRecordServiceI stTradeRecordServiceI;

    public StockAnalysisServiceImpl() {
        stAccountServiceI = new StAccountServiceImpl();
        stPositionServiceI = new StPositionServiceImpl();
        stTradeRecordServiceI = new StTradeRecordServiceImpl();
    }

    @Override
    public void trading(StQuote buyQuote, StQuote sellQuote, int tradeStockAmount,StStock sts){

        //交易成功，交易买家持仓增加
        stPositionServiceI.operateStPosition(buyQuote.getAccountId(), buyQuote.getStockId(), tradeStockAmount, Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

        //卖家新增费用计算
        Map<String, Object> sellmap = buyOrSellStockMoney(buyQuote.getQuotePrice(), tradeStockAmount, sellQuote.getType());

        //交易成功，交易卖家资产增加
        stAccountServiceI.opearteUseMoney(sellQuote.getAccountId(), (double) sellmap.get("rsMoney"), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

        double fee = new BigDecimal((double) sellmap.get("commissionFee")).multiply(new BigDecimal(2)).doubleValue();
        //收取买家卖家佣金和卖家印花税
        Constant.STOCK_TRADE_AGENT_MONEY = Constant.STOCK_TRADE_AGENT_MONEY + fee + (double) sellmap.get("bstampFax");

        //添加交易记录
        StTradeRecord str = new StTradeRecord();
        str.setId(UUID.randomUUID().toString());
        str.setBuyerAccountId(buyQuote.getAccountId());
        str.setSellerAccountId(sellQuote.getAccountId());
        str.setAmount(tradeStockAmount);
        str.setStockId(sellQuote.getStockId());
        str.setQuotePrice(buyQuote.getQuotePrice());
        BigDecimal quotePriceb = new BigDecimal(buyQuote.getQuotePrice());
        BigDecimal amonutb = new BigDecimal(tradeStockAmount);
        str.setDealMoney(quotePriceb.multiply(amonutb).doubleValue());
        str.setDealFee(fee);
        str.setDealTax((double) sellmap.get("bstampFax"));
        str.setDateTime(System.currentTimeMillis());
        str.setStockCode(sts.getStockCode());

        //交易记录列表
        stTradeRecordServiceI.addStTradeRecord(str);

        logger.info("交易--买家->" + buyQuote.getAccountId() + "-和-卖家->" + sellQuote.getAccountId() + "--交易成功--" + "--交易股票->" + sts.getStockName() + "--股票编码->" + sts.getStockCode() + "--交易价格->" + buyQuote.getQuotePrice() + "-交易数量->" + buyQuote.getAmount() + "-交易总额->" + quotePriceb.multiply(amonutb).doubleValue() + "-买家卖家佣金->" + fee + "-印花税->" + +(double) sellmap.get("bstampFax"));
    }


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

            //撤回托管买股票费用,帐户增加资产
            stAccountServiceI.opearteUseMoney(stqb.getAccountId(), stqb.getFrozeMoney(), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

            StStock sto = Constant.stockTable.get(stqb.getStockId());

            logger.info("买家结算--买家->"+stqb.getAccountId()+"--交易股票->"+sto.getStockName()+"--股票编码->"+sto.getStockCode()+"--退还金额->"+stqb.getFrozeMoney());
        }
        //清除买家队列
        Constant.buyList.clear();

        return true;
    }

    @Override
    public synchronized Map<String, Object> buyOrSellStockMoney(Double qutoPrice, Integer amount, Integer type) {

        Map<String, Object> rs = new HashMap<String, Object>();

        double rsMoney = 0d;
        //金额
        BigDecimal volMoney = null;

        BigDecimal qutoPriceb = new BigDecimal(qutoPrice==null ? 0 : qutoPrice);
        BigDecimal amountb = new BigDecimal(amount == null ? 0 : amount);

        volMoney = qutoPriceb.multiply(amountb);

        //佣金
        double commissionFee = 0d;
        //印花税
        double bstampFax = 0d;
        //佣金比例
        BigDecimal cratio = new BigDecimal(Constant.STOCK_COMMINSSION_MONEY);
        //计算佣金
        commissionFee = volMoney.multiply(cratio).doubleValue();

        //买股票
        if (type == Constant.STOCK_STQUOTE_TYPE_BUY) {

            rsMoney = volMoney.doubleValue() + commissionFee;

        }else if (type == Constant.STOCK_STQUOTE_TYPE_SELL) {//卖股票

            //印花税比例
            BigDecimal bsratio = new BigDecimal(Constant.STOCK_STAMP_TAX);
            //计算印花税
            bstampFax = volMoney.multiply(bsratio).doubleValue();

            rsMoney = volMoney.doubleValue() - (commissionFee+bstampFax);

        }else{}

        rs.put("rsMoney",rsMoney);
        rs.put("volMoney",volMoney);
        rs.put("commissionFee",commissionFee);
        rs.put("bstampFax",bstampFax);

        return rs;
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

            //金额计算
            Map<String, Object> rsmap = buyOrSellStockMoney(stQuote.getQuotePrice(), stQuote.getAmount(), stQuote.getType());
            //冻结资金
            stQuote.setFrozeMoney((double)rsmap.get("rsMoney"));
            stQuote.setCommissionFee((double) rsmap.get("commissionFee"));
            rs = stAccountServiceI.opearteUseMoney(stQuote.getAccountId(),(double)rsmap.get("rsMoney"), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE);

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
            synchronized (Constant.stTradeQueueMap) {
                if (stQuote.getType() == Constant.STOCK_STQUOTE_TYPE_BUY) {
//            	Constant.buyList.add(stQuote);
                    stTradeQueue.addBuyStQuote(stQuote);
                    stTradeQueue.buyList.put(stQuote.getQuotePriceForSort(), stQuote);
                } else {
//            	Constant.sellList.add(stQuote);
                    stTradeQueue.addSellStQuote(stQuote);
                    stTradeQueue.sellList.put(stQuote.getQuotePriceForSort(), stQuote);
                }
                Constant.stTradeQueueMap.put(stQuote.getStockId(), stTradeQueue);
            }
        }

        return stQuote;
    }
}

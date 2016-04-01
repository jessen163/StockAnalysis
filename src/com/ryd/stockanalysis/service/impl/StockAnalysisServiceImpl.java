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
    public void dealTrading(StQuote buyQuote, StQuote sellQuote, StStock sts){
        //添加买入/卖出交易成功后的逻辑

        //股票交易数量
        int tradeStockAmount = 0;

        //买少卖多
        if (buyQuote.getAmount().intValue() < sellQuote.getAmount().intValue()) {

            //股票交易数量为买家购买数量
            tradeStockAmount = buyQuote.getAmount();

            //处理交易
            trading(buyQuote, sellQuote, tradeStockAmount, sts);

            //交易成功，交易卖家持仓减少
            sellQuote.setAmount(sellQuote.getAmount() - tradeStockAmount);

            //买家移出队列
            Constant.buyList.removeElement(buyQuote);

        } else if (buyQuote.getAmount().intValue() == sellQuote.getAmount().intValue()) {//买卖相等

            tradeStockAmount = buyQuote.getAmount();
            //处理交易
            trading(buyQuote, sellQuote, tradeStockAmount, sts);

            //买家卖家移出队列
            Constant.sellList.removeElement(sellQuote);
            Constant.buyList.removeElement(buyQuote);

        }else if(buyQuote.getAmount().intValue() > sellQuote.getAmount().intValue()){//买多卖少

            //股票交易数量为卖家卖掉数量
            tradeStockAmount = sellQuote.getAmount();
            //处理交易
            trading(buyQuote, sellQuote, tradeStockAmount, sts);

            //买家报价剩余股票数量
            int remainAmount = buyQuote.getAmount()-tradeStockAmount;

            //交易成功，交易买家报价金额减少
            Map<String, Object> buymap = buyOrSellStockMoney(buyQuote.getQuotePrice(), remainAmount, buyQuote.getType());
            buyQuote.setFrozeMoney((double) buymap.get("figureMoney"));
            buyQuote.setCommissionFee((double) buymap.get("commissionFee"));

            //交易成功，股票交易数量减少
            buyQuote.setAmount(remainAmount);

            //卖家移出队列
            Constant.sellList.removeElement(sellQuote);
        }
    }


    /**
     * 交易处理
     * @param buyQuote
     * @param sellQuote
     * @param tradeStockAmount
     * @param sts
     */
    private void trading(StQuote buyQuote, StQuote sellQuote, int tradeStockAmount, StStock sts){

        //股票交易价格
        double tradeStockQuotePrice = 0d;

        tradeStockQuotePrice = judgeQuotePrice(buyQuote, sellQuote, tradeStockAmount);

        //交易成功，交易买家持仓增加
        stPositionServiceI.operateStPosition(buyQuote.getAccountId(), buyQuote.getStockId(), tradeStockAmount, Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

        //卖家新增费用计算
        Map<String, Object> sellmap = buyOrSellStockMoney(tradeStockQuotePrice, tradeStockAmount, sellQuote.getType());

        //交易成功，交易卖家资产增加
        stAccountServiceI.opearteUseMoney(sellQuote.getAccountId(), (double) sellmap.get("figureMoney"), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

        double fee = BigDecimal.valueOf((double) sellmap.get("commissionFee")).multiply(new BigDecimal(2)).doubleValue();
        //收取买家卖家佣金和卖家印花税
        Constant.STOCK_TRADE_AGENT_MONEY = Constant.STOCK_TRADE_AGENT_MONEY.intValue() + fee + (double) sellmap.get("bstampFax");

        //添加交易记录
        StTradeRecord str = new StTradeRecord();
        str.setId(UUID.randomUUID().toString());
        str.setBuyerAccountId(buyQuote.getAccountId());
        str.setSellerAccountId(sellQuote.getAccountId());
        str.setAmount(tradeStockAmount);
        str.setStockId(sts.getStockId());
        str.setQuotePrice(tradeStockQuotePrice);
        BigDecimal quotePriceb = BigDecimal.valueOf(tradeStockQuotePrice);
        BigDecimal amonutb = new BigDecimal(tradeStockAmount);
        str.setDealMoney(quotePriceb.multiply(amonutb).doubleValue());
        str.setDealFee(fee);
        str.setDealTax((double) sellmap.get("bstampFax"));
        str.setDateTime(System.currentTimeMillis());
        str.setStockCode(sts.getStockCode());

        //交易记录列表
        stTradeRecordServiceI.addStTradeRecord(str);

        logger.info("交易--买家->" + buyQuote.getAccountId() + "-和-卖家->" + sellQuote.getAccountId() + "--交易成功--" + "--交易股票->" + sts.getStockName() + "--股票编码->" + sts.getStockCode() + "--交易价格->" + tradeStockQuotePrice + "-交易数量->" + tradeStockAmount + "-交易总额->" + quotePriceb.multiply(amonutb).doubleValue() + "-买家卖家佣金->" + fee + "-印花税->" + +(double) sellmap.get("bstampFax"));
    }

    /**
     * 判断买家卖家报价
     * @param buyQuote
     * @param sellQuote
     * @param tradeStockAmount
     */
    private double judgeQuotePrice(StQuote buyQuote, StQuote sellQuote, int tradeStockAmount){

        double tradeStockQuotePrice = 0d;
        //买家报价大于卖家报价
        if(Double.doubleToLongBits(sellQuote.getQuotePrice()) < Double.doubleToLongBits(buyQuote.getQuotePrice())){
            //如果卖家报价早于买家报价
            if(sellQuote.getDateTime().longValue() < buyQuote.getDateTime().longValue()){
                //交易价格取卖家报价
                tradeStockQuotePrice = sellQuote.getQuotePrice();

                //节省成本
                double saveMoney = 0d;
                //以买家报价计算交易成本
                Map<String, Object> buymap = buyOrSellStockMoney(buyQuote.getQuotePrice(), tradeStockAmount, buyQuote.getType());
                //以交易价格计算交易成本
                Map<String, Object> dealmap = buyOrSellStockMoney(tradeStockQuotePrice, tradeStockAmount, buyQuote.getType());
                //节省成本
                saveMoney = (double) buymap.get("figureMoney") - (double) dealmap.get("figureMoney");
                //将节省成本归还买家
                stAccountServiceI.opearteUseMoney(buyQuote.getAccountId(),saveMoney,Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

            }else if(sellQuote.getDateTime().longValue() >= buyQuote.getDateTime().longValue()){ //如果买家报价早于或等于卖家报价
                //交易价格取买家报价
                tradeStockQuotePrice = buyQuote.getQuotePrice();
            }
        }else{//买家报价等于卖家报价
            tradeStockQuotePrice = buyQuote.getQuotePrice();
        }
        return tradeStockQuotePrice;
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

        double figureMoney = 0d;
        //金额
        BigDecimal volMoney = null;

        BigDecimal qutoPriceb = BigDecimal.valueOf(qutoPrice == null ? 0 : qutoPrice);
        BigDecimal amountb = new BigDecimal(amount == null ? 0 : amount);

        volMoney = qutoPriceb.multiply(amountb);

        //佣金
        double commissionFee = 0d;
        //印花税
        double bstampFax = 0d;
        //佣金比例
        BigDecimal cratio = BigDecimal.valueOf(Constant.STOCK_COMMINSSION_MONEY);
        //计算佣金
        commissionFee = volMoney.multiply(cratio).doubleValue();
        //买股票
        if (type.intValue() == Constant.STOCK_STQUOTE_TYPE_BUY.intValue()) {

            figureMoney = volMoney.doubleValue() + commissionFee;

        }else if (type.intValue() == Constant.STOCK_STQUOTE_TYPE_SELL.intValue()) {//卖股票

            //印花税比例
            BigDecimal bsratio = BigDecimal.valueOf(Constant.STOCK_STAMP_TAX);
            //计算印花税
            bstampFax = volMoney.multiply(bsratio).doubleValue();

            figureMoney = volMoney.doubleValue() - (commissionFee+bstampFax);

        }else{}

        rs.put("figureMoney",figureMoney);
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
        if (stQuote.getType().intValue() == Constant.STOCK_STQUOTE_TYPE_BUY.intValue()) {
            //委托买股票，减少资产

            //金额计算
            Map<String, Object> rsmap = buyOrSellStockMoney(stQuote.getQuotePrice(), stQuote.getAmount(), stQuote.getType());
            //冻结资金
            stQuote.setFrozeMoney((double)rsmap.get("figureMoney"));
            stQuote.setCommissionFee((double) rsmap.get("commissionFee"));
            rs = stAccountServiceI.opearteUseMoney(stQuote.getAccountId(), (double) rsmap.get("figureMoney"), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE);

        }else if (stQuote.getType().intValue() == Constant.STOCK_STQUOTE_TYPE_SELL.intValue()) {//卖股票
            //委托卖股票，减少股票持仓数量
            rs = stPositionServiceI.operateStPosition(stQuote.getAccountId(),stQuote.getStockId(),stQuote.getAmount(),Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE);
        }else{}

        if(rs) {
            stQuote.setQuoteId(UUID.randomUUID().toString());
            // 用于排序的字段
            long quotePriceSort = Long.parseLong("100000000") * stQuote.getQuotePrice().longValue()+Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(5));
            stQuote.setQuotePriceForSort(quotePriceSort);

            StTradeQueue stTradeQueue = Constant.stTradeQueueMap.get(stQuote.getStockId());
            if (stTradeQueue==null) {
                stTradeQueue = new StTradeQueue();
            }
            synchronized (Constant.stTradeQueueMap) {
                if (stQuote.getType().intValue() == Constant.STOCK_STQUOTE_TYPE_BUY.intValue()) {
            	Constant.buyList.add(stQuote);
                    stTradeQueue.addBuyStQuote(stQuote);
//                    stTradeQueue.buyList.put(stQuote.getQuotePriceForSort(), stQuote);
                } else {
            	Constant.sellList.add(stQuote);
                    stTradeQueue.addSellStQuote(stQuote);
//                    stTradeQueue.sellList.put(stQuote.getQuotePriceForSort(), stQuote);
                }
                Constant.stTradeQueueMap.put(stQuote.getStockId(), stTradeQueue);
            }
        }

        return stQuote;
    }
}

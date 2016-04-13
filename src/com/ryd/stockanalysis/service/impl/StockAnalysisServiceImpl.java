package com.ryd.stockanalysis.service.impl;

import java.util.*;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.service.*;
import com.ryd.stockanalysis.util.ArithUtil;
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

    private StockGetInfoFromApiI stockGetInfoFromApiI;

    private StDateScheduleServiceI scheduleServiceI;

    public StockAnalysisServiceImpl() {
        stAccountServiceI = new StAccountServiceImpl();
        stPositionServiceI = new StPositionServiceImpl();
        stTradeRecordServiceI = new StTradeRecordServiceImpl();
        stockGetInfoFromApiI = new StockGetInfoFromApiImpl();
        scheduleServiceI = new StDateScheduleServiceImpl();
    }


    @Override
    public synchronized boolean quotePrice(StQuote stQuote) {
        boolean rs = false;
        //判断时间是否允许报价
        if(scheduleServiceI.dateAndTimeJudge() == Constant.STQUOTE_TRADE_TIMECOMPARE_1 ||
                scheduleServiceI.dateAndTimeJudge() == Constant.STQUOTE_TRADE_TIMECOMPARE_2) {

            //报价对应股票
            StStock stStock = DataConstant.stockTable.get(stQuote.getStockId());

            if (stStock == null) {
                return false;
            }
            stQuote.setStatus(Constant.STOCK_STQUOTE_STATUS_TRUSTEE);
            stQuote.setDateTime(System.currentTimeMillis());

            //报价大于等于最小价格，小于等于最大价格，可以正常报价
            if (isStockQuotePriceInScope(stStock.getBfclosePrice(), stQuote.getQuotePrice())) {
                stQuote.setCurrentAmount(stQuote.getAmount());
                //买股票
                if (stQuote.getType().intValue() == Constant.STOCK_STQUOTE_TYPE_BUY.intValue()) {
                    //委托买股票，减少资产

                    //金额计算
                    Map<String, Object> rsmap = buyOrSellStockMoney(stQuote.getQuotePrice(), stQuote.getAmount(), stQuote.getType());
                    //冻结资金
                    stQuote.setFrozeMoney((double) rsmap.get("figureMoney"));
                    stQuote.setCommissionFee((double) rsmap.get("commissionFee"));
                    rs = stAccountServiceI.opearteUseMoney(stQuote.getAccountId(), (double) rsmap.get("figureMoney"), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE);

                } else if (stQuote.getType().intValue() == Constant.STOCK_STQUOTE_TYPE_SELL.intValue()) {//卖股票
                    //委托卖股票，减少股票持仓数量
                    rs = stPositionServiceI.operateStPosition(stQuote.getAccountId(), stQuote.getStockId(), stQuote.getAmount(), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE);
                } else {
                }

                if (rs) {
                    stQuote.setQuoteId(UUID.randomUUID().toString());
                    // 用于排序的字段
                    long timeSort = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(7));
                    stQuote.setTimeSort(timeSort);

                    StTradeQueue stTradeQueue = DataConstant.stTradeQueueMap.get(stQuote.getStockId());
                    if (stTradeQueue == null) {
                        stTradeQueue = new StTradeQueue();
                    }

                    synchronized (DataConstant.stTradeQueueMap) {
                        if (stQuote.getType().intValue() == Constant.STOCK_STQUOTE_TYPE_BUY.intValue()) {
                            DataConstant.buyList.add(stQuote);
                            stTradeQueue.addBuyStQuote(stQuote);
//                    stTradeQueue.buyList.put(stQuote.getQuotePriceForSort(), stQuote);
                        } else {
                            DataConstant.sellList.add(stQuote);
                            stTradeQueue.addSellStQuote(stQuote);
//                    stTradeQueue.sellList.put(stQuote.getQuotePriceForSort(), stQuote);
                        }
                        DataConstant.stTradeQueueMap.put(stQuote.getStockId(), stTradeQueue);
                    }


                    //我的委托买或卖
                    Map stQuoteMap = DataConstant.stAccountQuoteMap.get(stQuote.getAccountId());
                    if (stQuoteMap == null) {
                        stQuoteMap = new HashMap<>();
                        DataConstant.stAccountQuoteMap.put(stQuote.getAccountId(), stQuoteMap);
                    }
                    stQuoteMap.put(stQuote.getQuoteId(), stQuote);

                    if (stQuote.getType().intValue() == Constant.STOCK_STQUOTE_TYPE_BUY.intValue()) {
                        logger.info("报价成功----报价状态->买入---买入->" + stQuote.getAccountId() + "--股票名称->" + stStock.getStockName() + "--股票编码->" + stStock.getStockCode() + "--申报价格->" + stQuote.getQuotePrice() + "--申报数量->" + stQuote.getAmount() + "--报价时间->" + new Date(stQuote.getDateTime()));
                    } else {
                        logger.info("报价成功----报价状态->卖出---卖方->" + stQuote.getAccountId() + "--股票名称->" + stStock.getStockName() + "--股票编码->" + stStock.getStockCode() + "--申报价格->" + stQuote.getQuotePrice() + "--申报数量->" + stQuote.getAmount() + "--报价时间->" + new Date(stQuote.getDateTime()));
                    }
                }
            }
        }
        return rs;
    }


    @Override
    public void dealTrading(StTradeQueue stTradeQueueMap, StQuote buyQuote, StQuote sellQuote, StStock sts){
        //添加买入/卖出交易成功后的逻辑

        //股票交易数量
        int tradeStockAmount = 0;

        //买少卖多
        if (buyQuote.getCurrentAmount().intValue() < sellQuote.getCurrentAmount().intValue()) {

            //股票交易数量为买家购买数量
            tradeStockAmount = buyQuote.getCurrentAmount();

            //处理交易
            trading(buyQuote, sellQuote, tradeStockAmount, sts);

            //交易成功，交易卖家持仓减少
            sellQuote.setCurrentAmount(sellQuote.getCurrentAmount() - tradeStockAmount);

            //买家移出队列
//            DataConstant.buyList.removeElement(buyQuote);
            stTradeQueueMap.removeBuyStQuote(buyQuote);

        } else if (buyQuote.getCurrentAmount().intValue() == sellQuote.getCurrentAmount().intValue()) {//买卖相等

            tradeStockAmount = buyQuote.getCurrentAmount();
            //处理交易
            trading(buyQuote, sellQuote, tradeStockAmount, sts);

            //买家卖家移出队列
//            DataConstant.sellList.removeElement(sellQuote);
//            DataConstant.buyList.removeElement(buyQuote);
            stTradeQueueMap.removeBuyStQuote(buyQuote);
            stTradeQueueMap.removeSellStQuote(sellQuote);
        }else if(buyQuote.getCurrentAmount().intValue() > sellQuote.getCurrentAmount().intValue()){//买多卖少

            //股票交易数量为卖家卖掉数量
            tradeStockAmount = sellQuote.getCurrentAmount();
            //处理交易
            trading(buyQuote, sellQuote, tradeStockAmount, sts);

            //买家报价剩余股票数量
            int remainAmount = buyQuote.getCurrentAmount()-tradeStockAmount;

            //交易成功，交易买家报价金额减少
            Map<String, Object> buymap = buyOrSellStockMoney(buyQuote.getQuotePrice(), remainAmount, buyQuote.getType());
            buyQuote.setFrozeMoney((double) buymap.get("figureMoney"));
            buyQuote.setCommissionFee((double) buymap.get("commissionFee"));

            //交易成功，股票交易数量减少
            buyQuote.setCurrentAmount(remainAmount);

            //卖家移出队列
//            DataConstant.sellList.removeElement(sellQuote);
            stTradeQueueMap.removeSellStQuote(sellQuote);
        }
    }


    @Override
    public boolean settleResult(){
        for (String key : DataConstant.stTradeQueueMap.keySet()) {
            StTradeQueue stTradeQueueMap = DataConstant.stTradeQueueMap.get(key);

            if (stTradeQueueMap.buyList.isEmpty() && stTradeQueueMap.sellList.isEmpty()) continue;

            //卖家结算
            for(Long bkey: stTradeQueueMap.sellList.keySet()){
                StQuote stq = (StQuote) stTradeQueueMap.sellList.get(bkey);

                if (stq==null) {
                    continue;
                }

                //撤回托管为卖的股票，持仓数量增加
                stPositionServiceI.operateStPosition(stq.getAccountId(), stq.getStockId(), stq.getCurrentAmount(), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

                StStock sto = DataConstant.stockTable.get(stq.getStockId());

                logger.info("卖家结算--卖家->" + stq.getAccountId() + "--股票名称->" + sto.getStockName() + "--股票编码->" + sto.getStockCode() + "--退还股票数--" +stq.getCurrentAmount());
            }

            //买家结算，退还未交易报价费用
            for(Long skey: stTradeQueueMap.buyList.keySet()){

                StQuote stqb = (StQuote)stTradeQueueMap.buyList.get(skey);

                if (stqb==null) {
                    continue;
                }

                //撤回托管买股票费用,帐户增加资产
                stAccountServiceI.opearteUseMoney(stqb.getAccountId(), stqb.getFrozeMoney(), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

                StStock sto = DataConstant.stockTable.get(stqb.getStockId());

                logger.info("买家结算--买家->"+stqb.getAccountId()+"--交易股票->"+sto.getStockName()+"--股票编码->"+sto.getStockCode()+"--退还金额->"+stqb.getFrozeMoney());
            }

            //清除结算完股票买卖队列
            DataConstant.stTradeQueueMap.remove(key);
        }
        return true;
    }


    @Override
    public boolean cancelStQuote(StQuote stQuote){
        boolean rs = false;
        //获取委托信息
        Map<String,StQuote> stQuoteMap = DataConstant.stAccountQuoteMap.get(stQuote.getAccountId());
        if (stQuoteMap==null) {
            return rs;
        }

        //被撤销的报价
        StQuote cancelQuote = stQuoteMap.get(stQuote.getQuoteId());

        //获取股票对应队列
        StTradeQueue stTradeQueue = DataConstant.stTradeQueueMap.get(stQuote.getStockId());
        if (stTradeQueue==null) {
            return rs;
        }

        if(cancelQuote.getStatus().intValue()==1){
            if(stQuote.getType().intValue() == Constant.STOCK_STQUOTE_TYPE_BUY.intValue()){
                rs = stTradeQueue.removeBuyStQuote(cancelQuote);
            } else if(stQuote.getType().intValue() == Constant.STOCK_STQUOTE_TYPE_SELL){
                rs = stTradeQueue.removeSellStQuote(cancelQuote);
            }
        }

        if(rs){
            stQuoteMap.remove(cancelQuote.getQuoteId(),cancelQuote);
        }

        return rs;
    }

    @Override
    public boolean quotePriceBySimulation(List<StAccount> stAccountList, StStock stStock) {
        if (stAccountList.isEmpty()) return false;
        StAccount account = stAccountList.get(0);

        double[] priceArr = new double[10];
        int[] amountArr = new int[10];
        int[] typeArr = new int[10];

        priceArr[0]=stStock.getBuyOnePrice();
        priceArr[1]=stStock.getBuyTwoPrice();
        priceArr[2]=stStock.getBuyThreePrice();
        priceArr[3]=stStock.getBuyFourPrice();
        priceArr[4]=stStock.getBuyFivePrice();
        priceArr[5]=stStock.getSellOnePrice();
        priceArr[6]=stStock.getSellTwoPrice();
        priceArr[7]=stStock.getSellThreePrice();
        priceArr[8]=stStock.getSellFourPrice();
        priceArr[9]=stStock.getSellFivePrice();

        amountArr[0]=stStock.getBuyOneAmount();
        amountArr[1]=stStock.getBuyTwoAmount();
        amountArr[2]=stStock.getBuyThreeAmount();
        amountArr[3]=stStock.getBuyFourAmount();
        amountArr[4]=stStock.getBuyFiveAmount();
        amountArr[5]=stStock.getSellOneAmount();
        amountArr[6]=stStock.getSellTwoAmount();
        amountArr[7]=stStock.getSellThreeAmount();
        amountArr[8]=stStock.getSellFourAmount();
        amountArr[9]=stStock.getSellFiveAmount();

        typeArr[0] = Constant.STOCK_STQUOTE_TYPE_BUY;
        typeArr[1] = Constant.STOCK_STQUOTE_TYPE_BUY;
        typeArr[2] = Constant.STOCK_STQUOTE_TYPE_BUY;
        typeArr[3] = Constant.STOCK_STQUOTE_TYPE_BUY;
        typeArr[4] = Constant.STOCK_STQUOTE_TYPE_BUY;
        typeArr[5] = Constant.STOCK_STQUOTE_TYPE_SELL;
        typeArr[6] = Constant.STOCK_STQUOTE_TYPE_SELL;
        typeArr[7] = Constant.STOCK_STQUOTE_TYPE_SELL;
        typeArr[8] = Constant.STOCK_STQUOTE_TYPE_SELL;
        typeArr[9] = Constant.STOCK_STQUOTE_TYPE_SELL;

        for (int i = 0; i< priceArr.length; i++) {
            if (amountArr[i]==0||priceArr[i]==0||typeArr[i]==0) continue;

            StQuote s = new StQuote();
            s.setAccountId(account.getAccountId());
            s.setStockId(stStock.getStockId());
            s.setQuotePrice(priceArr[i]);
            s.setAmount(amountArr[i]);
            s.setType(typeArr[i]);
            s.setDateTime(System.currentTimeMillis());
            this.quotePrice(s);
        }

        return true;
    }

    @Override
    public void updateSyncStockInfo() {
        logger.info("更新股票实时信息.............start...........");
        StStock stock = null;
        for (String k :DataConstant.stockTable.keySet()) {
            StStock stStock = DataConstant.stockTable.get(k);
            stock = stockGetInfoFromApiI.getStStockInfo(stStock.getStockType(), stStock.getStockCode());
            logger.info("股票信息：" + stock);
            if (stock!=null) {
                stock.setStockId(stStock.getStockId());
                stock.setStockType(stStock.getStockType());
                DataConstant.stockTable.put(stStock.getStockId(), stock);
            }
        }
        logger.info("更新股票实时信息.............end...........");
    }

    public void quotePriceBySimulation() {
        logger.info("增加马甲盘.............start...........");
        this.updateSyncStockInfo();
        for (String k :DataConstant.stockTable.keySet()) {
            StStock stock = DataConstant.stockTable.get(k);
            logger.info("股票信息：" + stock);
            if (stock!=null) {
                this.quotePriceBySimulation(DataConstant.accountList, stock);
            }
        }

        logger.info("增加马甲盘.............end...........");
    }

    /**
     * 是否在范围内报价
     * @param closePrice 昨日收盘价
     * @param quotePrice 当前报价
     * @return
     */
    private boolean isStockQuotePriceInScope(double closePrice, double quotePrice){

        Map<String, Object> rs = new HashMap<String, Object>();

        double extent = ArithUtil.multiply(closePrice, Constant.STOCK_UP_AND_DOWN_PERCENT);

        double maxPrice = ArithUtil.add(closePrice, extent);
        double minPrice = ArithUtil.subtract(closePrice,extent);

        //报价大于等于最小价格，小于等于最大价格，可以正常报价
        if((ArithUtil.compare(quotePrice, minPrice)>=0) && (ArithUtil.compare(quotePrice, maxPrice)<=0)){
            return true;
        }else{
            logger.info("报价失败-----报价价格->" + quotePrice + "--最高涨幅->" + maxPrice + "--最低跌幅->" + minPrice);
        }

        return false;
    }

    /**
     * 交易处理
     * @param buyQuote
     * @param sellQuote
     * @param tradeStockAmount
     * @param sts
     */
    private synchronized void trading(StQuote buyQuote, StQuote sellQuote, int tradeStockAmount, StStock sts){

        //股票交易价格
        double tradeStockQuotePrice = 0d;

        tradeStockQuotePrice = judgeQuotePrice(buyQuote, sellQuote, tradeStockAmount);

        //交易成功，交易买家持仓增加
        stPositionServiceI.operateStPosition(buyQuote.getAccountId(), buyQuote.getStockId(), tradeStockAmount, Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

        //卖家新增费用计算
        Map<String, Object> sellmap = buyOrSellStockMoney(tradeStockQuotePrice, tradeStockAmount, sellQuote.getType());

        //交易成功，交易卖家资产增加
        stAccountServiceI.opearteUseMoney(sellQuote.getAccountId(), (double) sellmap.get("figureMoney"), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

        double fee = ArithUtil.multiply((double) sellmap.get("commissionFee"), 2);
        //收取买家卖家佣金和卖家印花税
        DataConstant.STOCK_TRADE_AGENT_MONEY = DataConstant.STOCK_TRADE_AGENT_MONEY.doubleValue() + ArithUtil.add(fee,(double) sellmap.get("bstampFax"));

        //修改交易状态,变为交易中
        buyQuote.setStatus(Constant.STOCK_STPOSITION_STATUS_DEAL);
        sellQuote.setStatus(Constant.STOCK_STPOSITION_STATUS_DEAL);

        //添加交易记录
        StTradeRecord str = new StTradeRecord();
        str.setId(UUID.randomUUID().toString());
        str.setBuyerAccountId(buyQuote.getAccountId());
        str.setSellerAccountId(sellQuote.getAccountId());
        str.setAmount(tradeStockAmount);
        str.setStockId(sts.getStockId());
        str.setQuotePrice(tradeStockQuotePrice);
        str.setDealMoney(ArithUtil.multiply(tradeStockQuotePrice, tradeStockAmount));
        str.setDealFee(fee);
        str.setDealTax((double) sellmap.get("bstampFax"));
        str.setDateTime(System.currentTimeMillis());
        str.setStockCode(sts.getStockCode());

        //交易记录列表
        stTradeRecordServiceI.addStTradeRecord(str);

        logger.info("交易--买家->" + buyQuote.getAccountId() + "-和-卖家->" + sellQuote.getAccountId() + "--交易成功--" + "--交易股票->" + sts.getStockName() + "--股票编码->" + sts.getStockCode() + "--交易价格->" + tradeStockQuotePrice + "-交易数量->" + tradeStockAmount + "-交易总额->" + ArithUtil.multiply(tradeStockQuotePrice,tradeStockAmount) + "-买家卖家佣金->" + fee + "-印花税->" + +(double) sellmap.get("bstampFax"));
    }


    private synchronized Map<String, Object> buyOrSellStockMoney(Double qutoPrice, Integer amount, Integer type) {

        Map<String, Object> rs = new HashMap<String, Object>();

        double figureMoney = 0d;
        //金额
        double volMoney = 0d;

        volMoney = ArithUtil.multiply(qutoPrice,amount);

        //佣金
        double commissionFee = 0d;
        //印花税
        double bstampFax = 0d;
        //计算佣金
        commissionFee =  ArithUtil.multiply(volMoney,Constant.STOCK_COMMINSSION_MONEY_PERCENT);
        //买股票
        if (type.intValue() == Constant.STOCK_STQUOTE_TYPE_BUY.intValue()) {

            figureMoney = ArithUtil.add(volMoney, commissionFee);

        }else if (type.intValue() == Constant.STOCK_STQUOTE_TYPE_SELL.intValue()) {//卖股票
            //计算印花税
            bstampFax =  ArithUtil.multiply(volMoney, Constant.STOCK_STAMP_TAX_PERCENT);

            figureMoney =  ArithUtil.subtract(volMoney, ArithUtil.add(commissionFee, bstampFax));

        }else{}

        rs.put("figureMoney",figureMoney);
        rs.put("volMoney",volMoney);
        rs.put("commissionFee",commissionFee);
        rs.put("bstampFax", bstampFax);

        return rs;
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
        if(ArithUtil.compare(buyQuote.getQuotePrice(), sellQuote.getQuotePrice())==1){
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
                saveMoney = ArithUtil.subtract((double) buymap.get("figureMoney"), (double) dealmap.get("figureMoney"));
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



    public boolean settleResultAcount(){
        //卖家结算
        LinkedList<StQuote> sellLinkList = DataConstant.sellList.getList();
        Iterator iterator = sellLinkList.iterator();
        while (iterator.hasNext())
        {
            StQuote stq = (StQuote)iterator.next();

            //撤回托管为卖的股票，持仓数量增加
            stPositionServiceI.operateStPosition(stq.getAccountId(), stq.getStockId(), stq.getCurrentAmount(), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

            StStock sto = DataConstant.stockTable.get(stq.getStockId());

            logger.info("卖家结算--卖家->" + stq.getAccountId() + "--股票名称->" + sto.getStockName() + "--股票编码->" + sto.getStockCode() + "--退还股票数--" +stq.getCurrentAmount());
        }
        //清除卖家队列
        DataConstant.sellList.clear();


        //买家结算，退还未交易报价费用
        LinkedList<StQuote> buyLinkList =  DataConstant.buyList.getList();
        Iterator iteratorb = buyLinkList.iterator();
        while (iteratorb.hasNext())
        {
            StQuote stqb = (StQuote)iteratorb.next();

            //撤回托管买股票费用,帐户增加资产
            stAccountServiceI.opearteUseMoney(stqb.getAccountId(), stqb.getFrozeMoney(), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

            StStock sto = DataConstant.stockTable.get(stqb.getStockId());

            logger.info("买家结算--买家->"+stqb.getAccountId()+"--交易股票->"+sto.getStockName()+"--股票编码->"+sto.getStockCode()+"--退还金额->"+stqb.getFrozeMoney());
        }
        //清除买家队列
        DataConstant.buyList.clear();

        return true;
    }

}

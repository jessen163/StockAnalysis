package com.ryd.stockanalysis.common;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.handle.StockTradeThread;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.util.ArithUtil;
import com.ryd.stockanalysis.util.FestivalDateUtil;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.common
 * 创建人：songby
 * 创建时间：2016/3/30 10:33
 */
public class DataInitTool {

    private static Logger logger = Logger.getLogger(DataInitTool.class);

    public static boolean dataCheck(String disinfo){

        double totalUseMoney = 0d;
        double totalAllMoney = 0d;
        Map<String,Object> stmap = new HashMap<String,Object>();
        //用户信息
        for(String key: DataConstant.stAccounts.keySet()){
            StAccount uu = DataConstant.stAccounts.get(key);
            totalUseMoney = totalUseMoney + uu.getUseMoney();
            totalAllMoney = totalAllMoney + uu.getTotalMoney();
            //获取对应的持仓
            Map<String,StPosition> stpMap = DataConstant.stAccountPositionMap.get(uu.getAccountId());
            for(String keys:stpMap.keySet()) {
                StPosition sstp = stpMap.get(keys);
                Integer amount = (Integer)stmap.get(sstp.getStockId());
                if(amount==null){
                    stmap.put(sstp.getStockId(), sstp.getAmount());
                }else{
                    stmap.put(sstp.getStockId(), sstp.getAmount()+amount);
                }
            }
        }

        logger.info(disinfo+"--所有资产总金额->"+totalAllMoney+"--所有资产总可用金额->"+totalUseMoney);

        if("settle结算后".equals(disinfo)){
            logger.info(disinfo+"--交易费用总金额->"+DataConstant.STOCK_TRADE_AGENT_MONEY);
        }

        for(String skey: DataConstant.stockTable.keySet()) {
            StStock st = DataConstant.stockTable.get(skey);
            Integer amount = (Integer) stmap.get(skey);
            if(amount==null){
                amount = 0;
            }
            if (st != null) {
                logger.info(disinfo + "--股票->" + st.getStockName() + "--股票编码->" + st.getStockCode() + "--总数量->" + amount);
            }
        }

        return false;
    }

    /**
     * 创建基础数据 初始帐户
     * 股票：
     * "1","中国平安","633256"
     * "2","广发证券","000776"
     * "3","创业板","150153"
     * 帐户 A 持有 中国平安 1000股
     * 帐户 B 持有 广发证券 10000
     * 帐户 C 持有 中国平安 100000 广发证券 100000
     * 帐户 D 持有 中国平安 100000
     * 帐户 E 持有 中国平安 100000
     */
    public static boolean createBaseData() {

        //中国平安-股票
        StStock stStock = new StStock("1","中国平安","633256");
        stStock.setBfclosePrice(10);
        StStock stStock2 = new StStock("2","广发证券","000776");
        stStock2.setBfclosePrice(10);
        StStock stStock3 = new StStock("3","创业板A","150153");
        DataConstant.stockTable.put(stStock.getStockId(),stStock);
        DataConstant.stockTable.put(stStock2.getStockId(),stStock2);
        DataConstant.stockTable.put(stStock3.getStockId(),stStock3);
        //初始数据用户A、B为卖家拥有持仓，用户C、D、E为买家，持仓为空

        //创建买家A
        StAccount ataA = new StAccount("A","A","1",1000000d,2000000d);
        //用户A持仓
        StPosition ata1Pos = new StPosition();
        ata1Pos.setPositionId(UUID.randomUUID().toString());
        ata1Pos.setAccountId(ataA.getAccountId());
        ata1Pos.setStockId(stStock.getStockId());
        ata1Pos.setStStock(stStock);
        ata1Pos.setAmount(1000);
        ata1Pos.setStatus(1);

        //获取对应的持仓
        Map<String,StPosition> stpMapA = new HashMap<String,StPosition>();
        stpMapA.put(ata1Pos.getStockId(), ata1Pos);
        DataConstant.stAccountPositionMap.put(ataA.getAccountId(), stpMapA);

        //创建买家B
        StAccount ataB = new StAccount("B","B","2",1000000d,2000000d);
        //用户B持仓
        StPosition ata2Pos = new StPosition();
        ata2Pos.setPositionId(UUID.randomUUID().toString());
        ata2Pos.setAccountId(ataB.getAccountId());
        ata2Pos.setStockId(stStock2.getStockId());
        ata2Pos.setStStock(stStock2);
        ata2Pos.setAmount(10000);
        ata2Pos.setStatus(1);

        //获取对应的持仓
        Map<String,StPosition> stpMapB = new HashMap<String,StPosition>();
        stpMapB.put(ata2Pos.getStockId(), ata2Pos);
        DataConstant.stAccountPositionMap.put(ataB.getAccountId(), stpMapB);


        StAccount ataC = new StAccount("C","C","3",10000d,1000000d);
        //用户C持仓 中国平安
        StPosition atc1Pos = new StPosition();
        atc1Pos.setPositionId(UUID.randomUUID().toString());
        atc1Pos.setAccountId(ataC.getAccountId());
        atc1Pos.setStockId(stStock.getStockId());
        atc1Pos.setStStock(stStock);
        atc1Pos.setAmount(100000);
        atc1Pos.setStatus(1);

        Map<String,StPosition> stpMapC = new HashMap<String,StPosition>();
        stpMapC.put(atc1Pos.getStockId(), atc1Pos);

        //用户C持仓 广发证券
        StPosition atc2Pos = new StPosition();
        atc2Pos.setPositionId(UUID.randomUUID().toString());
        atc2Pos.setAccountId(ataC.getAccountId());
        atc2Pos.setStockId(stStock2.getStockId());
        atc2Pos.setStStock(stStock2);
        atc2Pos.setAmount(100000);
        atc2Pos.setStatus(1);

        stpMapC.put(atc2Pos.getStockId(), atc2Pos);
        DataConstant.stAccountPositionMap.put(ataC.getAccountId(), stpMapC);


        StAccount ataD = new StAccount("D","D","4",10000d,1000000d);
        //用户D持仓
        StPosition ata4Pos = new StPosition();
        ata4Pos.setPositionId(UUID.randomUUID().toString());
        ata4Pos.setAccountId(ataD.getAccountId());
        ata4Pos.setStockId(stStock.getStockId());
        ata4Pos.setStStock(stStock);
        ata4Pos.setAmount(100000);
        ata4Pos.setStatus(1);

        //获取对应的持仓
        Map<String,StPosition> stpMapD = new HashMap<String,StPosition>();
        stpMapD.put(ata4Pos.getStockId(), ata4Pos);
        DataConstant.stAccountPositionMap.put(ataD.getAccountId(), stpMapD);

        StAccount ataE = new StAccount("E","E","5",10000d,1000000d);
        //用户E持仓
        StPosition ata5Pos = new StPosition();
        ata5Pos.setPositionId(UUID.randomUUID().toString());
        ata5Pos.setAccountId(ataE.getAccountId());
        ata5Pos.setStockId(stStock.getStockId());
        ata5Pos.setStStock(stStock);
        ata5Pos.setAmount(100000);
        ata5Pos.setStatus(1);

        //获取对应的持仓
        Map<String,StPosition> stpMapE = new HashMap<String,StPosition>();
        stpMapE.put(ata5Pos.getStockId(), ata5Pos);
        DataConstant.stAccountPositionMap.put(ataE.getAccountId(), stpMapE);

        DataConstant.stAccounts.put(ataA.getAccountId(),ataA);
        DataConstant.stAccounts.put(ataB.getAccountId(),ataB);
        DataConstant.stAccounts.put(ataC.getAccountId(),ataC);
        DataConstant.stAccounts.put(ataD.getAccountId(),ataD);
        DataConstant.stAccounts.put(ataE.getAccountId(),ataE);

        return true;
    }

    public static void printAccountInfo(StAccount uu,String pinfo){

        StringBuffer sbu = new StringBuffer(pinfo);

        sbu.append("帐户信息--帐号->" + uu.getAccountName());
        sbu.append("--可使用金额->->"+uu.getUseMoney());
        sbu.append("--帐户总金额->" + uu.getTotalMoney());

        logger.info(sbu.toString());
        //获取对应的持仓
        Map<String,StPosition> stpMap = DataConstant.stAccountPositionMap.get(uu.getAccountId());
        for(String key : stpMap.keySet()) {
            StPosition sp = stpMap.get(key);
            StStock st = sp.getStStock();
            StringBuffer sb = new StringBuffer(pinfo);
            sb.append("帐户信息--帐号->" + uu.getAccountName());
            sb.append("--持有股票编码->" + st.getStockCode());
            sb.append("--持有股票名称->" + st.getStockName());
            sb.append("--持仓数->" + sp.getAmount());

            logger.info(sb.toString());
        }
    }

    //初始化报价信息
    public static void initQuotePriceMap(ExecutorService pool,StockAnalysisServiceI serviceI){
        //判断时间是否允许报价
        int tstatus = FestivalDateUtil.getInstance().dateJudge();
        if(tstatus == Constant.STQUOTE_TRADE_TIMECOMPARE_1 || tstatus == Constant.STQUOTE_TRADE_TIMECOMPARE_2) {
            //交易股票
            StStock st = DataConstant.stockTable.get(DataConstant.TRADEING_STOCK_ID);
            try {
                //买家A
                StAccount aSt = DataConstant.stAccounts.get("A");
                ConcurrentMap<String, Map> aQuoteTable = new ConcurrentHashMap<String, Map>();
                for (int i = 1; i <= DataConstant.STQUOTE_A_NUM; i++) {
                    Map<String, Object> rtn = new HashMap<String, Object>();
                    rtn.put("accountId", aSt.getAccountId());
                    rtn.put("stockId", DataConstant.TRADEING_STOCK_ID);
                    rtn.put("quotePrice", DataConstant.STQUOTE_A_QUOTEPRICE);
                    rtn.put("amount", DataConstant.STQUOTE_A_AMOUNT);
                    rtn.put("type", Constant.STOCK_STQUOTE_TYPE_BUY);

                    rtn.put("info", "当前报价----报价状态->买入---买方->" + aSt.getAccountName() + "--股票名称->" + st.getStockName() + "--股票编码->" + st.getStockCode() + "--委托价格->" + DataConstant.STQUOTE_A_QUOTEPRICE + "--委托数量->" + DataConstant.STQUOTE_A_AMOUNT + "--报价次数第-->" + i + "次");

                    aQuoteTable.put("A" + i, rtn);
                    //买卖家报
                }
                pool.execute(new StockTradeThread(serviceI, aQuoteTable));


                //卖家C
                StAccount cSt = DataConstant.stAccounts.get("C");
                ConcurrentMap<String, Map> cQuoteTable = new ConcurrentHashMap<String, Map>();
                for (int ci = 1; ci <= DataConstant.STQUOTE_C_NUM; ci++) {
                    Map<String, Object> rtn = new HashMap<String, Object>();
                    rtn.put("accountId", cSt.getAccountId());
                    //卖两支股票，奇数卖广发银行，偶数卖中国平安
                    //            if(ci%2==0) {
                    rtn.put("stockId", DataConstant.TRADEING_STOCK_ID);
                    //            }else{
                    //                rtn.put("stockId", Constant.TRADEING_STOCK_ID2);
                    //            }
                    rtn.put("quotePrice", DataConstant.STQUOTE_C_QUOTEPRICE);
                    rtn.put("amount", DataConstant.STQUOTE_C_AMOUNT);
                    rtn.put("type", Constant.STOCK_STQUOTE_TYPE_SELL);

                    rtn.put("info", "当前报价----报价状态->卖出---卖方->" + cSt.getAccountName() + "--股票名称->" + st.getStockName() + "--股票编码->" + st.getStockCode() + "--委托价格->" + DataConstant.STQUOTE_C_QUOTEPRICE + "--委托数量->" + DataConstant.STQUOTE_C_AMOUNT + "--报价次数第-->" + ci + "次");

                    cQuoteTable.put("C" + ci, rtn);

                }
                Thread.sleep(100);
                pool.execute(new StockTradeThread(serviceI, cQuoteTable));

                //卖家D
                StAccount dSt = DataConstant.stAccounts.get("D");
                ConcurrentMap<String, Map> dQuoteTable = new ConcurrentHashMap<String, Map>();
                for (int di = 1; di <= DataConstant.STQUOTE_D_NUM; di++) {
                    Map<String, Object> rtn = new HashMap<String, Object>();
                    rtn.put("accountId", dSt.getAccountId());
                    rtn.put("stockId", DataConstant.TRADEING_STOCK_ID);
                    rtn.put("quotePrice", DataConstant.STQUOTE_D_QUOTEPRICE);
                    rtn.put("amount", DataConstant.STQUOTE_D_AMOUNT);
                    rtn.put("type", Constant.STOCK_STQUOTE_TYPE_SELL);

                    rtn.put("info", "当前报价----报价状态->卖出---卖方->" + dSt.getAccountName() + "--股票名称->" + st.getStockName() + "--股票编码->" + st.getStockCode() + "--委托价格->" + DataConstant.STQUOTE_D_QUOTEPRICE + "--委托数量->" + DataConstant.STQUOTE_D_AMOUNT + "--报价次数第-->" + di + "次");

                    dQuoteTable.put("D" + di, rtn);

                }
                Thread.sleep(100);
                pool.execute(new StockTradeThread(serviceI, dQuoteTable));

                //卖家E
                StAccount eSt = DataConstant.stAccounts.get("E");
                ConcurrentMap<String, Map> eQuoteTable = new ConcurrentHashMap<String, Map>();
                for (int ei = 1; ei <= DataConstant.STQUOTE_E_NUM; ei++) {
                    Map<String, Object> rtn = new HashMap<String, Object>();
                    rtn.put("accountId", eSt.getAccountId());
                    rtn.put("stockId", DataConstant.TRADEING_STOCK_ID);
                    rtn.put("quotePrice", DataConstant.STQUOTE_E_QUOTEPRICE);
                    rtn.put("amount", DataConstant.STQUOTE_E_AMOUNT);
                    rtn.put("type", Constant.STOCK_STQUOTE_TYPE_SELL);

                    rtn.put("info", "当前报价----报价状态->卖出---卖方->" + eSt.getAccountName() + "--股票名称->" + st.getStockName() + "--股票编码->" + st.getStockCode() + "--委托价格->" + DataConstant.STQUOTE_E_QUOTEPRICE + "--委托数量->" + DataConstant.STQUOTE_E_AMOUNT + "--报价次数第-->" + ei + "次");

                    eQuoteTable.put("E" + ei, rtn);

                }
                Thread.sleep(100);
                pool.execute(new StockTradeThread(serviceI, eQuoteTable));

                //买家B
                StAccount bSt = DataConstant.stAccounts.get("B");
                ConcurrentMap<String, Map> bQuoteTable = new ConcurrentHashMap<String, Map>();
                for (int bi = 1; bi <= DataConstant.STQUOTE_B_NUM; bi++) {
                    Map<String, Object> rtn = new HashMap<String, Object>();
                    rtn.put("accountId", bSt.getAccountId());
                    //买两支股票，奇数买广发银行，偶数买中国平安
                    //            if(bi%2==0) {
                    rtn.put("stockId", DataConstant.TRADEING_STOCK_ID);
                    //            }else{
                    //                rtn.put("stockId", Constant.TRADEING_STOCK_ID2);
                    //            }
                    rtn.put("quotePrice", DataConstant.STQUOTE_B_QUOTEPRICE);
                    rtn.put("amount", DataConstant.STQUOTE_B_AMOUNT);
                    rtn.put("type", Constant.STOCK_STQUOTE_TYPE_BUY);

                    rtn.put("info", "当前报价----报价状态->买入---买方->" + bSt.getAccountName() + "--股票名称->" + st.getStockName() + "--股票编码->" + st.getStockCode() + "--委托价格->" + DataConstant.STQUOTE_B_QUOTEPRICE + "--委托数量->" + DataConstant.STQUOTE_B_AMOUNT + "--报价次数第-->" + bi + "次");

                    bQuoteTable.put("B" + bi, rtn);
                }
                Thread.sleep(100);
                pool.execute(new StockTradeThread(serviceI, bQuoteTable));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static synchronized void printTradeQueue(String info,String stockId) {

            StTradeQueue stTradeQueueMap = DataConstant.stTradeQueueMap.get(stockId);

            if (stTradeQueueMap.buyList.isEmpty() && stTradeQueueMap.sellList.isEmpty()) return;

            logger.info("-----------------------------"+info+" start------------------------------------");
            //卖家队列
            for (Long bkey : stTradeQueueMap.sellList.keySet()) {
                StQuote stq = (StQuote) stTradeQueueMap.sellList.get(bkey);

                if (stq == null) {
                    continue;
                }
                StStock sst = DataConstant.stockTable.get(stq.getStockId());

                logger.info("卖家队列---" + stq.getAccountId() + "--股票-" + sst.getStockName() + "--价格-" + stq.getQuotePrice() + "--报价时间-" + new Date(stq.getDateTime()) + "--报价时间-" + stq.getDateTime());
            }

            logger.info("  ");

            //买家队列
            for (Long skey : stTradeQueueMap.buyList.keySet()) {

                StQuote stqb = (StQuote) stTradeQueueMap.buyList.get(skey);

                if (stqb == null) {
                    continue;
                }

                StStock sstb = DataConstant.stockTable.get(stqb.getStockId());

                logger.info("买家队列---" + stqb.getAccountId() + "--股票-" + sstb.getStockName() + "--价格-" + stqb.getQuotePrice() + "--报价时间-" + new Date(stqb.getDateTime()) + "--报价时间-" + stqb.getDateTime());
            }
            logger.info("-----------------------------"+info+" end------------------------------------");
    }
}

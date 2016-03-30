package com.ryd.stockanalysis.common;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StPosition;
import com.ryd.stockanalysis.bean.StStock;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

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
        for(String key: Constant.stAccounts.keySet()){
            StAccount uu = Constant.stAccounts.get(key);
            totalUseMoney = totalUseMoney + uu.getUseMoney();
            totalAllMoney = totalAllMoney + uu.getTotalMoney();
            for(StPosition sstp:uu.getStPositionList()) {
                Integer amount = (Integer)stmap.get(sstp.getStockId());
                if(amount==null){
                    stmap.put(sstp.getStockId(), sstp.getAmount());
                }else{
                    stmap.put(sstp.getStockId(), sstp.getAmount()+amount);
                }
            }
        }

        logger.info(disinfo+"--所有资产总金额->"+totalAllMoney+"--所有资产总可用金额->"+totalUseMoney);
        for(String skey: stmap.keySet()) {
            StStock st = Constant.stockTable.get(skey);
            int amount = (int) stmap.get(skey);
            if (st != null) {
                logger.info(disinfo + "--股票->" + st.getStockName() + "--股票编码->" + st.getStockCode() + "--总数量->" + amount);
            }
        }
        return false;
    }

    //创建基础数据
    public static boolean createBaseData() {

        //中国平安-股票
        StStock stStock = new StStock("1","中国平安","633256");
        Constant.stockTable.put(stStock.getStockId(),stStock);

        //初始数据用户A、B为卖家拥有持仓，用户C、D、E为买家，持仓为空

        //创建卖家A
        StAccount ataA = new StAccount("A","A","1",60000d,100000d);
        //用户A持仓
        StPosition ata1Pos = new StPosition();
        ata1Pos.setPositionId(UUID.randomUUID().toString());
        ata1Pos.setAccountId(ataA.getAccountId());
        ata1Pos.setStockId(stStock.getStockId());
        ata1Pos.setStStock(stStock);
        ata1Pos.setAmount(1000);
        ata1Pos.setStatus(1);

        ataA.getStPositionList().add(ata1Pos);

        //创建卖家B
        StAccount ataB = new StAccount("B","B","2",60000d,100000d);
        //用户B持仓
        StPosition ata2Pos = new StPosition();
        ata2Pos.setPositionId(UUID.randomUUID().toString());
        ata2Pos.setAccountId(ataB.getAccountId());
        ata2Pos.setStockId(stStock.getStockId());
        ata2Pos.setStStock(stStock);
        ata2Pos.setAmount(0);
        ata2Pos.setStatus(1);

        ataB.getStPositionList().add(ata2Pos);

        StAccount ataC = new StAccount("C","C","3",40000d,100000d);
        //用户C持仓
        StPosition ata3Pos = new StPosition();
        ata3Pos.setPositionId(UUID.randomUUID().toString());
        ata3Pos.setAccountId(ataC.getAccountId());
        ata3Pos.setStockId(stStock.getStockId());
        ata3Pos.setStStock(stStock);
        ata3Pos.setAmount(10000);
        ata3Pos.setStatus(1);

        ataC.getStPositionList().add(ata3Pos);

        StAccount ataD = new StAccount("D","D","4",40000d,100000d);
        //用户D持仓
        StPosition ata4Pos = new StPosition();
        ata4Pos.setPositionId(UUID.randomUUID().toString());
        ata4Pos.setAccountId(ataD.getAccountId());
        ata4Pos.setStockId(stStock.getStockId());
        ata4Pos.setStStock(stStock);
        ata4Pos.setAmount(10000);
        ata4Pos.setStatus(1);

        ataD.getStPositionList().add(ata4Pos);

        StAccount ataE = new StAccount("E","E","5",40000d,100000d);
        //用户E持仓
        StPosition ata5Pos = new StPosition();
        ata5Pos.setPositionId(UUID.randomUUID().toString());
        ata5Pos.setAccountId(ataE.getAccountId());
        ata5Pos.setStockId(stStock.getStockId());
        ata5Pos.setStStock(stStock);
        ata5Pos.setAmount(10000);
        ata5Pos.setStatus(1);

        ataE.getStPositionList().add(ata5Pos);

        Constant.stAccounts.put(ataA.getAccountId(),ataA);
        Constant.stAccounts.put(ataB.getAccountId(),ataB);
        Constant.stAccounts.put(ataC.getAccountId(),ataC);
        Constant.stAccounts.put(ataD.getAccountId(),ataD);
        Constant.stAccounts.put(ataE.getAccountId(),ataE);

        return true;
    }

    public static void printAccountInfo(StAccount uu,String pinfo){

        StringBuffer sb = new StringBuffer(pinfo);

        sb.append("帐户信息--帐号->" + uu.getAccountName());
        sb.append("--可使用金额->->"+uu.getUseMoney());
        sb.append("--帐户总金额->"+uu.getTotalMoney());


        if(CollectionUtils.isNotEmpty(uu.getStPositionList())) {
            StPosition sp = uu.getStPositionList().get(0);
            StStock st = sp.getStStock();
            sb.append("--持有股票编码->" + st.getStockCode());
            sb.append("--持有股票名称->" + st.getStockName());
            sb.append("--持仓数->" + sp.getAmount());
        }

        logger.info(sb.toString());
    }

    //初始化报价信息
    public static void initQuotePriceMap(){
        //交易股票
        StStock st = Constant.stockTable.get(Constant.TRADEING_STOCK_ID);

        //买家A
        StAccount aSt = Constant.stAccounts.get("A");
        for(int i=1; i<= Constant.STQUOTE_A_NUM;i++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",aSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_A_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_A_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_BUY);

            rtn.put("info","买家报价--报价次数-->"+i+"--买家->" + aSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--买家报价->" + Constant.STQUOTE_A_QUOTEPRICE+ "--购买数量->" + Constant.STQUOTE_A_AMOUNT);

            Constant.allQuoteTable.put("A"+i,rtn);
        }
        //买家B
        StAccount bSt = Constant.stAccounts.get("B");
        for(int bi=1; bi<= Constant.STQUOTE_B_NUM;bi++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",bSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_B_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_B_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_BUY);

            rtn.put("info","买家报价--报价次数-->"+bi+"--买家->" + bSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--买家报价->" + Constant.STQUOTE_B_QUOTEPRICE+ "--购买数量->" + Constant.STQUOTE_B_AMOUNT);

            Constant.allQuoteTable.put("B"+bi,rtn);
        }

        //卖家C
        StAccount cSt = Constant.stAccounts.get("C");
        for(int ci=1; ci<= Constant.STQUOTE_C_NUM;ci++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",cSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_C_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_C_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_SELL);

            rtn.put("info","卖家报价--报价次数-->"+ci+"--" + cSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--卖家报价->" +  Constant.STQUOTE_C_QUOTEPRICE+"--卖掉数量->" + Constant.STQUOTE_C_AMOUNT);

            Constant.allQuoteTable.put("C"+ci,rtn);
        }

        //卖家D
        StAccount dSt = Constant.stAccounts.get("D");
        for(int di=1; di<= Constant.STQUOTE_D_NUM;di++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",dSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_D_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_D_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_SELL);

            rtn.put("info","卖家报价--报价次数-->"+di+"--" + dSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--卖家报价->" +  Constant.STQUOTE_D_QUOTEPRICE+"--卖掉数量->" + Constant.STQUOTE_D_AMOUNT);

            Constant.allQuoteTable.put("D"+di,rtn);
        }

        //卖家E
        StAccount eSt = Constant.stAccounts.get("E");
        for(int ei=1; ei<= Constant.STQUOTE_E_NUM;ei++){
            Map<String,Object> rtn = new HashMap<String,Object>();
            rtn.put("accountId",eSt.getAccountId());
            rtn.put("stockId",Constant.TRADEING_STOCK_ID);
            rtn.put("quotePrice",Constant.STQUOTE_E_QUOTEPRICE);
            rtn.put("amount",Constant.STQUOTE_E_AMOUNT);
            rtn.put("type",Constant.STOCK_STQUOTE_TYPE_SELL);

            rtn.put("info","卖家报价--报价次数-->"+ei+"--" + eSt.getAccountName() + "--股票名称->" + st.getStockName()+"--股票编码->" + st.getStockCode() + "--卖家报价->" +  Constant.STQUOTE_E_QUOTEPRICE+"--卖掉数量->" + Constant.STQUOTE_E_AMOUNT);

            Constant.allQuoteTable.put("E"+ei,rtn);
        }
    }

}

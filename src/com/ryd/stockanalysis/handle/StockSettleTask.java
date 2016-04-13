package com.ryd.stockanalysis.handle;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.bean.StTradeRecord;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.common.DataInitTool;
import com.ryd.stockanalysis.service.StDateScheduleServiceI;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.service.impl.StDateScheduleServiceImpl;
import com.ryd.stockanalysis.service.impl.StockAnalysisServiceImpl;
import org.apache.log4j.Logger;

import java.util.TimerTask;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.handle
 * 创建人：songby
 * 创建时间：2016/4/13 9:59
 */
public class StockSettleTask extends TimerTask {

    private static Logger logger = Logger.getLogger(StockSettleTask.class);

    StockAnalysisServiceI serviceI;
    private StDateScheduleServiceI scheduleServiceI;

    public StockSettleTask(StockAnalysisServiceI serviceI) {
        this.serviceI = serviceI;
        scheduleServiceI = new StDateScheduleServiceImpl();
    }

    @Override
    public void run() {
        if(scheduleServiceI.dateIsWorkDayJudge()) {
            //结算前用户信息
            for (String key : DataConstant.stAccounts.keySet()) {
                StAccount uu = DataConstant.stAccounts.get(key);
                DataInitTool.printAccountInfo(uu, "结算前");
            }
            //结算信息
            serviceI.settleResult();

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

            logger.info("股票分析---------------结束---------------------");
        }
    }
}

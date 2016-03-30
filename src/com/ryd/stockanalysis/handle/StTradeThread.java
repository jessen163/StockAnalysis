package com.ryd.stockanalysis.handle;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.service.StAccountServiceI;
import com.ryd.stockanalysis.service.StPositionServiceI;
import com.ryd.stockanalysis.service.StTradeRecordServiceI;
import com.ryd.stockanalysis.service.impl.StAccountServiceImpl;
import com.ryd.stockanalysis.service.impl.StPositionServiceImpl;
import com.ryd.stockanalysis.service.impl.StTradeRecordServiceImpl;
import org.apache.log4j.Logger;

import com.ryd.stockanalysis.common.Constant;

import java.util.*;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis
 * 创建人：songby
 * 创建时间：2016/3/28 17:57
 */
public class StTradeThread implements Runnable {

    private static Logger logger = Logger.getLogger(StTradeThread.class);

	private StAccountServiceI stAccountServiceI;

	private StPositionServiceI stPositionServiceI;

	private StTradeRecordServiceI stTradeRecordServiceI;

	public StTradeThread() {
		stAccountServiceI = new StAccountServiceImpl();
		stPositionServiceI = new StPositionServiceImpl();
		stTradeRecordServiceI = new StTradeRecordServiceImpl();
	}

	@Override
    public void run(){
    	while (true) {
    		try {
    			if (!Constant.sellList.isEmpty()&&!Constant.sellList.isEmpty()) {

        			StQuote sellQuote = Constant.sellList.getLast();
        			StQuote buyQuote = Constant.buyList.getFrist();

					if (sellQuote==null||buyQuote==null) {
        				Thread.sleep(10);
        				continue;
        			}

					if (sellQuote.getStockId().equals(buyQuote.getStockId()) && sellQuote.getAmount()==buyQuote.getAmount()&&Double.doubleToLongBits(sellQuote.getQuotePrice())<=Double.doubleToLongBits(buyQuote.getQuotePrice())) {

        				//添加买入/卖出交易成功后的逻辑
						//撮合成功，买家增加股票持仓数量，卖家减钱

						//交易成功，交易买家持仓增加
						stPositionServiceI.operateStPosition(buyQuote.getAccountId(), buyQuote.getStockId(),buyQuote.getAmount(), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

						//卖家新增费用
						double oinmoney = buyQuote.getQuotePrice() * sellQuote.getAmount();

						//交易成功，交易卖家资产增加
						stAccountServiceI.opearteUseMoney(sellQuote.getAccountId(),oinmoney,Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

						//添加交易记录
						StTradeRecord str = new StTradeRecord();
						str.setId(UUID.randomUUID().toString());
						str.setBuyerAccountId(buyQuote.getAccountId());
						str.setSellerAccountId(sellQuote.getAccountId());
						str.setAmount(sellQuote.getAmount());
						str.setStockId(sellQuote.getStockId());
						str.setQuotePrice(buyQuote.getQuotePrice());
						str.setDealMoney(buyQuote.getQuotePrice()*sellQuote.getAmount());
						str.setDateTime(System.currentTimeMillis());

						//股票列表，设置股票编码
						StStock sts = Constant.stockTable.get(sellQuote.getStockId());

						if (sts != null) {
							str.setStockCode(sts.getStockCode());
						}

						//交易记录列表
						stTradeRecordServiceI.addStTradeRecord(str);

						logger.info("交易--买家->" + buyQuote.getAccountId() + "-和-卖家->" + sellQuote.getAccountId() + "--交易成功--"+"--交易股票->"+sts.getStockName() + "--股票编码->"+sts.getStockCode()+"--交易价格->" + buyQuote.getQuotePrice()+ "-交易数量->" + buyQuote.getAmount()+ "-交易总额->" + buyQuote.getQuotePrice()*sellQuote.getAmount());

						//移除记录
						Constant.sellList.removeElement(sellQuote);
						Constant.buyList.removeElement(buyQuote);
					}
        			Thread.sleep(10);
        		} else {
        			Thread.sleep(10);
        		}
    		} catch (Exception e) {
				e.printStackTrace();
			}
    	}

    }

}

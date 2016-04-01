package com.ryd.stockanalysis.handle;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.service.StAccountServiceI;
import com.ryd.stockanalysis.service.StPositionServiceI;
import com.ryd.stockanalysis.service.StTradeRecordServiceI;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.service.impl.StAccountServiceImpl;
import com.ryd.stockanalysis.service.impl.StPositionServiceImpl;
import com.ryd.stockanalysis.service.impl.StTradeRecordServiceImpl;
import com.ryd.stockanalysis.service.impl.StockAnalysisServiceImpl;
import org.apache.log4j.Logger;

import com.ryd.stockanalysis.common.Constant;

import java.math.BigDecimal;
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

	private StockAnalysisServiceI stockAnalysisServiceI;

	public StTradeThread() {
		stAccountServiceI = new StAccountServiceImpl();
		stPositionServiceI = new StPositionServiceImpl();
		stTradeRecordServiceI = new StTradeRecordServiceImpl();
		stockAnalysisServiceI = new StockAnalysisServiceImpl();
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

					if (sellQuote.getStockId().equals(buyQuote.getStockId()) && Double.doubleToLongBits(sellQuote.getQuotePrice())<=Double.doubleToLongBits(buyQuote.getQuotePrice())) {


						//股票
						StStock sts = Constant.stockTable.get(buyQuote.getStockId());
                        //交易
						stockAnalysisServiceI.dealTrading(buyQuote, sellQuote, sts);

					}
					Thread.sleep(10);
				} else {
					Thread.sleep(10);
				}
//				logger.info("股票交易引擎---------------开始--------------------");
//				if (!Constant.stTradeQueueMap.isEmpty()) {
////					synchronized (Constant.stTradeQueueMap) {
//						for (String s : Constant.stTradeQueueMap.keySet()) {
//							StTradeQueue stTradeQueueMap = Constant.stTradeQueueMap.get(s);
//							if (stTradeQueueMap.buyList.isEmpty() || stTradeQueueMap.sellList.isEmpty()) continue;
//							logger.info("stTradeQueueMap: 股票ID"+s+"-------------------其他信息："+stTradeQueueMap);
//							boolean flag = true;
//							Long buyerKey = 0L;
//							Long sellerKey = Long.MAX_VALUE;
//							logger.info("stTradeQueueMap："+stTradeQueueMap);
//							while (flag) {
//								StQuote sellQuote = stTradeQueueMap.getStQuote(sellerKey, 1);
//								StQuote buyQuote = stTradeQueueMap.getStQuote(buyerKey, 2);
//								if (sellQuote==null||buyQuote==null) {
//									flag = false;
//									break;
//								}
////								logger.info("buyQuote: 股票ID"+buyQuote.getStockId()+"-------------------其他信息："+buyQuote);
//								if (sellQuote.getQuotePrice().equals(buyQuote.getQuotePrice())) {
//									stTradeQueueMap.removeBuyStQuote(buyQuote);
//									stTradeQueueMap.removeSellStQuote(sellQuote);
//								}
//								buyerKey = buyQuote.getQuotePriceForSort();
//								sellerKey = sellQuote.getQuotePriceForSort();
//							}
//							logger.info("stTradeQueueMap："+stTradeQueueMap);
//
//							Constant.stTradeQueueMap.put(s, stTradeQueueMap);
//						}
////					}
//				}
//				logger.info("股票交易引擎---------------结束--------------------");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}

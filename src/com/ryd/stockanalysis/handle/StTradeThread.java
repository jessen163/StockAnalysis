package com.ryd.stockanalysis.handle;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.service.StAccountServiceI;
import com.ryd.stockanalysis.service.StPositionServiceI;
import com.ryd.stockanalysis.service.StTradeRecordServiceI;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.service.impl.StAccountServiceImpl;
import com.ryd.stockanalysis.service.impl.StPositionServiceImpl;
import com.ryd.stockanalysis.service.impl.StTradeRecordServiceImpl;
import com.ryd.stockanalysis.service.impl.StockAnalysisServiceImpl;
import com.ryd.stockanalysis.util.FestivalDateUtil;
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
			//判断时间是否允许交易
			int tstatus = FestivalDateUtil.getInstance().dateJudge();
			if(tstatus != Constant.STQUOTE_TRADE_TIMECOMPARE_1){
				return;
			}
			try {
				if (!DataConstant.sellList.isEmpty()&&!DataConstant.sellList.isEmpty()) {

					StQuote sellQuote = DataConstant.sellList.getLast();
					StQuote buyQuote = DataConstant.buyList.getFrist();
					if (sellQuote==null||buyQuote==null) {
						Thread.sleep(10);
						continue;
					}

					if (sellQuote.getStockId().equals(buyQuote.getStockId()) && Double.doubleToLongBits(sellQuote.getQuotePrice())<=Double.doubleToLongBits(buyQuote.getQuotePrice())) {
						logger.info("-----------------------------------------------------------------");
						for(StQuote stq : DataConstant.sellList.getList()){
							logger.info("卖家队列---"+stq.getAccountId()+"--价格-"+stq.getQuotePrice()+"--报价时间-"+new Date(stq.getDateTime()) + "--报价时间-" + stq.getDateTime());
						}
						logger.info("-----------------------------------------------------------------");
						for(StQuote stqb : DataConstant.buyList.getList()){
							logger.info("买家队列---"+stqb.getAccountId()+"--价格-"+stqb.getQuotePrice()+"--报价时间-"+new Date(stqb.getDateTime())+"--报价时间-"+stqb.getDateTime());
						}
						logger.info("-----------------------------------------------------------------");
						//股票
						StStock sts = DataConstant.stockTable.get(buyQuote.getStockId());
						//交易
						stockAnalysisServiceI.dealTrading(null,buyQuote,sellQuote,sts);
						logger.info("-----------------------------------------------------------------");
					}

					Thread.sleep(2000);
				} else {
					Thread.sleep(1000);
				}

//				logger.info("股票交易引擎---------------开始--------------------");
//				if (!DataConstant.stTradeQueueMap.isEmpty()) {
////					synchronized (Constant.stTradeQueueMap) {
//						for (String s : DataConstant.stTradeQueueMap.keySet()) {
//							StTradeQueue stTradeQueueMap = DataConstant.stTradeQueueMap.get(s);
//							if (stTradeQueueMap.buyList.isEmpty() || stTradeQueueMap.sellList.isEmpty()) continue;
////							logger.info("stTradeQueueMap: 股票ID"+s+"-------------------其他信息："+stTradeQueueMap);
//							boolean sellFlag = true;
//							Long buyerKey = 0L;
//							Long sellerKey = 0L;
////							logger.info("stTradeQueueMap："+stTradeQueueMap);
//							while (sellFlag) {
//								StQuote sellQuote = stTradeQueueMap.getStQuote(sellerKey, 2);
//								if (sellQuote==null) {
//									sellFlag = false;
//									break;
//								}
//
////								logger.info("buyQuote: 股票ID"+buyQuote.getStockId()+"-------------------其他信息："+buyQuote);
//								boolean buyFlag = true;
//								while (buyFlag) {
//									StQuote buyQuote = stTradeQueueMap.getStQuote(buyerKey, 1);
//									if (buyQuote==null) {
//										buyFlag = false;
//										break;
//									}
//									if (sellQuote.getQuotePrice().equals(buyQuote.getQuotePrice())) {
//                                        //股票
//										StStock sts = DataConstant.stockTable.get(buyQuote.getStockId());
//										//交易
//										stockAnalysisServiceI.dealTrading(stTradeQueueMap, buyQuote, sellQuote, sts);
//
//										buyFlag=false;
//									}
//									buyerKey = buyQuote.getQuotePriceForSort();
//									sellerKey = sellQuote.getQuotePriceForSort();
//								}
//							}
////							logger.info("stTradeQueueMap："+stTradeQueueMap.buyList.size()+"---------------"+stTradeQueueMap.sellList.size());
//
//							DataConstant.stTradeQueueMap.put(s, stTradeQueueMap);
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

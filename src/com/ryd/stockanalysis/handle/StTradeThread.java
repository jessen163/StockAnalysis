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

						//添加买入/卖出交易成功后的逻辑
						//交易股票
						StStock sts = Constant.stockTable.get(buyQuote.getStockId());
						//股票交易数量
						int tradeStockAmount = 0;

						//买少卖多
						if (buyQuote.getAmount() < sellQuote.getAmount()) {

							//股票交易数量为买家购买数量
							tradeStockAmount = buyQuote.getAmount();

							stockAnalysisServiceI.trading(buyQuote, sellQuote, tradeStockAmount, sts);

							//交易成功，交易卖家持仓减少
							buyQuote.setAmount(buyQuote.getAmount() - tradeStockAmount);

							//买家移出队列
							Constant.buyList.removeElement(buyQuote);

						} else if (buyQuote.getAmount() == sellQuote.getAmount()) {//买卖相等

							tradeStockAmount = buyQuote.getAmount();

							stockAnalysisServiceI.trading(buyQuote,sellQuote,tradeStockAmount,sts);

							//买家卖家移出队列
							Constant.sellList.removeElement(sellQuote);
							Constant.buyList.removeElement(buyQuote);
						}else if(buyQuote.getAmount()>sellQuote.getAmount()){//买多卖少

							//股票交易数量为卖家卖掉数量
							tradeStockAmount = sellQuote.getAmount();

							stockAnalysisServiceI.trading(buyQuote,sellQuote,tradeStockAmount,sts);

							//交易成功，交易买家报价金额减少
							Map<String, Object> sellmap = stockAnalysisServiceI.buyOrSellStockMoney(buyQuote.getQuotePrice(), tradeStockAmount, sellQuote.getType());
							buyQuote.setFrozeMoney(buyQuote.getFrozeMoney() - (double) sellmap.get("rsMoney"));
							buyQuote.setCommissionFee(buyQuote.getCommissionFee() - (double) sellmap.get("rsMoney"));
							//卖家移出队列
							Constant.sellList.removeElement(sellQuote);
						}

//					if (!Constant.stTradeQueueMap.isEmpty()) {
//						for (String s : Constant.stTradeQueueMap.keySet()) {
//							StTradeQueue stTradeQueueMap = Constant.stTradeQueueMap.get(s);
//							stTradeQueueMap.buyList.entrySet();
//							if (stTradeQueueMap.buyList.isEmpty() || stTradeQueueMap.sellList.isEmpty()) continue;
//							Set<Map.Entry<Long, StQuote>> buyListSet = stTradeQueueMap.buyList.entrySet();
//							Set<Map.Entry<Long, StQuote>> sellListSet = stTradeQueueMap.sellList.entrySet();
////							Iterator<Map.Entry<Long, org.jopenexchg.matcher.PriceLeader>> its =  peerPrcLdrSet.iterator();
//							Iterator<Map.Entry<Long, StQuote>> buyIts = buyListSet.iterator();
//							Iterator<Map.Entry<Long, StQuote>> sellIts = sellListSet.iterator();
//							for (Map.Entry<Long, StQuote> buyQuoteEntry: buyListSet) {
//								for (Map.Entry<Long, StQuote> sellQuoteEntry: sellListSet) {
//									buyQuote = buyQuoteEntry.getValue();
//									sellQuote = sellQuoteEntry.getValue();
//
//									if (sellQuote.getAmount() == buyQuote.getAmount() && Double.doubleToLongBits(sellQuote.getQuotePrice()) <= Double.doubleToLongBits(buyQuote.getQuotePrice())) {
//										stTradeQueueMap.buyList.remove(buyQuoteEntry.getKey());
//										stTradeQueueMap.sellList.remove(sellQuoteEntry.getKey());
//									}
//								}
//							}
//						}
					}


					Thread.sleep(10);
				} else {
					Thread.sleep(10);
				}
				if (!Constant.stTradeQueueMap.isEmpty()) {
					logger.info("股票交易引擎---------------开始--------------------");
//					synchronized (Constant.stTradeQueueMap) {
						for (String s : Constant.stTradeQueueMap.keySet()) {
							StTradeQueue stTradeQueueMap = Constant.stTradeQueueMap.get(s);
							if (stTradeQueueMap.buyList.isEmpty() || stTradeQueueMap.sellList.isEmpty()) continue;
							logger.info("stTradeQueueMap: 股票ID"+s+"-------------------其他信息："+stTradeQueueMap);
							boolean flag = true;
							Long buyerKey = 0L;
							Long sellerKey = Long.MAX_VALUE;
							while (flag) {
								StQuote sellQuote = stTradeQueueMap.getStQuote(sellerKey, 1);
								StQuote buyQuote = stTradeQueueMap.getStQuote(buyerKey, 2);
								if (sellQuote==null||buyQuote==null) {
									flag = false;
									break;
								}
								logger.info("buyQuote: 股票ID"+buyQuote.getStockId()+"-------------------其他信息："+buyQuote);
								logger.info("buyQuote: 股票ID"+buyQuote.getStockId()+"-------------------其他信息："+buyQuote);
								if (sellQuote.getQuotePrice().equals(buyQuote.getQuotePrice())) {
									stTradeQueueMap.removeBuyStQuote(buyQuote);
									stTradeQueueMap.removeSellStQuote(sellQuote);
								}
								buyerKey = buyQuote.getQuotePriceForSort();
								sellerKey = sellQuote.getQuotePriceForSort();
							}

							Constant.stTradeQueueMap.put(s, stTradeQueueMap);
						}
//					}
					logger.info("股票交易引擎---------------结束--------------------");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}

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

					if (sellQuote.getStockId().equals(buyQuote.getStockId()) && sellQuote.getAmount()==buyQuote.getAmount()&&Double.doubleToLongBits(sellQuote.getQuotePrice())<=Double.doubleToLongBits(buyQuote.getQuotePrice())) {

						//添加买入/卖出交易成功后的逻辑
						//撮合成功，买家增加股票持仓数量，卖家减钱

						//交易成功，交易买家持仓增加
						stPositionServiceI.operateStPosition(buyQuote.getAccountId(), buyQuote.getStockId(),buyQuote.getAmount(), Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

						//卖家新增费用计算
						Map<String, Object> sellmap = stockAnalysisServiceI.buyOrSellStockMoney(buyQuote.getQuotePrice(),sellQuote.getAmount(),sellQuote.getType());
						double oinmoney = (double)sellmap.get("rsMoney");

						//交易成功，交易卖家资产增加
						stAccountServiceI.opearteUseMoney(sellQuote.getAccountId(),oinmoney,Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD);

						//收取买家卖家佣金和卖家印花税
						Constant.STOCK_TRADE_AGENT_MONEY = Constant.STOCK_TRADE_AGENT_MONEY + buyQuote.getCommissionFee() + (double)sellmap.get("commissionFee") + (double)sellmap.get("bstampFax");

						//添加交易记录
						StTradeRecord str = new StTradeRecord();
						str.setId(UUID.randomUUID().toString());
						str.setBuyerAccountId(buyQuote.getAccountId());
						str.setSellerAccountId(sellQuote.getAccountId());
						str.setAmount(sellQuote.getAmount());
						str.setStockId(sellQuote.getStockId());
						str.setQuotePrice(buyQuote.getQuotePrice());
						BigDecimal quotePriceb = new BigDecimal(buyQuote.getQuotePrice());
						BigDecimal amonutb = new BigDecimal(sellQuote.getAmount());
						str.setDealMoney(quotePriceb.multiply(amonutb).doubleValue());
						str.setDealFee((buyQuote.getCommissionFee()+(double)sellmap.get("commissionFee")));
						str.setDealTax((double)sellmap.get("bstampFax"));
						str.setDateTime(System.currentTimeMillis());

						//股票列表，设置股票编码
						StStock sts = Constant.stockTable.get(sellQuote.getStockId());

						if (sts != null) {
							str.setStockCode(sts.getStockCode());
						}

						//交易记录列表
						stTradeRecordServiceI.addStTradeRecord(str);

						logger.info("交易--买家->" + buyQuote.getAccountId() + "-和-卖家->" + sellQuote.getAccountId() + "--交易成功--"+"--交易股票->"+sts.getStockName() + "--股票编码->"+sts.getStockCode()+"--交易价格->" + buyQuote.getQuotePrice()+ "-交易数量->" + buyQuote.getAmount()+ "-交易总额->" + quotePriceb.multiply(amonutb).doubleValue()+ "-买家佣金->" + buyQuote.getCommissionFee() + "-卖家佣金->" +  (double)sellmap.get("commissionFee") + "-印花税->" + + (double)sellmap.get("bstampFax"));

						//移除记录
						Constant.sellList.removeElement(sellQuote);
						Constant.buyList.removeElement(buyQuote);
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
//					}
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

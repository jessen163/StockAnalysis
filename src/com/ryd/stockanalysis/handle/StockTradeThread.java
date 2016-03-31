package com.ryd.stockanalysis.handle;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.UUID;

/**
 * 
 * <p>标题:用户买入/卖出股票线程</p> 
 * <p>描述:传入账户、股票信息，调用买入/卖出股票的报价动作--------------实现</p>
 * 包名：com.ryd.stockanalysis.handle
 *
 * 创   建 人：yl
 * 创建时间：2016-3-29 下午1:25:41
 */
public class StockTradeThread implements Runnable {

	private static Logger logger = Logger.getLogger(StockTradeThread.class);

	StockAnalysisServiceI stockAnalysisServiceI;

	public StockTradeThread(StockAnalysisServiceI stockAnalysisServiceI) {
		this.stockAnalysisServiceI = stockAnalysisServiceI;
	}

	@Override
	public void run() {
			try {
				if(!Constant.allQuoteTable.isEmpty()) {
					//处理报价
					for (String key : Constant.allQuoteTable.keySet()) {
						Map uu = Constant.allQuoteTable.get(key);
						if (uu == null) {
							Thread.sleep(10);
							continue;
						}

						StQuote stQuote = new StQuote();

						stQuote.setStockId(uu.get("stockId").toString());
						stQuote.setAccountId(uu.get("accountId").toString());
						stQuote.setQuotePrice((double) uu.get("quotePrice"));
						stQuote.setAmount( (int) uu.get("amount"));
						stQuote.setType( (int) uu.get("type"));
						stQuote.setDateTime(System.currentTimeMillis());
						stQuote.setStatus(Constant.STOCK_STQUOTE_STATUS_TRUSTEE);

						stockAnalysisServiceI.quotePrice(stQuote);

						if (stQuote != null) {
							logger.info(uu.get("info").toString());
						}

						Thread.sleep(10);
					}
				}else{
					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

}
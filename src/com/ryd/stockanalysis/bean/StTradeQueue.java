package com.ryd.stockanalysis.bean;

import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.util.ConcurrentSortedLinkedList;
import com.ryd.stockanalysis.util.SortedLinkedList;

import java.io.Serializable;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 单只股票的报价信息
 * 用于存储单只股票的买的报价队列信息和卖的报价信息
 * 2016-3-31
 */
public class StTradeQueue implements Serializable {
    private static final long serialVersionUID = 1531524722654988278L;

    static final int INIT_CAPACITY = 2000;

//    //买卖家报价
    public ConcurrentSkipListMap<Long, StQuote> sellList = new ConcurrentSkipListMap<Long, StQuote>();
    public ConcurrentSkipListMap<Long, StQuote> buyList = new ConcurrentSkipListMap<Long, StQuote>();

    private Map.Entry<Long, StQuote> sellMap = null;

    public void addSellStQuote(StQuote stQuote) {
            sellList.put(stQuote.getQuotePriceForSort(), stQuote);
//            this.stockId=stQuote.getStockId();
//            this.stQuote=stQuote;
    }

    public void addBuyStQuote(StQuote stQuote) {
            buyList.put(-1 * stQuote.getQuotePriceForSort(), stQuote);
    }

    public void removeSellStQuote(StQuote stQuote) {
        sellList.remove(stQuote.getQuotePriceForSort());
    }

    public void removeBuyStQuote(StQuote stQuote) {
        buyList.remove(-1 * stQuote.getQuotePriceForSort());
    }

    public StQuote getStQuote(Long key, int type) {
        if (type == Constant.STOCK_STQUOTE_TYPE_BUY) {
            sellMap = buyList.lowerEntry(key);
        } else {
            sellMap = sellList.higherEntry(key);
        }
        return sellMap==null?null:sellMap.getValue();
    }

    @Override
    public String toString() {
        return "StTradeQueue{" +
//                "stockId='" + stockId + '\'' +
//                ", StQuote=" + stQuote +
//                ", type=" + type +
                ", sellList=" + sellList.size() +
                ", buyList=" + buyList.size() +
                ", sellMap=" + sellMap +
                '}';
    }
}

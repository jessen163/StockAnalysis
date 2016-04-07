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

    //买卖家报价
    public ConcurrentSkipListMap<Long, StQuote> sellList = new ConcurrentSkipListMap<Long, StQuote>();
    public ConcurrentSkipListMap<Long, StQuote> buyList = new ConcurrentSkipListMap<Long, StQuote>();

    private Map.Entry<Long, StQuote> sellMap = null;

    public boolean addSellStQuote(StQuote stQuote) {
        Long k = Long.parseLong("100000000") * (long)(stQuote.getQuotePrice()*100) + stQuote.getQuotePriceForSort();
        return sellList.put(k, stQuote) != null;
    }

    public boolean addBuyStQuote(StQuote stQuote) {
        Long k = -1 * Long.parseLong("100000000") * (long)(stQuote.getQuotePrice()*100) + stQuote.getQuotePriceForSort();
        return buyList.put(k, stQuote) != null;
    }

    public boolean removeSellStQuote(StQuote stQuote) {
        Long k = Long.parseLong("100000000") * (long)(stQuote.getQuotePrice()*100) + stQuote.getQuotePriceForSort();
        return sellList.remove(k) != null;
    }

    public boolean removeBuyStQuote(StQuote stQuote) {
        Long k = -1 * Long.parseLong("100000000") * (long)(stQuote.getQuotePrice()*100) + stQuote.getQuotePriceForSort();
        return buyList.remove(k) != null;
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

    public static void main(String[] args) {

    }
}

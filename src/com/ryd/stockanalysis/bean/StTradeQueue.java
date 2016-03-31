package com.ryd.stockanalysis.bean;

import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.util.ConcurrentSortedLinkedList;
import com.ryd.stockanalysis.util.SortedLinkedList;

import java.io.Serializable;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * 单只股票的报价信息
 * 用于存储单只股票的买的报价队列信息和卖的报价信息
 * 2016-3-31
 */
public class StTradeQueue implements Serializable {
    private static final long serialVersionUID = 1531524722654988278L;

    static final int INIT_CAPACITY = 2000;

    private String stockId;

    private StQuote StQuote;

    private Integer type;

//    //买卖家报价
    public TreeMap<Long, StQuote> sellList = new TreeMap<Long, StQuote>();
    public TreeMap<Long, StQuote> buyList = new TreeMap<Long, StQuote>();

    private Map.Entry<Long, StQuote> sellMap = null;

    public void addSellStQuote(StQuote stQuote) {
        synchronized (this.sellList) {
            sellList.put(stQuote.getQuotePriceForSort(), stQuote);
        }
    }

    public void addBuyStQuote(StQuote stQuote) {
        synchronized (this.buyList) {
            buyList.put(Long.MAX_VALUE - stQuote.getQuotePriceForSort(), stQuote);
        }
    }

    public void removeSellStQuote(StQuote stQuote) {
        synchronized (this.sellList) {
            sellList.remove(stQuote.getQuotePriceForSort());
        }
    }

    public synchronized void removeBuyStQuote(StQuote stQuote) {
        synchronized (this.buyList) {
            buyList.remove(stQuote.getQuotePriceForSort());
        }
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
                "stockId='" + stockId + '\'' +
                ", StQuote=" + StQuote +
                ", type=" + type +
                ", sellList=" + sellList.size() +
                ", buyList=" + buyList.size() +
                ", sellMap=" + sellMap +
                '}';
    }
}

package com.ryd.stockanalysis.bean;

import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.util.ConcurrentSortedLinkedList;
import com.ryd.stockanalysis.util.SortedLinkedList;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by THINK on 2016/3/30.
 */
public class StTradeQueue implements Serializable {
    private static final long serialVersionUID = 1531524722654988278L;

    static final int INIT_CAPACITY = 2000;

    private String stockId;

    private StQuote StQuote;

    private Integer type;

//    //买卖家报价
    public Map<Long, StQuote> sellList = new TreeMap<Long, StQuote>();
    public Map<Long, StQuote> buyList = new TreeMap<Long, StQuote>();
}

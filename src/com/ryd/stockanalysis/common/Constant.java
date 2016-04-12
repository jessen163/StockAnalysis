package com.ryd.stockanalysis.common;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.util.ConcurrentSortedLinkedList;
import com.ryd.stockanalysis.util.SortedLinkedList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.common
 * 创建人：songby
 * 创建时间：2016/3/28 15:57
 */
public class Constant {

    //1、买，2、卖
    public static Integer STOCK_STQUOTE_TYPE_BUY=1;
    public static Integer STOCK_STQUOTE_TYPE_SELL=2;

    //1、增加，2、减少
    public static Integer STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD=1;
    public static Integer STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE=2;

    //报价状态 1、托管 2、成交 3、过期
    public static Integer STOCK_STQUOTE_STATUS_TRUSTEE=1;
    public static Integer STOCK_STQUOTE_STATUS_DEAL=2;
    public static Integer STOCK_STQUOTE_STATUS_OUTDATE=3;

    //持仓状态 1、托管 2、成交 3、过期
    public static Integer STOCK_STPOSITION_STATUS_TRUSTEE=1;
    public static Integer STOCK_STPOSITION_STATUS_DEAL=2;
    public static Integer STOCK_STPOSITION_STATUS_OUTDATE=3;

    //时间判断结果 1.可以交易和报价 2.只允许报价 3.不允许报价，不允许交易
    public static Integer STQUOTE_TRADE_TIMECOMPARE_1 = 1;
    public static Integer STQUOTE_TRADE_TIMECOMPARE_2 = 2;
    public static Integer STQUOTE_TRADE_TIMECOMPARE_3 = 3;

    //结算延迟时间,单位秒
    public static Integer TRADE_LATER_TIME = 160;

    //佣金比例
    public static double STOCK_COMMINSSION_MONEY_PERCENT = 0.0003;

    //印花税比例
    public static double STOCK_STAMP_TAX_PERCENT = 0.001;

    //股票涨跌幅度
    public static double STOCK_UP_AND_DOWN_PERCENT = 0.1;

    //开盘时间
    public static String STOCK_OPEN_TIME = "9:30";

    //收盘时间
    public static String STOCK_CLOSE_TIME = "20:00";

    //休盘时间开始，结束
    public static String STOCK_REST_TIME_START = "12:30";
    public static String STOCK_REST_TIME_END = "13:00";
}

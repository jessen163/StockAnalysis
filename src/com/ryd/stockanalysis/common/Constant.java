package com.ryd.stockanalysis.common;

import com.ryd.stockanalysis.bean.*;
import com.ryd.stockanalysis.util.ConcurrentSortedLinkedList;
import com.ryd.stockanalysis.util.SortedLinkedList;

import java.math.BigDecimal;
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

    //队列-多只股票
    public static ConcurrentHashMap<String,StTradeQueue> stTradeQueueMap = new ConcurrentHashMap<String,StTradeQueue>();
    //帐户
    public static ConcurrentMap<String,StAccount> stAccounts = new ConcurrentHashMap<String,StAccount>();
    //股票列表
    public static ConcurrentMap<String,StStock> stockTable = new ConcurrentHashMap<String,StStock>();
    //买卖家报价
    public static SortedLinkedList<StQuote> sellList = new ConcurrentSortedLinkedList<StQuote>();
    public static SortedLinkedList<StQuote> buyList = new ConcurrentSortedLinkedList<StQuote>();

    //所有报价信息
    public static ConcurrentMap<String,Map> allQuoteTable = new ConcurrentHashMap<String,Map>();
    //交易记录列表
    public static List<StTradeRecord> recordList = new ArrayList<StTradeRecord>();

    //交易费用
    public static Double STOCK_TRADE_AGENT_MONEY = 0d;

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

    //A、B、C、D、E买卖次数
    public static Integer STQUOTE_A_NUM = 60;
    public static Integer STQUOTE_B_NUM = 20;
    public static Integer STQUOTE_C_NUM = 40;
    public static Integer STQUOTE_D_NUM = 40;
    public static Integer STQUOTE_E_NUM = 50;

    //A、B、C、D、E买卖报价
    public static Double STQUOTE_A_QUOTEPRICE = 10d;
    public static Double STQUOTE_B_QUOTEPRICE = 15d;
    public static Double STQUOTE_C_QUOTEPRICE = 10d;
    public static Double STQUOTE_D_QUOTEPRICE = 20d;
    public static Double STQUOTE_E_QUOTEPRICE = 10d;

    //A、B、C、D、E买卖购买数量
    public static Integer STQUOTE_A_AMOUNT = 10;
    public static Integer STQUOTE_B_AMOUNT = 10;
    public static Integer STQUOTE_C_AMOUNT = 10;
    public static Integer STQUOTE_D_AMOUNT = 10;
    public static Integer STQUOTE_E_AMOUNT = 10;


    //交易股票
    public static String TRADEING_STOCK_ID = "1";
    public static String TRADEING_STOCK_ID2 = "2";
    public static String TRADEING_STOCK_ID3 = "2";

    //结算延迟时间,单位秒
    public static Integer TRADE_LATER_TIME = 30;

    //佣金比例
    public static double STOCK_COMMINSSION_MONEY = 0.0003;

    //印花税比例
    public static double STOCK_STAMP_TAX = 0.001;

}

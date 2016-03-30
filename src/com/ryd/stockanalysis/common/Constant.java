package com.ryd.stockanalysis.common;

import com.ryd.stockanalysis.util.ConcurrentSortedLinkedList;
import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.bean.StTradeRecord;
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
    public static Integer STQUOTE_A_NUM = 6;
    public static Integer STQUOTE_B_NUM = 2;
    public static Integer STQUOTE_C_NUM = 4;
    public static Integer STQUOTE_D_NUM = 4;
    public static Integer STQUOTE_E_NUM = 4;

    //A、B、C、D、E买卖报价
    public static Double STQUOTE_A_QUOTEPRICE = 10d;
    public static Double STQUOTE_B_QUOTEPRICE = 150d;
    public static Double STQUOTE_C_QUOTEPRICE = 100d;
    public static Double STQUOTE_D_QUOTEPRICE = 50d;
    public static Double STQUOTE_E_QUOTEPRICE = 100d;

    //A、B、C、D、E买卖购买数量
    public static Integer STQUOTE_A_AMOUNT = 100;
    public static Integer STQUOTE_B_AMOUNT = 100;
    public static Integer STQUOTE_C_AMOUNT = 100;
    public static Integer STQUOTE_D_AMOUNT = 100;
    public static Integer STQUOTE_E_AMOUNT = 100;


    //交易股票
    public static String TRADEING_STOCK_ID = "1";

    //结算延迟时间
    public static Integer TRADE_LATER_TIME = 50;


}

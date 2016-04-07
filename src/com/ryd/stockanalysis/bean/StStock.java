package com.ryd.stockanalysis.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>标题:报价/下单</p>
 * <p>描述:报价/下单</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/3/28 13:55
 */
public class StStock implements Serializable{


    private static final long serialVersionUID = 7549760581444025071L;


    private String stockId;
    private String stockName;
    private String stockCode;
    private double openPrice;//今日开盘价
    private double bfclosePrice;//昨日收盘价
    private double currentPrice;//当前价格
    private double maxPrice;//今日最高价
    private double minPrice;//今日最低价
    private double tradeAmount;//成交量
    private double tradeMoney;//成交金额
    private Date datetime;//取值时间
    private String stockPinyin;
    private String stockShortPinyin;


    public StStock() {
    }

    public StStock(String stockId, String stockName, String stockCode) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.stockCode = stockCode;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getBfclosePrice() {
        return bfclosePrice;
    }

    public void setBfclosePrice(double bfclosePrice) {
        this.bfclosePrice = bfclosePrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public double getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(double tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getStockPinyin() {
        return stockPinyin;
    }

    public void setStockPinyin(String stockPinyin) {
        this.stockPinyin = stockPinyin;
    }

    public String getStockShortPinyin() {
        return stockShortPinyin;
    }

    public void setStockShortPinyin(String stockShortPinyin) {
        this.stockShortPinyin = stockShortPinyin;
    }
}

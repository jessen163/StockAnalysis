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
    private String stockType; // 上证，深市
    private double openPrice;//今日开盘价
    private double bfclosePrice;//昨日收盘价
    private double currentPrice;//当前价格
    private double maxPrice;//今日最高价
    private double minPrice;//今日最低价

    private double biddingBuyPrice; // 竞买价
    private double biddingSellPrice; // 竞买价

    private double tradeAmount;//成交量
    private double tradeMoney;//成交金额

    private double buyOnePrice;//买一报价
    private int buyOneAmount;//买一申请股数
    private double buyTwoPrice;
    private int buyTwoAmount;
    private double buyThreePrice;
    private int buyThreeAmount;
    private double buyFourPrice;
    private int buyFourAmount;
    private double buyFivePrice;
    private int buyFiveAmount;

    private double sellOnePrice;//卖一报价
    private int sellOneAmount;//卖一申请股数
    private double sellTwoPrice;
    private int sellTwoAmount;
    private double sellThreePrice;
    private int sellThreeAmount;
    private double sellFourPrice;
    private int sellFourAmount;
    private double sellFivePrice;
    private int sellFiveAmount;

    private Date datetime;//取值时间
    private String stockPinyin;
    private String stockShortPinyin;


    public StStock() {
    }

    public StStock(String stockId, String stockName, String stockCode, String stockType) {
        this.stockId = stockId;
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.stockType = stockType;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public double getBiddingBuyPrice() {
        return biddingBuyPrice;
    }

    public void setBiddingBuyPrice(double biddingBuyPrice) {
        this.biddingBuyPrice = biddingBuyPrice;
    }

    public double getBiddingSellPrice() {
        return biddingSellPrice;
    }

    public void setBiddingSellPrice(double biddingSellPrice) {
        this.biddingSellPrice = biddingSellPrice;
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

    public double getBuyOnePrice() {
        return buyOnePrice;
    }

    public void setBuyOnePrice(double buyOnePrice) {
        this.buyOnePrice = buyOnePrice;
    }

    public int getBuyOneAmount() {
        return buyOneAmount;
    }

    public void setBuyOneAmount(int buyOneAmount) {
        this.buyOneAmount = buyOneAmount;
    }

    public double getBuyTwoPrice() {
        return buyTwoPrice;
    }

    public void setBuyTwoPrice(double buyTwoPrice) {
        this.buyTwoPrice = buyTwoPrice;
    }

    public int getBuyTwoAmount() {
        return buyTwoAmount;
    }

    public void setBuyTwoAmount(int buyTwoAmount) {
        this.buyTwoAmount = buyTwoAmount;
    }

    public double getBuyThreePrice() {
        return buyThreePrice;
    }

    public void setBuyThreePrice(double buyThreePrice) {
        this.buyThreePrice = buyThreePrice;
    }

    public int getBuyThreeAmount() {
        return buyThreeAmount;
    }

    public void setBuyThreeAmount(int buyThreeAmount) {
        this.buyThreeAmount = buyThreeAmount;
    }

    public double getBuyFourPrice() {
        return buyFourPrice;
    }

    public void setBuyFourPrice(double buyFourPrice) {
        this.buyFourPrice = buyFourPrice;
    }

    public int getBuyFourAmount() {
        return buyFourAmount;
    }

    public void setBuyFourAmount(int buyFourAmount) {
        this.buyFourAmount = buyFourAmount;
    }

    public double getBuyFivePrice() {
        return buyFivePrice;
    }

    public void setBuyFivePrice(double buyFivePrice) {
        this.buyFivePrice = buyFivePrice;
    }

    public int getBuyFiveAmount() {
        return buyFiveAmount;
    }

    public void setBuyFiveAmount(int buyFiveAmount) {
        this.buyFiveAmount = buyFiveAmount;
    }

    public double getSellOnePrice() {
        return sellOnePrice;
    }

    public void setSellOnePrice(double sellOnePrice) {
        this.sellOnePrice = sellOnePrice;
    }

    public int getSellOneAmount() {
        return sellOneAmount;
    }

    public void setSellOneAmount(int sellOneAmount) {
        this.sellOneAmount = sellOneAmount;
    }

    public double getSellTwoPrice() {
        return sellTwoPrice;
    }

    public void setSellTwoPrice(double sellTwoPrice) {
        this.sellTwoPrice = sellTwoPrice;
    }

    public int getSellTwoAmount() {
        return sellTwoAmount;
    }

    public void setSellTwoAmount(int sellTwoAmount) {
        this.sellTwoAmount = sellTwoAmount;
    }

    public double getSellThreePrice() {
        return sellThreePrice;
    }

    public void setSellThreePrice(double sellThreePrice) {
        this.sellThreePrice = sellThreePrice;
    }

    public int getSellThreeAmount() {
        return sellThreeAmount;
    }

    public void setSellThreeAmount(int sellThreeAmount) {
        this.sellThreeAmount = sellThreeAmount;
    }

    public double getSellFourPrice() {
        return sellFourPrice;
    }

    public void setSellFourPrice(double sellFourPrice) {
        this.sellFourPrice = sellFourPrice;
    }

    public int getSellFourAmount() {
        return sellFourAmount;
    }

    public void setSellFourAmount(int sellFourAmount) {
        this.sellFourAmount = sellFourAmount;
    }

    public double getSellFivePrice() {
        return sellFivePrice;
    }

    public void setSellFivePrice(double sellFivePrice) {
        this.sellFivePrice = sellFivePrice;
    }

    public int getSellFiveAmount() {
        return sellFiveAmount;
    }

    public void setSellFiveAmount(int sellFiveAmount) {
        this.sellFiveAmount = sellFiveAmount;
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

    @Override
    public String toString() {
        return "StStock{" +
                "stockId='" + stockId + '\'' +
                ", stockName='" + stockName + '\'' +
                ", stockCode='" + stockCode + '\'' +
                ", stockType='" + stockType + '\'' +
                ", openPrice=" + openPrice +
                ", bfclosePrice=" + bfclosePrice +
                ", currentPrice=" + currentPrice +
                ", maxPrice=" + maxPrice +
                ", minPrice=" + minPrice +
                ", biddingBuyPrice=" + biddingBuyPrice +
                ", biddingSellPrice=" + biddingSellPrice +
                ", tradeAmount=" + tradeAmount +
                ", tradeMoney=" + tradeMoney +
                ", buyOnePrice=" + buyOnePrice +
                ", buyOneAmount=" + buyOneAmount +
                ", buyTwoPrice=" + buyTwoPrice +
                ", buyTwoAmount=" + buyTwoAmount +
                ", buyThreePrice=" + buyThreePrice +
                ", buyThreeAmount=" + buyThreeAmount +
                ", buyFourPrice=" + buyFourPrice +
                ", buyFourAmount=" + buyFourAmount +
                ", buyFivePrice=" + buyFivePrice +
                ", buyFiveAmount=" + buyFiveAmount +
                ", sellOnePrice=" + sellOnePrice +
                ", sellOneAmount=" + sellOneAmount +
                ", sellTwoPrice=" + sellTwoPrice +
                ", sellTwoAmount=" + sellTwoAmount +
                ", sellThreePrice=" + sellThreePrice +
                ", sellThreeAmount=" + sellThreeAmount +
                ", sellFourPrice=" + sellFourPrice +
                ", sellFourAmount=" + sellFourAmount +
                ", sellFivePrice=" + sellFivePrice +
                ", sellFiveAmount=" + sellFiveAmount +
                ", datetime=" + datetime +
                ", stockPinyin='" + stockPinyin + '\'' +
                ", stockShortPinyin='" + stockShortPinyin + '\'' +
                '}';
    }
}

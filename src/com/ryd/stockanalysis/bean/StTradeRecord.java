package com.ryd.stockanalysis.bean;

import java.io.Serializable;

/**
 * <p>标题:交易</p>
 * <p>描述:交易</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/3/28 13:56
 */
public class StTradeRecord implements Serializable{


    private static final long serialVersionUID = 1077912350836350022L;

    private String id;
    private String sellerAccountId;
    private String buyerAccountId;
    private String sellerAccountNumber; //冗余开户号
    private String buyerAccountNumber; //冗余开户号
    private String stockId;
    private String stockCode;
    private Integer amount; //数量
    private Double quotePrice;//交易价格
    private Double dealMoney;//交易总额
    private Long dateTime;//交易时间
    private Double dealFee;//交易费用（佣金）
    private Double dealTax;//交易税(印花税)

    public Double getDealTax() {
        return dealTax;
    }

    public void setDealTax(Double dealTax) {
        this.dealTax = dealTax;
    }

    public Double getDealFee() {
        return dealFee;
    }

    public void setDealFee(Double dealFee) {
        this.dealFee = dealFee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerAccountId() {
        return sellerAccountId;
    }

    public void setSellerAccountId(String sellerAccountId) {
        this.sellerAccountId = sellerAccountId;
    }

    public String getBuyerAccountId() {
        return buyerAccountId;
    }

    public void setBuyerAccountId(String buyerAccountId) {
        this.buyerAccountId = buyerAccountId;
    }

    public String getBuyerAccountNumber() {
        return buyerAccountNumber;
    }

    public void setBuyerAccountNumber(String buyerAccountNumber) {
        this.buyerAccountNumber = buyerAccountNumber;
    }

    public String getSellerAccountNumber() {
        return sellerAccountNumber;
    }

    public void setSellerAccountNumber(String sellerAccountNumber) {
        this.sellerAccountNumber = sellerAccountNumber;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(Double quotePrice) {
        this.quotePrice = quotePrice;
    }

    public Double getDealMoney() {
        return dealMoney;
    }

    public void setDealMoney(Double dealMoney) {
        this.dealMoney = dealMoney;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }
}

package com.ryd.stockanalysis.bean;

import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.util.ArithUtil;

import java.io.Serializable;

/**
 * <p>标题:股票</p>
 * <p>描述:股票</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/3/28 13:55
 */
public class StQuote implements Serializable, Comparable<StQuote> {


    private static final long serialVersionUID = -8913199818978793382L;

    private String quoteId;
    private String accountId;
    private String stockId;
    private Integer amount; //报价数量
    private Double quotePrice;//报价
    private Integer type; //1、买，2、卖
    private Integer status; //1、托管 2、成交 -1、过期
    private Double frozeMoney;//买股票时冻结资金
    private Double commissionFee;//佣金
    private Long dateTime;//报价时间
    private Long timeSort; // 用于时间排序
    private Long quotePriceForSort; // 用于排序

    private Integer currentAmount; //交易数量-当前数量

    public Long getTimeSort() {
        return timeSort;
    }

    public void setTimeSort(Long timeSort) {
        this.timeSort = timeSort;
    }

    public Integer getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Long getQuotePriceForSort() {
        return quotePriceForSort;
    }

    public void setQuotePriceForSort(Long quotePriceForSort) {
        this.quotePriceForSort = quotePriceForSort;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Double getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(Double quotePrice) {
        this.quotePrice = quotePrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getCommissionFee() {
        return commissionFee;
    }

    public void setCommissionFee(Double commissionFee) {
        this.commissionFee = commissionFee;
    }

    public Double getFrozeMoney() {
        return frozeMoney;
    }

    public void setFrozeMoney(Double frozeMoney) {
        this.frozeMoney = frozeMoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int compareTo(StQuote o) {

        if(this.quotePrice < o.quotePrice){
            return 1;
        } else if(ArithUtil.compare(this.quotePrice,o.quotePrice)==0){
            if(o.getType() == Constant.STOCK_STQUOTE_TYPE_BUY){
                if(this.dateTime.longValue() < o.getDateTime().longValue()){
                    return -1;
                }else{
                    return 1;
                }
            }else{
                if(this.dateTime.longValue() < o.getDateTime().longValue()){
                    return 1;
                }else{
                    return -1;
                }
            }

        } else{
            return -1;
        }
    }

    @Override
    public String toString() {
        return "StQuote{" +
                "quoteId='" + quoteId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", stockId='" + stockId + '\'' +
                ", amount=" + amount +
                ", quotePrice=" + quotePrice +
                ", type=" + type +
                ", status=" + status +
                ", frozeMoney=" + frozeMoney +
                ", commissionFee=" + commissionFee +
                ", dateTime=" + dateTime +
                ", timeSort=" + timeSort +
                ", quotePriceForSort=" + quotePriceForSort +
                ", currentAmount=" + currentAmount +
                '}';
    }
}

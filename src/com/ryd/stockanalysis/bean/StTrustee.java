package com.ryd.stockanalysis.bean;

import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.util.ArithUtil;

import java.io.Serializable;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.bean
 * 创建人：songby
 * 创建时间：2016/4/6 17:49
 */
public class StTrustee implements Serializable, Comparable<StTrustee> {
    private String stockId;
    private String trustName;//买卖名称
    private double quotePrice;//申报价格
    private long amount;//申报股票数
    private int type;//1.买 2。卖

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getTrustName() {
        return trustName;
    }

    public void setTrustName(String trustName) {
        this.trustName = trustName;
    }

    public double getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(double quotePrice) {
        this.quotePrice = quotePrice;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(StTrustee o) {
        if(Constant.STOCK_STQUOTE_TYPE_BUY == this.type) {
            return ArithUtil.compare(this.quotePrice, o.getQuotePrice());
        }else{
            return -ArithUtil.compare(this.quotePrice, o.getQuotePrice());
        }
    }
}

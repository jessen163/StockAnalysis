package com.ryd.stockanalysis.service;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/3/30 13:33
 */
public interface StAccountServiceI {


    /**
     * 增加/减少可用资产
     * @param accountId
     * @param oinmoney
     * @param type 增加/减少
     * @return
     */
    public boolean opearteUseMoney(String accountId, double oinmoney, int type);

    /**
     * 增加/减少总资产
     * @param accountId
     * @param oinmoney
     * @param type 增加/减少
     * @return
     */
    public boolean opearteTotalMoney(String accountId, double oinmoney, int type);
}

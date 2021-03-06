package com.ryd.stockanalysis.service;

import com.ryd.stockanalysis.bean.StAccount;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/3/30 13:33
 */
public interface StAccountServiceI {
    /**
     * 通过参数查询账户信息
     * @param stAccount
     * @return
     */
    public StAccount findStAccountByParams(StAccount stAccount);

    /**
     * 增加/减少可用资产
     * @param accountId
     * @param oinmoney
     * @param type 增加/减少
     * @return
     */
    public boolean opearteUseMoney(String accountId, double oinmoney, int type);

    /**
     * 计算帐户总资产
     * @param account
     */
    public void sumTotalMoney(StAccount account);
}

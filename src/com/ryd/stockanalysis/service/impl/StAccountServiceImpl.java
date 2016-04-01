package com.ryd.stockanalysis.service.impl;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.service.StAccountServiceI;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/3/30 13:35
 */
public class StAccountServiceImpl implements StAccountServiceI {

    @Override
    public boolean opearteUseMoney(String accountId, double oinmoney, int type){

        //获取对应的帐户信息
        StAccount account = Constant.stAccounts.get(accountId);

        if(account==null){return false;}

        //原有帐户可用资产
        double useMoney = account.getUseMoney();
        //原有帐户总资产
        double totalMoney = account.getTotalMoney();

        //新增资产，交易增加费用
        if(type == Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD.intValue()) {
            account.setUseMoney(useMoney + oinmoney);
            return true;
        }else if(type == Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE.intValue()) { //如果状态为减少，则是减少费用
            //交易减少费用
            if (useMoney >= oinmoney) {
                account.setUseMoney(useMoney - oinmoney);
                return true;
            } else {
                return false;
            }

        }else{
            return false;
        }
    }

    @Override
    public boolean opearteTotalMoney(String accountId, double oinmoney, int type){

        //获取对应的帐户信息
        StAccount account = Constant.stAccounts.get(accountId);

        if(account==null){return false;}

        //原有帐户总资产
        double totalMoney = account.getTotalMoney();

        //新增资产，交易增加费用
        if(type == Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD.intValue()) {
            account.setTotalMoney(totalMoney + oinmoney);
            return true;
        }else if(type == Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE.intValue()) { //如果状态为减少，则是减少费用
            //交易减少费用
            if (totalMoney >= oinmoney) {
//                account.setTotalMoney(totalMoney - oinmoney);
                return true;
            } else {
                return false;
            }

        }else{
            return false;
        }
    }
}

package com.ryd.stockanalysis.service.impl;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StPosition;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.service.StAccountServiceI;
import com.ryd.stockanalysis.util.ArithUtil;

import java.util.Map;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/3/30 13:35
 */
public class StAccountServiceImpl implements StAccountServiceI {
    @Override
    public StAccount findStAccountByParams(StAccount stAccount) {
        for (String k : DataConstant.stAccounts.keySet()) {
               StAccount account = DataConstant.stAccounts.get(k);
            if (stAccount.getAccountNumber().equals(account.getAccountNumber())) {
                //计算总资产
                sumTotalMoney(account);
                return account;
            }
        }
        return null;
    }

    @Override
    public boolean opearteUseMoney(String accountId, double oinmoney, int type){

        //获取对应的帐户信息
        StAccount account = DataConstant.stAccounts.get(accountId);

        if(account==null){return false;}

        //原有帐户可用资产
        double useMoney = account.getUseMoney();
        //原有帐户总资产
        double totalMoney = account.getTotalMoney();

        //新增资产，交易增加费用
        if(type == Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD.intValue()) {
            account.setUseMoney(ArithUtil.add(useMoney, oinmoney));
            return true;
        }else if(type == Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE.intValue()) { //如果状态为减少，则是减少费用
            //交易减少费用
            if (ArithUtil.compare(useMoney,oinmoney)>=0) {
                account.setUseMoney(ArithUtil.subtract(useMoney, oinmoney));
                return true;
            } else {
                return false;
            }

        }else{
            return false;
        }
    }


    @Override
    public synchronized void sumTotalMoney(StAccount account){

        if(account==null){return;}
        //原有帐户可用资产
        double useMoney = account.getUseMoney();

        //所有持仓资产
        double countMoney = 0d;

        Map<String,StPosition> positionMap = DataConstant.stAccountPositionMap.get(account.getAccountId());
        if(positionMap!=null) {
            //计算每支股票持仓资产
            for (String pkey : positionMap.keySet()) {
                //持仓
                StPosition position = positionMap.get(pkey);
                //对应股票
                StStock stock = DataConstant.stockTable.get(position.getStockId());

                //持仓数量*股票现价 = 持有这支股票资产
                double tempMoney = ArithUtil.multiply(position.getAmount(), stock.getCurrentPrice());

                countMoney = ArithUtil.add(countMoney, tempMoney);
            }

            account.setTotalMoney(ArithUtil.add(useMoney,countMoney));
        }
    }
}

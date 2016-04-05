package com.ryd.stockanalysis.service.impl;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StPosition;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.service.StPositionServiceI;

import java.util.List;
import java.util.UUID;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/3/30 13:36
 */
public class StPositionServiceImpl implements StPositionServiceI {


    @Override
    public boolean operateStPosition(String accountId, String stockId, int amount,int type) {

        //获取对应的帐户信息
        StAccount account = DataConstant.stAccounts.get(accountId);

        if(account==null){return false;}

        //获取所有的股票持仓
        List<StPosition> stplist = account.getStPositionList();

        //判断对应股票要增加/减少的仓位
        StPosition stp = null;
        for (StPosition p : stplist) {
            if (p.getStockId().equals(stockId)) {
                stp = p;
                break;
            }
        }

        //如果状态为增加，则是增加仓位
        if(type == Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_ADD.intValue()) {

            //如果对应仓位为空，新建对应这支股票的仓位
            if (stp == null) {
                stp = new StPosition();
                StPosition atPos = new StPosition();
                atPos.setPositionId(UUID.randomUUID().toString());
                atPos.setAccountId(accountId);
                atPos.setStockId(stockId);
                atPos.setStStock(DataConstant.stockTable.get(stockId));
                atPos.setAmount(amount);
                atPos.setStatus(Constant.STOCK_STPOSITION_STATUS_TRUSTEE);

                account.getStPositionList().add(atPos);
            } else {

                //原有持仓
                int camount = stp.getAmount();

                //增加持仓
                stp.setAmount(camount + amount);
            }

            return true;
        }else if(type == Constant.STOCK_STQUOTE_ACCOUNTMONEY_TYPE_REDUSE.intValue()) { //如果状态为减少，则是减少仓位
            //没有对应股票持仓
            if (stp == null) {
                return false;
            }else{
                //原有持仓数量
                int stamount = stp.getAmount();

                if (stamount >= amount) {
                    //减少持仓
                    stp.setAmount(stamount - amount);
                    return true;
                } else {
                    return false;
                }
            }

        }else{
            return  false;
        }
    }

}

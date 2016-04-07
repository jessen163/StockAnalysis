package com.ryd.stockanalysis.service.impl;

import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.bean.StTrustee;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.service.StockGetInfoFromApiI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * <p>标题:</p>
 * <p>描述:</p>
 * 包名：com.ryd.stockanalysis.service.impl
 * 创建人：songby
 * 创建时间：2016/4/6 17:03
 */
public class StockGetInfoFromApiImpl implements StockGetInfoFromApiI {


    @Override
    public StStock getStStockInfo(String st, String stockCode) {

        StringBuilder bf = new StringBuilder();
        StStock sts = null;
        //访问新浪股票接口
        URL url = null;
        try {
            url = new URL(DataConstant.STOCK_SINA_URL + "sh" + stockCode);

            InputStream in = null;
            try {
                in = url.openStream();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    byte buf[] = new byte[1024];
                    int read = 0;
                    while ((read = in.read(buf)) > 0) {
                        out.write(buf, 0, read);
                    }
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                byte[] byteArray = out.toByteArray();
                String str = new String(byteArray, "gbk");
                String[] s = str.split("\"");
                String[] sk = null;
                if (!s[1].equals("")) {
                    bf.append(stockCode + ",");
                    bf.append(s[1]);
                    int n = bf.lastIndexOf(",");
                    String string = bf.substring(0, n);
                    sk = string.split(",");

                    sts = new StStock();
                    sts.setStockId(sk[0]);
                    sts.setStockCode(sk[0]);
                    sts.setStockName(sk[1]);
                    sts.setOpenPrice(Double.parseDouble(sk[2]));
                    sts.setBfclosePrice(Double.parseDouble(sk[3]));
                    sts.setCurrentPrice(Double.parseDouble(sk[4]));
                    sts.setMaxPrice(Double.parseDouble(sk[5]));
                    sts.setMinPrice(Double.parseDouble(sk[6]));
                    sts.setTradeAmount(Double.parseDouble(sk[9]));
                    sts.setTradeMoney(Double.parseDouble(sk[10]));

                    //买一
                    StTrustee stt1 = new StTrustee();
                    stt1.setStockId(sts.getStockId());
                    stt1.setTrustName("买一");
                    stt1.setAmount(Long.parseLong(sk[11]));
                    stt1.setQuotePrice(Double.parseDouble(sk[12]));
                    stt1.setType(Constant.STOCK_STQUOTE_TYPE_BUY);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return sts;
    }
}

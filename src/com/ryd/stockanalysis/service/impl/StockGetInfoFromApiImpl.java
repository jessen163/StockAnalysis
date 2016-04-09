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
            url = new URL(DataConstant.STOCK_SINA_URL + st + stockCode);

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
//                    bf.append(stockCode + ",");
                    bf.append(s[1]);
                    int n = bf.lastIndexOf(",");
                    String string = bf.substring(0, n);
                    sk = string.split(",");

                    sts = new StStock();
                    sts.setStockId(stockCode);
                    sts.setStockCode(stockCode);
                    sts.setStockName(sk[0]);
                    sts.setOpenPrice(Double.parseDouble(sk[1]));
                    sts.setBfclosePrice(Double.parseDouble(sk[2]));
                    sts.setCurrentPrice(Double.parseDouble(sk[3]));
                    sts.setMaxPrice(Double.parseDouble(sk[4]));
                    sts.setMinPrice(Double.parseDouble(sk[5]));
                    sts.setBiddingBuyPrice(Double.parseDouble(sk[6]));
                    sts.setBiddingSellPrice(Double.parseDouble(sk[7]));
                    sts.setTradeAmount(Double.parseDouble(sk[8]));
                    sts.setTradeMoney(Double.parseDouble(sk[9]));

                    sts.setBuyOneAmount(Integer.parseInt(sk[10]));
                    sts.setBuyOnePrice(Double.parseDouble(sk[11]));
                    sts.setBuyTwoAmount(Integer.parseInt(sk[12]));
                    sts.setBuyTwoPrice(Double.parseDouble(sk[13]));
                    sts.setBuyThreeAmount(Integer.parseInt(sk[14]));
                    sts.setBuyThreePrice(Double.parseDouble(sk[15]));
                    sts.setBuyFourAmount(Integer.parseInt(sk[16]));
                    sts.setBuyFourPrice(Double.parseDouble(sk[17]));
                    sts.setBuyFiveAmount(Integer.parseInt(sk[18]));
                    sts.setBuyFivePrice(Double.parseDouble(sk[19]));

                    sts.setSellOneAmount(Integer.parseInt(sk[20]));
                    sts.setSellOnePrice(Double.parseDouble(sk[21]));
                    sts.setSellTwoAmount(Integer.parseInt(sk[22]));
                    sts.setSellTwoPrice(Double.parseDouble(sk[23]));
                    sts.setSellThreeAmount(Integer.parseInt(sk[24]));
                    sts.setSellThreePrice(Double.parseDouble(sk[25]));
                    sts.setSellFourAmount(Integer.parseInt(sk[26]));
                    sts.setSellFourPrice(Double.parseDouble(sk[27]));
                    sts.setSellFiveAmount(Integer.parseInt(sk[28]));
                    sts.setSellFivePrice(Double.parseDouble(sk[29]));

//                    10：”4695″，“买一”申请4695股，即47手；
//                    11：”26.91″，“买一”报价；
//                    12：”57590″，“买二”
//                    13：”26.90″，“买二”
//                    14：”14700″，“买三”
//                    15：”26.89″，“买三”
//                    16：”14300″，“买四”

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

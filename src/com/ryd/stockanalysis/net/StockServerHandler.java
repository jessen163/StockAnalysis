package com.ryd.stockanalysis.net;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StPosition;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.common.DataInitTool;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.service.impl.StockAnalysisServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理客户端任务
 * Created by Administrator on 2016/4/6.
 */
public class StockServerHandler extends ChannelInboundHandlerAdapter {

    StockAnalysisServiceI stockAnalysisServiceI = new StockAnalysisServiceImpl();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StAccount) {
            StAccount stAccount = (StAccount) msg;
            System.out.println("stAccount:"+stAccount);

        } else if  (msg instanceof StQuote) {

        } else if (msg instanceof String) {
            String str = (String)msg;

            System.out.println("string:"+msg);

            Boolean flag = false;
            String[] strArr = str.toString().split("@");
            if (strArr==null||strArr.length!=7) {
                // 向客户端发送消息
                String response = "parameter error";
                // 在当前场景下，发送的数据必须转换成ByteBuf数组
//                ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
//                encoded.writeBytes(response.getBytes());
//                ctx.write(encoded);
                ctx.writeAndFlush("parameter error");
                return;
            }

            if (strArr[0].equals("A")) {
                // 从互动端获取报价
                StQuote stQuote = new StQuote();
                stQuote.setStockId(strArr[1]);
                stQuote.setAccountId(strArr[2]);
                stQuote.setQuotePrice(Double.parseDouble(strArr[3]));
                stQuote.setAmount(Integer.parseInt(strArr[4]));
                stQuote.setType(Integer.parseInt(strArr[5]));
                stQuote.setDateTime(Long.parseLong(strArr[6]));
                stQuote.setStatus(Constant.STOCK_STQUOTE_STATUS_TRUSTEE);

                flag = stockAnalysisServiceI.quotePrice(stQuote);
            } else if(strArr[0].equals("B")) {
                // 撤单
                StQuote stQuote = new StQuote();
                stQuote.setQuoteId(strArr[1]);
                stQuote.setStockId(strArr[2]);
                stQuote.setAccountId(strArr[3]);
                stQuote.setType(Integer.parseInt(strArr[4]));
                DataInitTool.printTradeQueue("cancel before",stQuote.getStockId());
                flag = stockAnalysisServiceI.cancelStQuote(stQuote);
                DataInitTool.printTradeQueue("cancel end", stQuote.getStockId());
            }

            // 向客户端发送消息
            String response = flag?"Operation success":"Operation fail";
            ctx.writeAndFlush(flag);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}

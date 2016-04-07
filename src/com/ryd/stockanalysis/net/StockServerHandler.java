package com.ryd.stockanalysis.net;

import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.common.Constant;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.service.impl.StockAnalysisServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 处理客户端任务
 * Created by Administrator on 2016/4/6.
 */
public class StockServerHandler extends ChannelInboundHandlerAdapter {
    StockAnalysisServiceI stockAnalysisServiceI = new StockAnalysisServiceImpl();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        try {
            StringBuffer str = new StringBuffer();
            while (in.isReadable()) { // (1)
//                System.out.print((char) in.readByte());
                str.append((char) in.readByte());
                System.out.flush();
            }
            System.out.println(str.toString());
            String[] strArr = str.toString().split("@");
            if (strArr==null||strArr.length!=7) return;

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

//                stockAnalysisServiceI.quotePrice(stQuote);
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
    }
}

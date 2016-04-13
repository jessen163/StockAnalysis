package com.ryd.stockanalysis.net;

import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StPosition;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.common.DataConstant;
import com.ryd.stockanalysis.protocol.NettyMessage;
import com.ryd.stockanalysis.service.StAccountServiceI;
import com.ryd.stockanalysis.service.StStockServiceI;
import com.ryd.stockanalysis.service.StockAnalysisServiceI;
import com.ryd.stockanalysis.service.impl.StAccountServiceImpl;
import com.ryd.stockanalysis.service.impl.StStockServiceImpl;
import com.ryd.stockanalysis.service.impl.StockAnalysisServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 处理客户端任务
 * Created by Administrator on 2016/4/6.
 */
public class StockServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(StockServerHandler.class);

    StockAnalysisServiceI stockAnalysisServiceI = new StockAnalysisServiceImpl();
    StAccountServiceI stAccountServiceI = new StAccountServiceImpl();
    StStockServiceI stStockServiceI = new StStockServiceImpl();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof NettyMessage) {
            NettyMessage nettyMessage = (NettyMessage) msg;
            Integer msgType = nettyMessage.getMsgType();
            boolean flag = false;
            StQuote stQuote = null;
            StAccount account = null;
            switch (msgType) {
                case 1 :
                    // 登陆
                    account = (StAccount) nettyMessage.getMsgObj();
                    account = stAccountServiceI.findStAccountByParams(account);
                    logger.info("登陆信息：" + (account == null ? "登陆失败":account.getAccountNumber()));
                    nettyMessage.setMsgObj(account);
                    logger.info("登陆信息：" + account == null ? "登陆失败" : "登陆成功");
                    break;
                case 2 :
                    // 获取股票信息-行情
                    logger.info("获取股票信息-行情");
                    List<StStock> stStockList = stStockServiceI.findStockList();
                    nettyMessage.setMsgObj(stStockList);
                    logger.info("股票信息-行情:" + stStockList.toString());
                    break;
                case 3 :
                    // 我的报价信息
                    account = (StAccount) nettyMessage.getMsgObj();
                    logger.info("获取我的报价信息："+account.getAccountId());
                    List<StQuote> quoteList = new ArrayList<StQuote>();
                    Map<String,StQuote> stQuoteMap= DataConstant.stAccountQuoteMap.get(account.getAccountId());
                    if (stQuoteMap!=null) {
                        for (String k : stQuoteMap.keySet()) {
                            quoteList.add(stQuoteMap.get(k));
                        }
                    }
                    nettyMessage.setMsgObj(quoteList);
                    logger.info("我的报价信息：" + quoteList.toString());
                    break;
                case 4 :
                    // 报价
                    stQuote = (StQuote) nettyMessage.getMsgObj();
                    logger.info("报价："+stQuote.toString());
                    flag = stockAnalysisServiceI.quotePrice(stQuote);
                    nettyMessage.setMsgObj(flag);
                    logger.info("报价状态："+ (flag ? "报价成功" : "报价失败"));
                    break;
                case 5 :
                    // 撤单
                    stQuote = (StQuote) nettyMessage.getMsgObj();
                    logger.info("撤单："+stQuote.getQuoteId());
                    flag = stockAnalysisServiceI.cancelStQuote(stQuote);
                    nettyMessage.setMsgObj(flag);
                    logger.info("撤单状态："+ (flag ? "报价成功" : "报价失败"));
                    break;
                case 6 :
                    // 持仓信息
                    account = (StAccount) nettyMessage.getMsgObj();
                    logger.info("获取我的持仓信息："+account.getAccountId());
                    Map<String,StPosition> stPositionMap= DataConstant.stAccountPositionMap.get(account.getAccountId());
                    List<StPosition> positionList = new ArrayList<StPosition>();
                    if (stPositionMap!=null) {
                        for (String k : stPositionMap.keySet()) {
                            positionList.add(stPositionMap.get(k));
                        }
                    }
                    nettyMessage.setMsgObj(positionList);
                    logger.info("我的持仓信息：" + positionList.toString());
                    break;
            }
            ctx.writeAndFlush(nettyMessage);
        }

//        if (msg instanceof StAccount) {
//            StAccount stAccount = (StAccount) msg;
//            System.out.println("stAccount:"+stAccount);
//
//        } else if  (msg instanceof StQuote) {
//
//        } else if (msg instanceof String) {
//            String str = (String)msg;
//
//            System.out.println("string:"+msg);
//
//            Boolean flag = false;
//            String[] strArr = str.toString().split("@");
//            if (strArr==null||strArr.length!=7) {
//                // 向客户端发送消息
//                String response = "parameter error";
//                // 在当前场景下，发送的数据必须转换成ByteBuf数组
////                ByteBuf encoded = ctx.alloc().buffer(4 * response.length());
////                encoded.writeBytes(response.getBytes());
////                ctx.write(encoded);
//                ctx.writeAndFlush("parameter error");
//                return;
//            }
//
//            if (strArr[0].equals("A")) {
//                // 从互动端获取报价
//                StQuote stQuote = new StQuote();
//                stQuote.setStockId(strArr[1]);
//                stQuote.setAccountId(strArr[2]);
//                stQuote.setQuotePrice(Double.parseDouble(strArr[3]));
//                stQuote.setAmount(Integer.parseInt(strArr[4]));
//                stQuote.setType(Integer.parseInt(strArr[5]));
//                stQuote.setDateTime(Long.parseLong(strArr[6]));
//                stQuote.setStatus(Constant.STOCK_STQUOTE_STATUS_TRUSTEE);
//
//                flag = stockAnalysisServiceI.quotePrice(stQuote);
//            } else if(strArr[0].equals("B")) {
//                // 撤单
//                StQuote stQuote = new StQuote();
//                stQuote.setQuoteId(strArr[1]);
//                stQuote.setStockId(strArr[2]);
//                stQuote.setAccountId(strArr[3]);
//                stQuote.setType(Integer.parseInt(strArr[4]));
//                DataInitTool.printTradeQueue("cancel before",stQuote.getStockId());
//                flag = stockAnalysisServiceI.cancelStQuote(stQuote);
//                DataInitTool.printTradeQueue("cancel end", stQuote.getStockId());
//            }
//
//            // 向客户端发送消息
//            String response = flag?"Operation success":"Operation fail";
//            ctx.writeAndFlush(flag);
//        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
//        cause.printStackTrace();
        logger.info("客户端断开链接-----------------------------------");
        ctx.close();
    }
}

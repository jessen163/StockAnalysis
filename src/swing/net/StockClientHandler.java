package swing.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import swing.service.impl.MessageServiceImpl;

/**
 * Created by Administrator on 2016/4/11.
 */
public class StockClientHandler extends ChannelInboundHandlerAdapter {

    public StockClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send the message to Server
        super.channelActive(ctx);
        MessageServiceImpl.ctx = ctx;
//        String message = "A@1@1@1@1@1@1";
//        ctx.writeAndFlush(message);
//        StAccount stAccount = new StAccount();
////        stAccount.setAccountNumber("1");
//        stAccount.setAccountId("A");
//
//        StQuote stQuote = new StQuote();
//        stQuote.setAccountId("A");
//        stQuote.setStockId("2");
//        stQuote.setAmount(100);
//        stQuote.setQuotePrice(16.59);
//        stQuote.setType(1);
////        accountId='A', stockId='2', amount=32100, quotePrice=16.59
//
////        ctx.writeAndFlush(message);
//        NettyMessage nettyMessage = new NettyMessage();
////        nettyMessage.setMsgType(1);
//        nettyMessage.setMsgType(6);
//        nettyMessage.setMsgObj(stAccount);
////        nettyMessage.setMsgObj(stQuote);
//        ctx.writeAndFlush(nettyMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageServiceImpl.doMsgForShunt(msg);

//        if (msg instanceof NettyMessage) {
//            NettyMessage nettyMessage = (NettyMessage) msg;
//            StAccount account = null;
//            boolean flag = false;
//            switch (nettyMessage.getMsgType()) {
//                case 1:
//                    // 登陆
//                    account = (StAccount) nettyMessage.getMsgObj();
//                    System.out.println("登陆信息："+account.toString());
//                    break;
//                case 2:
//                    // 获取股票信息-行情
//                    List<StStock> stStockList = (List<StStock>)nettyMessage.getMsgObj();
//                    System.out.println("股票信息:"+stStockList);
//                    break;
//                case 3:
//                    // 我的报价信息
//                    List<StQuote> quoteList = (List<StQuote>) nettyMessage.getMsgObj();
//                    System.out.println("我的报价信息："+quoteList);
//                    break;
//                case 4:
//                    // 报价
//                    flag = (boolean) nettyMessage.getMsgObj();
//                    System.out.println("报价状态："+flag);
//                    break;
//                case 5:
//                    // 撤单
//                    flag = (boolean) nettyMessage.getMsgObj();
//                    System.out.println("撤单状态："+flag);
//                    break;
//                case 6:
//                    // 持仓信息
//                    List<StPosition> positionList = (List<StPosition>) nettyMessage.getMsgObj();
//                    System.out.println(positionList);
//                    break;
//            }
//        }
    }
}

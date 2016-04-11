package swing.service.impl;


import com.ryd.stockanalysis.bean.StPosition;
import com.ryd.stockanalysis.bean.StAccount;
import com.ryd.stockanalysis.bean.StQuote;
import com.ryd.stockanalysis.bean.StStock;
import com.ryd.stockanalysis.protocol.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import swing.ClientConstants;
import swing.bean.*;
import swing.frame.LoginFrame;
import swing.frame.MainFrame;
import swing.frame.QuotePriceJDialog;
import swing.service.MessageServiceI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class MessageServiceImpl extends MessageServiceI {
    public static ChannelHandlerContext ctx;

    /**
     * 1、登陆
     传入参数：type=1 obj：username


     2、获取交易信息-行情
     传入参数：type=2obj：userid


     3、报价信息：单只股票的报价信息（买卖）
     传入参数：type=3 obj：null
     NettyMessage

     4、报价
     传入参数：type=4 obj：stquote

     5、撤单
     传入参数：type=5 obj：quoteId
     * @param msg
     */
    public static void doMsgForShunt(Object msg) {
        NettyMessage rsmsg = (NettyMessage)msg;
        switch (rsmsg.getMsgType()) {

        case 1:
            ClientConstants.stAccount = (StAccount) rsmsg.getMsgObj();
        case 2:
            ClientConstants.stStockList = (List<StStock>) rsmsg.getMsgObj();
        case 3:
            ClientConstants.stQuoteList = (List<StQuote>) rsmsg.getMsgObj();
        case 4:

            boolean rs = (boolean)rsmsg.getMsgObj();
            if(rs) {
                QuotePriceJDialog.instance().setVisible(false);
            }
        case 5:

        case 6:
            ClientConstants.stPositionList = (List<StPosition>) rsmsg.getMsgObj();
        default:
            break;
        }
    }

    public static void sendMessage(Object obj) {
        ctx.writeAndFlush(obj);
    }
}

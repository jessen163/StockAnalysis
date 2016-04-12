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
import swing.frame.QuoteListDialog;
import swing.frame.QuotePriceJDialog;
import swing.service.MessageServiceI;

import javax.swing.*;
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
    public static synchronized void doMsgForShunt(Object msg) {
        NettyMessage rsmsg = (NettyMessage)msg;
        int msgType = rsmsg.getMsgType();
        switch (msgType) {
        case 1:
            if(rsmsg.getMsgObj()==null){
                JOptionPane.showMessageDialog(null, "帐号信息错误，没有该用户", "提示",
                        JOptionPane.ERROR_MESSAGE);
            }else {
                ClientConstants.stAccount = (StAccount) rsmsg.getMsgObj();
                //持仓
                NettyMessage msg2 = new NettyMessage();
                msg2.setMsgObj(ClientConstants.stAccount);
                msg2.setMsgType(ClientConstants.STSTOCK_POSITION);
                sendMessage(msg2);
                //股票
                NettyMessage msg3 = new NettyMessage();
                msg3.setMsgObj(null);
                msg3.setMsgType(ClientConstants.STSTOCK_LIST);
                sendMessage(msg3);
            }
            break;
        case 2:
            ClientConstants.stStockList = (List<StStock>) rsmsg.getMsgObj();
            ClientConstants.stockListToMap();

            LoginFrame.instance().setVisible(false);
            MainFrame.instance().open();
            break;
        case 3:
            ClientConstants.stQuoteList = (List<StQuote>) rsmsg.getMsgObj();
            ClientConstants.quoteListToMap();

            QuoteListDialog.instance().open();
            break;
        case 4:
            boolean rs = (boolean)rsmsg.getMsgObj();
            if(rs) {
                JOptionPane.showMessageDialog(null, "报价成功", "提示",
                        JOptionPane.ERROR_MESSAGE);
                QuotePriceJDialog.instance().setVisible(false);
            }
            break;
        case 5:
            boolean crs = (boolean)rsmsg.getMsgObj();
            if(crs) {
                JOptionPane.showMessageDialog(null, "撤单成功", "提示",
                        JOptionPane.ERROR_MESSAGE);
                QuoteListDialog.instance().setVisible(false);
            }else{
                JOptionPane.showMessageDialog(null, "撤单失败", "提示",
                        JOptionPane.ERROR_MESSAGE);
            }
        case 6:
            ClientConstants.stPositionList = (List<StPosition>) rsmsg.getMsgObj();
            break;
        default:
            break;
        }
    }

    public static void sendMessage(Object obj) {
        ctx.writeAndFlush(obj);
    }
}

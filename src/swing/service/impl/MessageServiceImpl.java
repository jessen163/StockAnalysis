package swing.service.impl;

import io.netty.channel.ChannelHandlerContext;
import swing.service.MessageServiceI;

/**
 * Created by Administrator on 2016/4/11.
 */
public class MessageServiceImpl extends MessageServiceI {
    public static ChannelHandlerContext ctx;

    public static void doMsgForShunt(Object msg) {

    }

    public static void sendMessage(Object obj) {
        ctx.writeAndFlush(obj);
    }
}

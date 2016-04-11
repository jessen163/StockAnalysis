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
        String message = "A@1@1@1@1@1@1";
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageServiceImpl.doMsgForShunt(msg);
//        String str = (String) msg;
//        if (msg instanceof Boolean) {
//            Boolean flag = (Boolean) msg;
//            System.out.println("操作状态："+flag);
//        }
//        String message = "B@1@1@1@1@1@1";
//        ctx.writeAndFlush(message);
    }
}

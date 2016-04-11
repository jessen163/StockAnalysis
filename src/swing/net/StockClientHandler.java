package swing.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Administrator on 2016/4/11.
 */
public class StockClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send the message to Server
        super.channelActive(ctx);
        String message = "A@1@1@1@1@1@1";
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        String str = (String) msg;
        if (msg instanceof Boolean) {
            Boolean flag = (Boolean) msg;
            System.out.println("操作状态："+flag);
        }
//        String message = "B@1@1@1@1@1@1";
//        ctx.writeAndFlush(message);
    }
}

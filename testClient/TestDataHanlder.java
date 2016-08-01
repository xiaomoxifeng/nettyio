

import java.util.Date;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by wh on 16/7/28.
 */
public class TestDataHanlder extends ChannelHandlerAdapter {

    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("客户端与服务端通道-开启："+ctx.channel().localAddress()+"channelActive");

    }


    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("客户端与服务端通道-关闭："+ctx.channel().localAddress()+"channelInactive");

    }


    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {

        //注意此处已经不需要手工解码了
        System.out.println(ctx.channel().id()+""+new Date()+" "+msg);

        //通知您已经链接上客户端[给客户端穿回去的数据加个换行]
        //String str = "服务端收到："+ctx.channel().id()+new Date()+" "+msg+"\r\n";

        //通知您已经链接上客户端[给客户端穿回去的数据加个换行][能用于测试发送量与接受量，在网络调试助手下可以观察到]
        String str = (String) msg+"\r\n";

        //发送给服务端
        ctx.writeAndFlush(str);
    }


    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        System.out.println("异常退出:"+cause.getMessage());
    }

}

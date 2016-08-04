

import java.util.Date;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 每一个Channel资源不共享
 * Created by wh on 16/7/22.
 */
public class TCPServerHanlder extends ChannelHandlerAdapter {
    private String id;

    /**
     * channelAction
     * <p>
     * channel 通道
     * action  活跃的
     * <p>
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().localAddress().toString() + " channelActive");
        //添加到channelGroup 通道组
        //        MyChannelHandlerPool.channelGroup.add(ctx.channel());
        //通知您已经链接上客户端
        String str = "您已经开启与服务端链接" + " " + new Date() + " " + ctx.channel().remoteAddress();
        id = ctx.channel().remoteAddress().toString();
        TCPChannelPool.add(ctx.channel(),id);
        ctx.writeAndFlush(str);
    }

    /**
     * channelInactive
     * <p>
     * channel 	通道
     * Inactive 不活跃的
     * <p>
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 从channelGroup中移除，当有客户端退出后，移除channel。

        TCPChannelPool.remove(id);

        System.out.println(ctx.channel().localAddress().toString() + " channelInactive");
    }

    /**
     * channelRead
     * <p>
     * channel 通道
     * Read    读
     * <p>
     * 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据
     * 但是这个数据在不进行解码时它是ByteBuf类型的后面例子我们在介绍
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new Date() + " " + msg);
//        id = msg.toString();
//        boxOperation = gson.fromJson(String.valueOf(msg), BoxOperation.class);
//        EventBus.getDefault().post(boxOperation);
//        TCPChannelPool.add(ctx.channel(), id);
        TCPChannelPool.send(id, "I am rec" + msg.toString());

    }

    /**
     * channelReadComplete
     * <p>
     * channel  通道
     * Read     读取
     * Complete 完成
     * <p>
     * 在通道读取完成后会在这个方法里通知，对应可以做刷新操作
     * ctx.flush()
     */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * exceptionCaught
     * <p>
     * exception	异常
     * Caught		抓住
     * <p>
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        TCPChannelPool.remove(id);
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}

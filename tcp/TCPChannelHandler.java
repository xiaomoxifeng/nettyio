
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class TCPChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel e) throws Exception {
        System.out.println("报告");
        System.out.println("信息：有一客户端链接到本服务端");
        System.out.println("IP:" + e.localAddress().getHostName());
        System.out.println("Port:" + e.localAddress().getPort());
        System.out.println("报告完毕");
        //全算
        e.pipeline().addLast(new IdleStateHandler(0, 0, 2));
        //读超时
        //   e.pipeline().addLast(new ReadTimeoutHandler( 2));
        //写超时
            // e.pipeline().addLast(new WriteTimeoutHandler( 2));
            //----上面的3个是用于设计心跳
           
        // 解码器
        // 基于换行符号
        e.pipeline().addLast(new LineBasedFrameDecoder(1024));
        //---自定义分隔符---
        byte[] bts = {-1};
        e.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(bts)));
        //----
        // 基于指定字符串【换行符，这样功能等同于LineBasedFrameDecoder】
        // e.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, false, Unpooled.wrappedBuffer(new byte[]{-1})));
        // 基于最大长度
//		e.pipeline().addLast(new FixedLengthFrameDecoder(4));
        // 解码转Sring
        e.pipeline().addLast(new StringDecoder());
        // 编码器 String
        e.pipeline().addLast(new StringEncoder());
        // 在管道中添加我们自己的接收数据实现方法
        e.pipeline().addLast(new HeartBeatIdleHandler());
        e.pipeline().addLast(new TCPServerHanlder());

    }

}

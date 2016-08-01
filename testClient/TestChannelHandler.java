
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by wh on 16/7/28.
 */
public class TestChannelHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel e) throws Exception {

        System.out.println("报告");
        System.out.println("信息：客户端与服务端建立通道链接，下面开始加载管道信息处理");
        System.out.println("报告完毕");

        //基于行的半包粘包处理、String类型解码、String类型编码、消息处理
        e.pipeline().addLast(new LineBasedFrameDecoder(1024));
        e.pipeline().addLast(new StringDecoder());
        e.pipeline().addLast(new StringEncoder());
        e.pipeline().addLast(new TestDataHanlder());

    }
}
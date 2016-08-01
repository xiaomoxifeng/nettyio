

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * Created by wh on 16/7/27.
 */
public class UDPNettyServer {
    EventLoopGroup group ;
    public static void start(int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("服务端开启等待客户端链接");
                    new UDPNettyServer().bind(port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void bind(int port) throws Exception {
        Bootstrap b = new Bootstrap();
        group = new NioEventLoopGroup();
        try {
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UDPNettySeverHandler());
            b.bind(port).sync().channel().closeFuture().await();
        }finally {
            group.shutdownGracefully();
        }
    }
    public void stop(){
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        start(8888);
    }
}

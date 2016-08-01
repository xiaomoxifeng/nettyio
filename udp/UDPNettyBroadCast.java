

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * Created by wh on 16/7/28.
 */
public class UDPNettyBroadCast {
    private final Bootstrap bootstrap;

    private final EventLoopGroup group;
    private boolean flag = true;

    public static void main(String[] args) throws Exception {
        start(8888, "我是广播");
    }

    public UDPNettyBroadCast(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new UDPNettyEncoder(address));
    }

    public void run(String msg) {
        Channel ch = bootstrap.bind(0).syncUninterruptibly().channel();
        System.out.println("LogEventBroadcaster running");
        while (flag) {
            ch.writeAndFlush(msg);
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        flag = false;
        group.shutdownGracefully();
    }


    private static void start(int port, String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UDPNettyBroadCast broadcaster = new UDPNettyBroadCast(new InetSocketAddress("255.255.255.255",
                        port));
                try {
                    broadcaster.run(content);
                } finally {
                    broadcaster.stop();
                }
            }
        }).start();
    }
}

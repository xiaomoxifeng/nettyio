

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TCPNettyServer {
    EventLoopGroup bossGroup ;
    EventLoopGroup workGroup ;
    public static void main(String[] args) {
        start(8888);
    }

    public static void start(int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("服务端开启等待客户端链接");
                    new TCPNettyServer().bind(port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 端口监听
     * @param port
     * @throws Exception
     */
    public void bind(int port) throws Exception {
         bossGroup = new NioEventLoopGroup();
         workGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.childHandler(new TCPChannelHandler());

            // 绑定端口
            ChannelFuture f = b.bind(port).sync();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
    public void stop(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

}
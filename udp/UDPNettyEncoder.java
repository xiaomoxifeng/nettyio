

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

/**
 * Created by wh on 16/7/28.
 */
public class UDPNettyEncoder extends MessageToMessageEncoder<String> {
    private final InetSocketAddress remoteAddress;

    public UDPNettyEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        out.add(new DatagramPacket(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8), remoteAddress));
    }
}

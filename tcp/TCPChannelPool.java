


import com.raventech.projectflow.utils.log.FlowLog;

import java.util.Iterator;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by wh on 16/7/25.
 */
public class TCPChannelPool {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static boolean add(Channel channel, String imuid) {
        channel.attr(AttributeKey.valueOf(imuid));
        return channelGroup.add(channel);
    }

    public static Channel getChannel(String imuid) {
        Channel channel;
        Iterator<Channel> iterator = channelGroup.iterator();
        while (iterator.hasNext()) {
            if ((channel = iterator.next()).hasAttr(AttributeKey.valueOf(imuid))) {
                return channel;
            }
        }
        return null;
    }

    public static void update(Channel channel, String imuid) {
        remove(imuid);
        add(channel, imuid);

    }

    public static void remove(String imuid) {
        Channel channel;
        Iterator<Channel> iterator = channelGroup.iterator();
        while (iterator.hasNext()) {
            if ((channel = iterator.next()).hasAttr(AttributeKey.valueOf(imuid))) {
                channelGroup.remove(channel);
            }
        }
    }

    public static boolean send(String imuid, String content) {
        Channel channel;
        if ((channel = getChannel(imuid)).isActive()) {
            channel.writeAndFlush(content);
            return true;
        } else {
            remove(imuid);
            FlowLog.d("TCPChannelPool", "channel die");
            return false;
        }

    }

    //
    public static void send(String content) {
        channelGroup.writeAndFlush(content);
    }
}

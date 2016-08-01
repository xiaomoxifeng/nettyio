


import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by wh on 16/7/25.
 */
public class TCPChannelPool {
    private static Map<String, Channel> channelMap = new HashMap<>();
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static boolean add(Channel channel, String imuid) {
        if (!channelGroup.contains(channel)) {
            if (channelGroup.add(channel)) {
                channelMap.put(imuid, channel);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static Channel getChannel(String imuid) {
        if (channelMap.containsKey(imuid)) {
            if (channelGroup.contains(channelMap.get(imuid))){
               return channelGroup.find(channelMap.get(imuid).id());
            }else{
                channelMap.remove(imuid);
                return null;
            }

        }
        return null;
    }

    public static void update(Channel channel, String imuid) {
        remove(imuid);
        add(channel, imuid);

    }

    public static void remove(String imuid) {
        if (channelMap.containsKey(imuid)) {

            if (channelGroup.contains(channelMap.get(imuid))){
            channelGroup.remove(channelMap.get(imuid));}

            channelMap.remove(imuid);
        }
    }

    public static boolean send(String imuid, String content) {
        if(getChannel(imuid).isActive()){
            getChannel(imuid).writeAndFlush(content);
            return true;
        }else{
            remove(imuid);
            return false;
        }


    }
}

package org.tinygame.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Auther: gengwei
 * @Date: 2019-12-21 22:13
 * @Description:
 */
public class Broadcaster {
    /**
     * 客户端信道数组，一定要使用 static ，否则无法实现群发
     */
    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster() {
    }

    /**
     * 添加信道
     * @param channel
     */
    public static void addChannel(Channel channel) {
        _channelGroup.add(channel);
    }

    /**
     * 移除信道
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        _channelGroup.remove(channel);
    }

    /**
     * 广播消息
     * @param msg
     */
    public static void broadcast(Object msg) {
        if (null == msg) {
            return;
        }
        _channelGroup.writeAndFlush(msg);
    }
}

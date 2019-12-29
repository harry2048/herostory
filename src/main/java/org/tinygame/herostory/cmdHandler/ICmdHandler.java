package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Auther: gengwei
 * @Date: 2019-12-21 23:27
 * @param <TCmd>
 * @Description: 指令处理器接口
 * 直接使用GeneratedMessageV3作为参数不严谨，操作时还要转换
 */
public interface ICmdHandler<TCmd extends GeneratedMessageV3> {
    void handle(ChannelHandlerContext ctx, TCmd msg);
}

package org.tinygame.herostory;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: gengwei
 * @Date: 2019-12-21 11:29
 * @Description:
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
    private static Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame)msg;
        ByteBuf byteBuf = frame.content();


        byteBuf.readShort();// 读取消息的长度 读取俩个字节
        int msgCode = byteBuf.readShort();// 读取消息的编号

        // 拿到消息并处理消息，判断消息类型交给消息识别器
        Message.Builder msgBuilder = GameMsgRecognizer.getBuilderByMsg(msgCode);
        if (null == msgBuilder) {
            LOGGER.error("无法识别的消息，msgCode = {}", msgCode);
            return;
        }

        // 拿到消息体
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        msgBuilder.clear();
        msgBuilder.mergeFrom(msgBody);
        Message newMsg = msgBuilder.build();

        // 解码后 重新触发channelRead
        if (null != newMsg) {
            ctx.fireChannelRead(newMsg);
        }
    }
}

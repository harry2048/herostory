package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Auther: gengwei
 * @Date: 2019-12-20 23:47
 * @Description:
 */
public class GameMsghandler extends SimpleChannelInboundHandler<Object> {

    private static Logger LOGGER = LoggerFactory.getLogger(GameMsghandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());
    }

    /**
     * 用户离场处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        Broadcaster.removeChannel(ctx.channel());

        Integer userId = (Integer)ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if (null == userId) {
            return;
        }

        UserManager.removeUserById(userId);

        GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
        resultBuilder.setQuitUserId(userId);

        GameMsgProtocol.UserQuitResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
//        System.out.println("收到客户端消息：msgClazz =" +msg.getClass().getName()+", msg ="+ msg);
//        // 工厂模式，我只需要指令处理器，但不需要知道哪个业务指令处理器
//        // 获取指令处理器
//        ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msg.getClass());
//
//        if (null != cmdHandler) {
//            cmdHandler.handle(ctx, cast(msg));
//        }



        if (msg instanceof GeneratedMessageV3) {
            MainThreadProcessor.getInstance().process(ctx, cast(msg));
        }
    }

    /**
     * 转型消息对象
     * @param msg
     * @param <TCmd>
     * @return
     */
    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
        if (null == msg) {
            return null;
        } else {
            return (TCmd)msg;
        }
    }
}
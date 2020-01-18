package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Auther: gengwei
 * @Date: 2019-12-22 12:22
 * @Description:
 */
public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {
    private static Logger LOGGER = LoggerFactory.getLogger(UserAttkCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd cmd) {
        if (null == ctx ||
            null == cmd) {
            return;
        }
        // 获取攻击者id
        Integer attkUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == attkUserId) {
            return;
        }

        // 获取被攻击者 Id
        int targetUserId = cmd.getTargetUserId();

        GameMsgProtocol.UserAttkResult.Builder resultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
        resultBuilder.setAttkUserId(attkUserId);
        resultBuilder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserAttkResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);

        User targetUser = UserManager.getUserById(targetUserId);
        if (null == targetUser) {
            return;
        }

        // 在此打印线程名称
        LOGGER.info("当前线程 = {}", Thread.currentThread().getName());
        // 我们可以看到不相同的线程名称...
        // 用户 A 在攻击用户 C 的时候, 是在线程 1 里,
        // 用户 B 在攻击用户 C 的时候, 是在线程 2 里,
        // 线程 1 和线程 2 同时修改用户 C 的血量...
        // 这是要出事的节奏啊!

        int subtractHp = 10;
        targetUser.currHp = targetUser.currHp - subtractHp;
        // 广播减血消息
        broadcastSubtractHp(targetUserId, subtractHp);

        // 如果血没有了，广播死亡消息
        if (targetUser.currHp <= 0) {
            // 广播死亡消息
            broadcastDie(targetUserId);
        }
    }

    /**
     * 广播减血消息
     * @param targetUserId
     * @param subtractHp
     */
    private static void broadcastSubtractHp(int targetUserId, int subtractHp) {
        if (targetUserId <= 0 ||
            subtractHp <= 0) {
            return;
        }

        GameMsgProtocol.UserSubtractHpResult.Builder resultBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        resultBuilder.setTargetUserId(targetUserId);
        resultBuilder.setSubtractHp(subtractHp);

        GameMsgProtocol.UserSubtractHpResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

    /**
     * 广播死亡消息
     *
     * @param targetUserId
     */
    private static void broadcastDie(int targetUserId) {
        if (targetUserId <= 0) {
            return;
        }

        GameMsgProtocol.UserDieResult.Builder resultBuilder = GameMsgProtocol.UserDieResult.newBuilder();
        resultBuilder.setTargetUserId(targetUserId);
        GameMsgProtocol.UserDieResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}

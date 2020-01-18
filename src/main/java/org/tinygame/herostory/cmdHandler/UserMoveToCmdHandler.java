package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.model.MoveState;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Auther: gengwei
 * @Date: 2019-12-21 23:26
 * @Description:
 */
public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd>{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserMoveToCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd cmd) {
        // 将userId从管道中取出
        Integer userId = (Integer)ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if (null == userId) {
            return;
        }

        // 获取移动的用户
        User moveUser = UserManager.getUserById(userId);
        if (null == moveUser) {
            LOGGER.error("未找到用户，userId = {}", userId);
            return;
        }

        LOGGER.info("当前线程为：" + Thread.currentThread().getName());

        // 获取移动状态
        MoveState mvState = moveUser.moveState;
        // 设置位置和时间
        mvState.fromPosX = cmd.getMoveFromPosX();
        mvState.fromPosY = cmd.getMoveFromPosY();
        mvState.toPosX = cmd.getMoveToPosX();
        mvState.toPosY = cmd.getMoveToPosY();
        mvState.startTime = System.currentTimeMillis();

        // 构建moveResult
        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        // 将目标地址交给moveResult
        // 拿到用户Id
        resultBuilder.setMoveUserId(userId);
        // 拿到用户移动目的地位置
        resultBuilder.setMoveFromPosX(mvState.fromPosX);
        resultBuilder.setMoveFromPosY(mvState.fromPosY);
        resultBuilder.setMoveToPosX(mvState.toPosX);
        resultBuilder.setMoveToPosY(mvState.toPosY);
        resultBuilder.setMoveStartTime(mvState.startTime);


        // 将result写给客户端
        GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}

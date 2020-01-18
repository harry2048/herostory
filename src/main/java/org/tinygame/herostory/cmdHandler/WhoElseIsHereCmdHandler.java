package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.model.MoveState;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Auther: gengwei
 * @Date: 2019-12-21 23:22
 * @Description:
 */
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd msg) {
        if (null == ctx ||
            null == msg) {
            return;
        }

        // 构建谁在场的结果消息
        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        // 循环字典map中的用户
        for (User currUser : UserManager.listUser()) {
            if (null == currUser) {
                continue;
            }

            // 取出用户id和heroAvatar，放到结果对象->userInfoBuilder中
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(currUser.userId);
            userInfoBuilder.setHeroAvatar(currUser.heroAvatar);

            // 构建移动状态
            MoveState mvState = currUser.moveState;
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder
                    mvStateBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();
            mvStateBuilder.setFromPosX(mvState.fromPosX);
            mvStateBuilder.setFromPosY(mvState.fromPosY);
            mvStateBuilder.setToPosX(mvState.toPosX);
            mvStateBuilder.setToPosY(mvState.toPosY);
            mvStateBuilder.setStartTime(mvState.startTime);
            // 将移动状态设置到用户信息
            userInfoBuilder.setMoveState(mvStateBuilder);

            resultBuilder.addUserInfo(userInfoBuilder);
        }

        // 构建结果，并写给客户端
        GameMsgProtocol.WhoElseIsHereResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);
    }
}

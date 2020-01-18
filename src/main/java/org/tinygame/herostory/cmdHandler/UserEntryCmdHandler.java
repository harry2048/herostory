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
 * @Date: 2019-12-21 23:19
 * @Description:
 */
public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd>{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntryCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd msg) {
        if (null == ctx ||
                null == msg) {
            return;
        }

        // 以下信息放到loginCmd中执行
        /*int userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        User existUser = UserManager.getUserById(userId);
        if (null == existUser) {
            LOGGER.error("用户不存在，userId = {}" + userId);
            return;
        }
        String heroAvatar = existUser.heroAvatar;

        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        // 用户入场时将用户加入字典map中
        User newUser = new User();
        newUser.userId = userId;
        newUser.heroAvatar = heroAvatar;
        newUser.currHp = 100;
        UserManager.addUser(newUser);

        // 将用户id附着到 channel
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        // 构建结果并发送
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);*/

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }

        User existUser = UserManager.getUserById(userId);
        if (null == existUser) {
            return;
        }

        // 获取英雄形象
        String heroAvatar = existUser.heroAvatar;

        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        // 构建结果并发送
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}

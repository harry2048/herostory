package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.login.db.LoginService;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @Auther: gengwei
 * @Date: 2020-01-01 11:53
 * @Description:
 */
public class UserLoginCmdHandler implements ICmdHandler<GameMsgProtocol.UserLoginCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntryCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd cmd) {
        if (null == cmd ||
                null == ctx) {
            return;
        }

        LOGGER.info("当前线程为：" + Thread.currentThread().getName());


        String userName = cmd.getUserName();
        String password = cmd.getPassword();
        LOGGER.info("userName = {}, password = {}",
                userName,
                password
        );

        LoginService.getInstance().userLogin(
                userName,
                password,
                (userEntity) ->{
                    if (null == userEntity) {
                        LOGGER.error("用户登录失败, userName = {}", cmd.getUserName());
                        return null;
                    }

                    int userId = userEntity.userId;
                    String heroAvatar = userEntity.heroAvatar;

                    // 用户入场时将用户加入字典map中
                    User newUser = new User();
                    newUser.userId = userId;
                    newUser.heroAvatar = heroAvatar;
                    newUser.currHp = 100;
                    newUser.userName = userEntity.userName;
                    // 将用户放入管理器
                    UserManager.addUser(newUser);

                    // 将用户id附着到 channel
                    ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
                    GameMsgProtocol.UserLoginResult.Builder resultBuilder = GameMsgProtocol.UserLoginResult.newBuilder();
                    resultBuilder.setUserId(newUser.userId);
                    resultBuilder.setUserName(newUser.userName);
                    resultBuilder.setHeroAvatar(newUser.heroAvatar);

                    GameMsgProtocol.UserLoginResult newResult = resultBuilder.build();
                    ctx.writeAndFlush(newResult);

                    return null;
                }
        );
    }
}

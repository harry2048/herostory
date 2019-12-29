package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.util.PackageUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: gengwei
 * @Date: 2019-12-21 23:44
 * @Description: 指令处理器工厂
 */
public final class CmdHandlerFactory {
    // 私有化类默认构造器
    private CmdHandlerFactory() {}

    private static final Logger LOGGER = LoggerFactory.getLogger(CmdHandlerFactory.class);

    private static final Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handlerMap = new HashMap<>();
    public static void init() {
        // 拿到ICmdHandler下所有的子类
        Set<Class<?>> classSet = PackageUtil.listSubClazz(
                CmdHandlerFactory.class.getPackage().getName(),
                true,
                ICmdHandler.class
        );

        for (Class<?> clazz : classSet) {
            if ((clazz.getModifiers() & Modifier.ABSTRACT) !=0) {
                // 过滤接口和抽象类
                continue;
            }
            // 获取方法数组
            Method[] methodArray = clazz.getDeclaredMethods();

            // 消息类型
            Class<?> msgType = null;
            for (Method currMethod : methodArray) {
                if (!currMethod.getName().equals("handle")) {
                    continue;
                }

                // 获取函数参数类型
                Class<?>[] paramTypeArray = currMethod.getParameterTypes();

                // 拿到handler函数必须是俩个参数，并且第二个参数必须是GeneratedMessageV3
                if (paramTypeArray.length < 2 ||
                    !GeneratedMessageV3.class.isAssignableFrom(paramTypeArray[1])) {
                    continue;
                }

                msgType = paramTypeArray[1];
                break;
            }

            if (null == msgType) {
                continue;
            }

            try {
                ICmdHandler<?> newHandler = (ICmdHandler<?>) clazz.newInstance();

                LOGGER.info("{} <==> {}", msgType.getName(), clazz.getName());

                _handlerMap.put(msgType, newHandler);
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
//        _handlerMap.put(GameMsgProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
//        _handlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
//        _handlerMap.put(GameMsgProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
    }

    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClazz) {
        if (null == msgClazz) {
            return null;
        }

        return _handlerMap.get(msgClazz);
    }
}

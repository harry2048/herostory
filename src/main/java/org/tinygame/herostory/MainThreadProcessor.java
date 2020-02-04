package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;
import org.tinygame.herostory.cmdHandler.ICmdHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: gengwei
 * @Date: 2019-12-31 22:56
 * @Description: 主线程处理器
 */
public class MainThreadProcessor {
    /**
     * 日志对象
     */
    private static Logger LOGGER = LoggerFactory.getLogger(MainThreadProcessor.class);

    /**
     * 单例对象
     */
    private static final MainThreadProcessor _instance = new MainThreadProcessor();

    /**
     * 创建一个单线程
     */
    private static final ExecutorService _es = Executors.newSingleThreadExecutor((r) ->{
        Thread newThread = new Thread(r);
        newThread.setName("MainThreadProcessor");
        return newThread;
    });

    /**
     * 私有化类默认构造器
     */
    private MainThreadProcessor() {}

    /**
     * 获取单线程对象
     * @return 主线程处理器
     */
    public static MainThreadProcessor getInstance() {
        return _instance;
    }

    /**
     * 处理消息
     * @param ctx 客户端信道上下文
     * @param msg 消息对象
     */
    public void process(ChannelHandlerContext ctx, GeneratedMessageV3 msg) {
        if (null == ctx ||
            null == msg) {
            return;
        }
        LOGGER.info("收到客户端消息：msgClazz = " + msg.getClass().getName() + ", msg = " + msg);

        this._es.submit(() -> {
            // 获取指令处理器
            ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msg.getClass());

            if (null == cmdHandler) {
                LOGGER.error(
                        "未找到相对应的指令处理器，msgClazz = {}",
                        msg.getClass().getName()
                );
            }

            try {
                // 处理指令
                cmdHandler.handle(ctx, cast(msg));
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        });
    }

    public void process(Runnable r) {
        if (null != r) {
            _es.submit(r);
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

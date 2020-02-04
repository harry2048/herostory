package org.tinygame.herostory.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.MainThreadProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: gengwei
 * @Date: 2020-01-01 20:55
 * @Description: 异步线程处理
 */
public final class AsyncOperationProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncOperationProcessor.class);
    /**
     * 单例对象
     */
    private static final AsyncOperationProcessor _instance = new AsyncOperationProcessor();

    /**
     * 创建一个单线程
     *
     * 单例模式下，使用static时的时序 引起的异常
     */
//    private static final ExecutorService[] _es = new ExecutorService[8];这里不能static
            // static的类只有在用到的时候才会初始化吗？是的
    private final ExecutorService[] _es = new ExecutorService[8];

    /**
     * 私有化类型默认构造器
     */
    private AsyncOperationProcessor() {
        for (int i = 0; i < _es.length; i++) {
            // 线程名称
            final String threadName = "AsyncOperationProcessor_" + i;

            // 创建一个单线程
            _es[i] = Executors.newSingleThreadExecutor((r)->{
                Thread thread = new Thread(r);
                thread.setName(threadName);
                return thread;
            });
        }
    }


    public static AsyncOperationProcessor getInstance() {
        return _instance;
    }

    /**
     * 处理异步操作
     * @param asyncOp
     */
    public void process(IAsyncOperation asyncOp) {
        if (null == asyncOp) {
            return;
        }

        int boundId = Math.abs(asyncOp.boundId());
        int esIndex = boundId % _es.length;

        _es[esIndex].submit(()->{
            try {
                // 执行异步
                asyncOp.doAsync();

                // 回到主线程执行业务逻辑
                MainThreadProcessor.getInstance().process(asyncOp::doFinish);
            } catch (Exception ex) {
                LOGGER.info(ex.getMessage(), ex);
            }
        });
    }
}

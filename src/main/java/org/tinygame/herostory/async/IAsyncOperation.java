package org.tinygame.herostory.async;

/**
 * @Auther: gengwei
 * @Date: 2020-01-01 21:12
 * @Description: 异步操作接口
 */
public interface IAsyncOperation {

    default int boundId() {
        return 0;
    }

    /**
     * 执行异步操作
     */
    void doAsync();

    /**
     * 执行完成逻辑
     */
    default void doFinish(){
    }
}

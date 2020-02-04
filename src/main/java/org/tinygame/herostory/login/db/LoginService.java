package org.tinygame.herostory.login.db;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.MySqlSessionFactory;
import org.tinygame.herostory.async.AsyncOperationProcessor;
import org.tinygame.herostory.async.IAsyncOperation;

import java.util.function.Function;

/**
 * @Auther: gengwei
 * @Date: 2020-01-01 17:21
 * @Description:
 */
public final class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    /**
     * 单例对象
     */
    private static final LoginService _instance = new LoginService();

    public static LoginService getInstance() {
        return _instance;
    }

    /**
     * 私有化类构造器
     */
    private LoginService() {}

    /**
     * 获取用户实体
     * @param userName 用户名
     * @param password 密码
     * @return 用户实体
     */
    public void userLogin(String userName, String password, Function<UserEntity, Void> callback) {
        if (null == userName ||
                null == password) {
            return;
        }

        LOGGER.info("当前线程为：" + Thread.currentThread().getName());

        // 自定义一个接口
        IAsyncOperation asyncOp = new AsyncGetUserByName(userName, password) {

            @Override
            public void doFinish() {
                if (null != callback) {
                    callback.apply(this.getUserEntity());
                }
            }
        };

        // 执行异步
        AsyncOperationProcessor.getInstance().process(asyncOp);
    }

    /**
     * 异步方式获取用户
     */
    private class AsyncGetUserByName implements IAsyncOperation {
        /**
         * 用户名称
         */
        private final String _userName;

        /**
         * 用户密码
         */
        private final String _password;

        /**
         * 用户实体
         */
        private UserEntity _userEntity;

        AsyncGetUserByName(String userName, String password) {
            this._userName = userName;
            this._password = password;
        }

        public UserEntity getUserEntity() {
            return _userEntity;
        }

        @Override
        public int boundId() {
            return _userName.charAt(_userName.length() - 1);
        }

        @Override
        public void doAsync() {
            LOGGER.info("当前线程为：" + Thread.currentThread().getName());

            try (SqlSession mySqlsession = MySqlSessionFactory.openSession()) {
                IUserDao dao = mySqlsession.getMapper(IUserDao.class);

                UserEntity userEntity = dao.getUserByName(_userName);
                if (null != userEntity) {
                    if (!_password.equals(userEntity.password)) {
                        LOGGER.error("用户密码错误, userName = {}", _userName);
                        return;
                        // throw new RuntimeException("用户密码错误");  异步就不抛异常了
                    }
                } else {
                    // 有则校验用户密码，没有则新建用户实体
                    userEntity = new UserEntity();
                    userEntity.userName = _userName;
                    userEntity.password = _password;
                    userEntity.heroAvatar = "Hero_Shaman";

                    // 将用户实体添加到数据库
                    dao.insertInto(userEntity);
                }
                this._userEntity = userEntity;
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }
}

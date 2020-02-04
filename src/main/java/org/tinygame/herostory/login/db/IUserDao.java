package org.tinygame.herostory.login.db;

/**
 * @Auther: gengwei
 * @Date: 2020-01-01 12:04
 * @Description:
 */
public interface IUserDao {

    /**
     * 根据用户名称获取用户
     * @param name
     * @return
     */
    UserEntity getUserByName(String name);

    void insertInto(UserEntity newUserEntity);
}

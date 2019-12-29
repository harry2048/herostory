package org.tinygame.herostory.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: gengwei
 * @Date: 2019-12-21 22:21
 * @Description: 用户管理器
 */
public final class UserManager {
    /**
     * 用户字典
     */
    private static final Map<Integer, User> _userMap = new HashMap<>();

    private UserManager() {
    }

    /**
     * 根据用户Id 移除用户
     * @param userId
     */
    public static void removeUserById(int userId) {
        _userMap.remove(userId);
    }

    /**
     * 添加用户
     * @param newUser
     */
    public static void addUser(User newUser) {
        _userMap.put(newUser.userId, newUser);
    }

    /**
     * 列表用户
     * @return
     */
    public static Collection<User> listUser() {
        return _userMap.values();
    }
}

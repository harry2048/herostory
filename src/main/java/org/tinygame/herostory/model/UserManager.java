package org.tinygame.herostory.model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: gengwei
 * @Date: 2019-12-21 22:21
 * @Description: 用户管理器
 */
public final class UserManager {
    /**
     * 用户字典
     */
    private static final Map<Integer, User> _userMap = new ConcurrentHashMap<>();

    private UserManager() {
    }

    /**
     * 根据用户Id 移除用户
     *
     * @param userId
     */
    public static void removeUserById(int userId) {
        _userMap.remove(userId);
    }

    /**
     * 添加用户
     *
     * @param newUser
     */
    public static void addUser(User newUser) {
        _userMap.put(newUser.userId, newUser);
    }

    /**
     * 列表用户
     *
     * @return
     */
    public static Collection<User> listUser() {
        return _userMap.values();
    }

    /**
     * 根据 Id 获取用户
     * @param userId
     * @return
     */
    public static User getUserById(int userId) {
        return _userMap.get(userId);
    }
}

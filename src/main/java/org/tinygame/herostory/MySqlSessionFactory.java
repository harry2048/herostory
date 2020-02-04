package org.tinygame.herostory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @Auther: gengwei
 * @Date: 2020-01-01 17:14
 * @Description: mysql会话工厂
 */
public final class MySqlSessionFactory {
    private static SqlSessionFactory _sqlSessionFactory;

    private MySqlSessionFactory() {}

    public static void init() {
        try {
            _sqlSessionFactory = (new SqlSessionFactoryBuilder()).build(
                    // 拿到resource下的配置文件
                    Resources.getResourceAsStream("MyBatisConfig.xml")
            );
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static SqlSession openSession() {
        if (null == _sqlSessionFactory) {
            throw new RuntimeException("_sqlSessionFactory 尚未初始化");
        }

        // true是自动提交
        return _sqlSessionFactory.openSession(true);
    }
}

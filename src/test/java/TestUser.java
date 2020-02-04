import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: gengwei
 * @Date: 2019-12-30 22:54
 * @Description:
 */
public class TestUser {
    public int currHp;
    /**
     * 当前血量, 使用 AtomicInteger 确实可以保证线程安全.
     * 但是, 用户类中有那么多字段不能全用 Atomic 类型,
     * 这样会让用户类变得特别臃肿...
     * 而且, 这样做也还是不能彻底解决问题, 例如:
     * 属性 A 本身是线程安全的,
     * 属性 B 本身也是线程安全的,
     * 但我们无法保证同时操作 A 和 B 是线程安全的!
     */
    public AtomicInteger safeCurrHp;

    public synchronized void subtractHp(int val) {
        if (val <= 0) {
            return;
        }

        this.currHp = this.currHp - val;
    }

    /*public synchronized void attkUser(TestUser targetUser) {
        if (null == targetUser) {
            return;
        }

        int subtractHp = 10;
        targetUser.subtractHp(subtractHp);
    }*/

    // 细粒度上锁
    public void attkUser(TestUser targetUser) {
        if (null == targetUser) {
            return;
        }

        int subtractHp;
        synchronized (this) {
           subtractHp = 10;
        };

        targetUser.subtractHp(subtractHp);
    }
}

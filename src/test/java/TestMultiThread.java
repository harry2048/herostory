import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: gengwei
 * @Date: 2019-12-30 22:53
 * @Description:
 */
public class TestMultiThread {
    public static void main(String[] args) {
        for (int i = 1; i <= 5000; i++) {
            System.out.println("第" + i +" 次测试");
            (new TestMultiThread()).test4();
        }
    }

    private void test1() {
        TestUser newUser = new TestUser();
        newUser.currHp = 100;

        Thread[] threadArray = new Thread[2];

        for (int i = 0; i < threadArray.length; i++) {
            threadArray[i] = new Thread(() -> {
                newUser.currHp = newUser.currHp - 10;
            });
        }

        threadArray[0].start();
        threadArray[1].start();

        try {
            threadArray[0].join();
            threadArray[1].join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (newUser.currHp != 80) {
            throw new RuntimeException("当前血量错误：currHp = " + newUser.currHp);
        }

        System.out.println("当前血量正确：currHp = " + newUser.currHp);
    }

    private void test2() {
        TestUser newUser = new TestUser();
        newUser.currHp = 100;

        Thread[] threadArray = new Thread[2];

        for (int i = 0; i < threadArray.length; i++) {
            threadArray[i] = new Thread(() -> {
                newUser.subtractHp(10);
            });
        }

        threadArray[0].start();
        threadArray[1].start();

        try {
            threadArray[0].join();
            threadArray[1].join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (newUser.currHp != 80) {
            throw new RuntimeException("当前血量错误：currHp = " + newUser.currHp);
        }

        System.out.println("当前血量正确：currHp = " + newUser.currHp);
    }

    private void test3() {
        TestUser user1 = new TestUser();
        user1.currHp = 100;
        TestUser user2 = new TestUser();
        user2.currHp = 100;

        Thread[] threadArray = new Thread[2];

        threadArray[0] = new Thread(() -> {
            user1.attkUser(user2);
        });

        threadArray[1] = new Thread(() -> {
            user2.attkUser(user1);
        });

        for (Thread thread : threadArray) {
            thread.start();
        }

        for (Thread thread : threadArray) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("当前血量正确：currHp = " + user2.currHp);
    }

    private void test4() {
        TestUser newUser = new TestUser();
        newUser.safeCurrHp = new AtomicInteger(100);

        Thread[] threadArray = new Thread[2];

        for (int i = 0; i < threadArray.length; i++) {
            threadArray[i] = new Thread(() -> {
                newUser.safeCurrHp.addAndGet(-10);
            });
        }

        threadArray[0].start();
        threadArray[1].start();

        try {
            threadArray[0].join();
            threadArray[1].join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (newUser.safeCurrHp.get() != 80) {
            throw new RuntimeException("当前血量错误：currHp = " + newUser.safeCurrHp.get());
        }

        System.out.println("当前血量正确：currHp = " + newUser.safeCurrHp.get());
    }
}

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Auther: gengwei
 * @Date: 2019-12-31 23:40
 * @Description:
 */
public class TestBlockingQueue {
    public static void main(String[] args) {
        (new TestBlockingQueue()).test2();
    }

    private void test1() {
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>();

        Thread thead1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                blockingQueue.offer(i);
            }
        });

        Thread thead2 = new Thread(() -> {
            for (int i = 10; i < 20; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                blockingQueue.offer(i);
            }
        });

        Thread thead3 = new Thread(() -> {
            while (true) {
                try {
                    // blockingQueue会一直等待取数
                    Integer take = blockingQueue.take();
                    System.out.println("获取数值：" + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thead1.start();
        thead2.start();
        thead3.start();

        try {
            thead1.join();
            thead2.join();
            thead3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void test2() {
        MyExecutorService es = new MyExecutorService();

        // 第一个线程往里塞数
        Thread thead1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int x = i;
                es.submit(() -> System.out.println("x = " + x));
            }
        });

        // 第二个线程往里塞数
        Thread thead2 = new Thread(() -> {
            for (int i = 10; i < 20; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int x = i;
                es.submit(() -> System.out.println("x = " + x));
            }
        });


        thead1.start();
        thead2.start();

        try {
            thead1.join();
            thead2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class MyExecutorService {
        private final BlockingQueue<Runnable> _blockingQueue = new LinkedBlockingDeque<>();
        private final Thread _thread;

        MyExecutorService() {
            this._thread = new Thread(() -> {
                try {
                    while (true) {

                        // blockingQueue会一直等待取数
                        Runnable r = _blockingQueue.take();
                        if (null != r) {
                            r.run();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // 线程就绪
            _thread.start();
        }

        public void submit(Runnable r) {
            if (null != r) {
                this._blockingQueue.offer(r);
            }
        }
    }
}


//    CREATE DATABASE hero_story DEFAULT CHARACTER SET utf8;
//        USE hero_story;
//        CREATE TABLE `t_user` (
//        `user_id` int(11) NOT NULL AUTO_INCREMENT,
//        `user_name` varchar(64) DEFAULT NULL,
//        `password` varchar(64) DEFAULT NULL,
//        `hero_avatar` varchar(64) DEFAULT NULL,
//        PRIMARY KEY (`user_id`)
//        );
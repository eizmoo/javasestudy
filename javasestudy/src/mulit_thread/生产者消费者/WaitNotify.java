package mulit_thread.生产者消费者;

public class WaitNotify {
    private static final int[] LOCK = new int[1];

    private static int COUNT = 0;
    private static int FULL = 5;
    private static int EMPTY = 0;

    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify();

        new Thread(waitNotify.new Consumer()).start();
        new Thread(waitNotify.new Consumer()).start();
        new Thread(waitNotify.new Producer()).start();
        new Thread(waitNotify.new Consumer()).start();
        new Thread(waitNotify.new Producer()).start();
        new Thread(waitNotify.new Consumer()).start();

    }

    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    //如果产品池已满
                    while (COUNT == FULL) {
                        try {
                            System.out.println("生产者阻塞");
                            Thread.sleep(1000);
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("生产者生产,目前总数" + COUNT++);
                    LOCK.notifyAll();
                }

            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    //如果产品池为空
                    while (COUNT == EMPTY) {
                        try {
                            System.out.println("消费者阻塞");
                            Thread.sleep(1000);
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("消费者消费,目前总数" + COUNT-- + "剩余" + COUNT);
                    LOCK.notifyAll();
                }
            }
        }
    }
}

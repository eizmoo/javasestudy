package mulit_thread.thread_join;

public class ThreadJoinDemo {

    private static int i = 0;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println("t1: i" + ++i);
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            for (int j = 0; j < 10; j++) {
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2: i" + ++i);
            }
        });
        t2.start();
        // join只会生效一次，因为t1就执行结束了
    }

}

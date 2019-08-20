package mulit_thread.deamon_thread;

import java.util.concurrent.TimeUnit;

public class DeamonThreadDemo {

    public static void main(String[] args) {
        // 守护线程
        Thread deamonThread = new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                try {
                    Thread.sleep(300);
                    System.out.println("守护线程执行i" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        deamonThread.setDaemon(true);
        deamonThread.start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(300);
                    System.out.println("用户线程i " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("主线程结束");
    }

}

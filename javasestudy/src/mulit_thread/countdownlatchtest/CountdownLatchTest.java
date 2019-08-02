package mulit_thread.countdownlatchtest;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class CountdownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        int total = 10;
        CountDownLatch latch = new CountDownLatch(total);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("name-");
        executor.setCorePoolSize(total);
        executor.setMaxPoolSize(total);
        executor.initialize();

        for (int i = 0; i < total; i++) {
            executor.execute(() -> {
                try {
                    Thread.sleep(new Random().nextInt(20) * 1000);
                    System.out.println(Thread.currentThread().getName() + " is over");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
        }
        System.out.println(Thread.currentThread().getName() + "阻塞");
        latch.await();
        System.out.println("all is over");
    }

}

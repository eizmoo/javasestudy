package mulit_thread.threadpool;

import java.time.LocalDateTime;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Schedule注解的原理
 */
public class ScheduleThreadPoolDemo {

    public static void main(String[] args) {
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();

        TimerTask timerTask = new TimerTask(4000);

        System.out.printf("起始时间：%s\n\n", LocalDateTime.now());

        // 延时 1 秒后，按 3 秒的周期执行任务
        timer.scheduleAtFixedRate(timerTask, 1000, 3000, TimeUnit.MILLISECONDS);

    }

    private static class TimerTask implements Runnable {
        private final int sleepTime;

        public TimerTask(int sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            System.out.println("任务开始，当前时间：" + LocalDateTime.now().toString());

            try {
                System.out.println("模拟任务运行...");
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }

            System.out.println("任务结束，当前时间：" + LocalDateTime.now());
            System.out.println();
        }
    }


}

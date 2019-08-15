package mulit_thread.dead_thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 死锁
 */
public class DeadThread {


    private static final Object lockOne = new Object();
    private static final Object lockTwo = new Object();


    private void aLock() {
        synchronized (lockOne) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("get one lock \n trying lock two");
            synchronized (lockTwo) {
                try {
                    TimeUnit.DAYS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("get two and one lock");
        }
    }

    private void bLock() {
        synchronized (lockTwo) {
            try {
               Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("get two lock \n trying lock one");
            synchronized (lockOne) {
                try {
                    TimeUnit.DAYS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("get two and one lock");
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new Thread(() -> new DeadThread().aLock()).start();
        new Thread(() -> new DeadThread().bLock()).start();
    }

}

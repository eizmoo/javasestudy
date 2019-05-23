package mulit_thread.read_write_lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁demo
 */
public class ReadWriteLockDemo {

    /**
     * 读写锁
     */
    private static final ReadWriteLock RWL = new ReentrantReadWriteLock();

    private static final Lock writeLock = RWL.writeLock();
    private static final Lock readLock = RWL.readLock();

    private static Integer i = 1;

    /**
     * 写
     */
    public static void input() {
        writeLock.lock();

        try {
            System.out.println("write to i, current " + i);

            i++;

            System.out.println("write i to " + i);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放锁
            writeLock.unlock();
        }

    }

    /**
     * 读
     */
    public static void output() {
        readLock.lock();
        try {
            System.out.println("read i is " + i);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            readLock.unlock();
        }
    }

    public static void main(String[] args) {
        //5线程写
        new Thread(()->{
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < 5; j++) {
                ReadWriteLockDemo.input();
            }
        }).start();
        //10线程读
        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < 20; j++) {
                ReadWriteLockDemo.output();
            }
        }).start();

    }
}

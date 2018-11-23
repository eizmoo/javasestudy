package 多线程.对象锁;


/**
 * synchronized代码块:
 * 类锁: synchronized(Class) 锁住的是类
 * 对象锁: synchronized(object) 锁住的是对象,每个对象自己拥有一个锁
 * synchronized方法:
 * 就是对象锁
 */

public class ClassLock {

    public static void main(String[] args) {

        /*ClassLock classLock1 = new ClassLock();
        ClassLock classLock2 = new ClassLock();

        new Thread(() -> classLock1.lock()).start();
        new Thread(() -> classLock2.lock()).start();*/


    }

    private synchronized void lock() {
//        synchronized (this.getClass()) {
        int i = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this + " is locked " + i);
        } while (i++ < 10);
//        }
    }


}

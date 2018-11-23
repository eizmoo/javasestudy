package 多线程交叉打印;

public class ThreadCrossPrint {

    private static int[] source = new int[0];


    public static void main(String[] args) {
        final int[] count = {0};
        new Thread(() -> {
            synchronized (source) {
                while (count[0] <= 100){
                    try {
                        source.notify();
                        System.out.println("thread 1 print " + count[0]++);
                        source.wait();
                        System.out.println("thread 1 is waiting!!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (source) {
                while (count[0] <= 100){
                    try {
                        source.notify();
                        System.out.println("thread 2 print " + count[0]++);
                        source.wait();
                        System.out.println("thread 2 is waiting!!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

}

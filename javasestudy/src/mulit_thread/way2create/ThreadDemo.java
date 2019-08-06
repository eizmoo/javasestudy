package mulit_thread.way2create;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ExtendsThread().start();
        new Thread(new ImplementsRunnable()).start();
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        Future future = threadPool.submit(new ImplementsCallable());
        System.out.println(future.get());
        threadPool.shutdown();
    }

}

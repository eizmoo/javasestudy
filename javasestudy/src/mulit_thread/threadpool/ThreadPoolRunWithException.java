package mulit_thread.threadpool;

import java.util.concurrent.*;

public class ThreadPoolRunWithException {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        threadPool.execute(() -> System.out.println(1 / 0));
        threadPool.shutdownNow();
    }

}

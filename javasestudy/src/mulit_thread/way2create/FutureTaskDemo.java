package mulit_thread.way2create;

import java.util.concurrent.*;

public class FutureTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // task只能执行一次
        FutureTask<String> task = new FutureTask<>(() -> {
            System.out.println(" i am running");
            return "hello world";
        });
        FutureTask<String> task1 = new FutureTask<>(() -> {
            System.out.println("1 i am running");
            return "hello world1";
        });

        System.out.println("thread:");
        new Thread(task).start();
        System.out.println(task.get());

        System.out.println("executors:");
        Executors.newFixedThreadPool(1).submit(task1);
        System.out.println(task1.get());

    }

}

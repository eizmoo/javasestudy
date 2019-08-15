package mulit_thread.comparefuturetest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureDemo {

    private CompletableFuture futureNoReturn() throws InterruptedException {
        return CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("异步任务fun1执行完成");
        });
    }

    private void noReturnTest(CompletableFutureDemo demo) throws InterruptedException, ExecutionException {
        CompletableFuture future = demo.futureNoReturn();
        System.out.println("同步操作完成");
        System.out.println(future.get());
    }

    private CompletableFuture<String> futureWithReturn() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("begin invoke");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("end invoke");
            return "hello world";
        });
    }

    private void withReturnTest(CompletableFutureDemo demo) throws InterruptedException, ExecutionException {
        CompletableFuture future = demo.futureWithReturn();
        System.out.println("同步操作完成");
        // 如果开启下面两行注释，则一秒后将结果设置为mockValue。get直接返回mockValue
        //
        //        TimeUnit.SECONDS.sleep(1);
        //        future.complete("mock value");
        System.out.println(future.get());
    }

    public void sequenceInvoke() {

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        CompletableFutureDemo demo = new CompletableFutureDemo();

    }


}

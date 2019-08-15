# CompletableFuture
提供了四个方法来创建异步操作。
```
public static CompletableFuture<Void> runAsync(Runnable runnable)
public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)
```

没有指定Executor线程池的方法会使用ForkJoinPool.commonPool()作为线程池执行异步任务。
指定线程池则使用指定的线程池运行。

runAsync方法没有返回值。
supplyAsync方法有返回值。

实例：`CompletableFutureDemo.java`

# 执行任务，获取返回值
声明完CompletableFuture对象后任务就会执行，只有在get后才会阻塞当前线程。

### 自定义返回值
complate方法可以自定义返回值，设置后get则不会被阻塞而直接返回设置的值。

### 按照顺序执行异步任务
三个方法：thenApply、thenAccept、thenRun

| 方法名 | 是否能获取前一个任务的返回值 | 是否有返回值 |
| ------ | ------ | ------ |
| thenApply | 能 | 有 |
| thenAccept | 能 | 无 |
| thenRun | 不能 | 无 |


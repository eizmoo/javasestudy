# ThreadPoolExecutor

### 构造方法
#### 参数
- corePoolSize 
核心线程池大小，指常驻的线程数量，不会收到`keepAliveTime`的影响
如果设置`allowCoreThreadTimeOut=true`，则核心线程池也会收到存活时间的影响
- maximumPoolSize
最大线程池数量，超过这个数量的线程将会阻塞，如果等待队列为没有大小的`LinkedBlockingDeque`，此值设置无效
- keepAliveTime
失活时间，超过这个时间的非核心线程数会被回收
- timeUnit
失活时间的单位
- workQueue
等待队列 
常用队列：`SynchronousQueue` `LinkedBlockingDeque` `ArrayBlockingQueue`
- threadFactory
线程工厂
- handler
拒绝策略

### 执行规则
任务到来时：
1. 如果当前线程数<核心线程池数量，启动新的核心线程执行任务
2. 如果当前核心线程数大于等于核心线程池数量，将任务放在等待队列中
3. 如果加入等待队列失败，且核心线程数小于最大线程池数量，启动新的临时线程执行任务，这里的临时线程在任务执行完成之后，会在失活时间到之后被摧毁
4. 如果当前线程数到达最大线程数，执行拒绝策略

### 等待队列
- ArrayBlockingQueue
基于数组的FIFO队列，是有界的，创建时必须指定大小
- LinkedBlockingQueue
基于链表的FIFO队列，是无界的，默认大小是`Integer.MAX_VALUE`
使用这个等待队列时，因为任务永远可以加入等待队列，所以线程数将一致保持在核心线程池数，而最大线程数和拒绝策略将无效
- SynchronousQueue
一个比较特殊的队列，虽然它是无界的，但它不会保存任务，每一个新增任务的线程必须等待另一个线程取出任务，也可以把它看成容量为0的队列

### 拒绝策略
- AbortPolicy
抛出异常
- DiscardOldestPolicy
丢弃等待队列中最老的任务
- DiscardPolicy
直接丢弃
- CallerRunsPolicy
将任务交给调用线程来执行

### 应用
j.u.c包下的Executors，提供了线程池ThreadPoolExecutor的初始化接口
- newFixedThreadPool
```java
public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }
```
核心线程数量 = 最大线程数量，核心线程满了之后不会开启新线程，而是放入LinkedBlockingQueue队列，队列
长度为Integer.MAX_VALUE

- newSingleThreadExecutor
```java
public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
```
线程数只有1，是串行执行的线程池，保证任务的顺序

- newCachedThreadPool
```java
public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
```
1. 初始化一个可以缓存线程的线程池，默认缓存60s，线程池的线程数可达到Integer.MAX_VALUE，
即2147483647，内部使用SynchronousQueue作为阻塞队列
2. 和newFixedThreadPool创建的线程池不同，newCachedThreadPool在没有任务执行时，
当线程的空闲时间超过keepAliveTime，会自动释放线程资源，当提交新任务时，
如果没有空闲线程，则创建新线程执行任务，会导致一定的系统开销
3. 适用于短期异步的小任务，或负载教轻的服务器

- newScheduledThreadPool
```java
public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize);
    }
```
放入仅执行一次的任务或周期性执行的重复任务，在实际的业务场景中可以使用该线程池定期的同步数据

### 线程异常
execute抛出异常，submit不抛出异常
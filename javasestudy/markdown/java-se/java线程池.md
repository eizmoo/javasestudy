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


# AQS

`AbstractQueuedSynchronizer`中的属性：

```java
// 等待队列的头结点，表示当前持有锁的节点
private transient volatile Node head;
// 等待队列的尾节点
private transient volatile Node tail;
// 同步状态 大于0表示有锁。每次加锁就是给state+1。state=0为无锁态
// 通过CAS更改state值，保证其原子性
private volatile int state;
// 当前持有独占锁的线程，判断锁是否是重入
private transient Thread exclusiveOwnerThread;
```

AQS的等待队列如下图，**注意head是不在等待队列中的**。

![aqs-0](https://www.javadoop.com/blogimages/AbstractQueuedSynchronizer/aqs-0.png)

队列中每个线程包装为Node对象，以链表的形式存储：

```java
static final class Node {
	// 标识节点是共享模式下
	static final Node SHARED = new Node();
	// 标识节点是独占模式下
	static final Node EXCLUSIVE = null;

	// 节点状态，其值为下面四种
	volatile int waitStatus;
	// 取消状态，代表此线程取消了争夺锁
	static final int CANCELLED =  1;
	// 等待触发状态，如果当前线程的节点状态为 SIGNAL，则表明当前线程的后继节点需要被唤醒
	static final int SIGNAL    = -1;
	// 等待条件状态，表示当前节点在等待 condition，即在 condition 队列中
	static final int CONDITION = -2;
	// 状态需要向后传播，表示 releaseShared 需要被传播给后续节点，仅在共享锁模式下使用
	static final int PROPAGATE = -3;
    
    // 前驱节点的引用
    volatile Node prev;
    // 后继节点的引用
    volatile Node next;
    // 这个就是线程本尊
    volatile Thread thread;
}
```

> AQS结构大概由3部分组成：
>
> 1. **用 volatile 修饰的整数类型的 state 状态，用于表示同步状态，提供 getState 和 setState 来操作同步状态**
> 2. **提供了一个 FIFO 等待队列，实现线程间的竞争和等待，这是 AQS 的核心**
> 3. **AQS 内部提供了各种基于 CAS 原子操作方法，如 compareAndSetState 方法，并且提供了锁操作的acquire和release方法**

以`ReentrantLock`使用为例介绍代码：

```java
// 我用个web开发中的service概念吧
public class OrderService {
    // 使用static，这样每个线程拿到的是同一把锁，当然，spring mvc中service默认就是单例，别纠结这个
    private static ReentrantLock reentrantLock = new ReentrantLock(true);
    
    public void createOrder() {
        // 比如我们同一时间，只允许一个线程创建订单
        reentrantLock.lock();
        // 通常，lock 之后紧跟着 try 语句
        try {
            // 这块代码同一时间只能有一个线程进来(获取到锁的线程)，
            // 其他的线程在lock()方法上阻塞，等待获取到锁，再进来
            // 执行代码...
            // 执行代码...
            // 执行代码...
        } finally {
            // 释放锁
            reentrantLock.unlock();
        }
    }
}
```

创建`ReentrantLock`时，指定了入参`true`，这里要说明`ReentrantLock`在内部通过`Sync`管理锁，有公平和非公平两种，我们这里使用公平锁。

```java
public ReentrantLock(boolean fair) {
    sync = fair ? new FairSync() : new NonfairSync();
}
```

### 线程抢占锁

`ReentrantLock::lock`实际上是调用的`Sync::lock`

```java
static final class FairSync extends Sync {
    final void lock() {
        // 抢占锁，传入值1
        // 方法在AQS类中实现，不可修改
        acquire(1);
    }
}    
```

```java
	public final void acquire(int arg) {
        // 先说个大概，if里面两步操作
        // 1. tryAcquire尝试去获得锁，如果获取成功，则直接返回
        // 2. addWaiter将当前线程包装成Node对象，通过acquireQueued方法再次尝试获取锁，成功也会直接返回
        // 如果两部操作失败了，则将当前线程中断
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```

细说这几个方法

```java
	// 这个方法在FairSync中实现
	// 返回值为boolean类型，true代表获取到锁
	// 返回true： 1. 当前无锁态 2. 重入锁
	protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
        	// 如果state==0，即当前是无锁态
            if (c == 0) {
                // 即使是无锁态，因为是公平锁，需要先检查等待队列中有没有已经在等待的线程
                if (!hasQueuedPredecessors() &&
                    // 通过CAS去设置值，期望0即无锁，修改为1
                    compareAndSetState(0, acquires)) {
                    // 进入这里代表已经获取锁，设置当前线程为已获取线程
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
        	// state!=0 判断是不是重入
            else if (current == getExclusiveOwnerThread()) {
                // 是重入锁
                // 设置state值为oldState+1
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
        	// 尝试获取锁失败
            return false;
        }
```

在`tryAcquire`尝试获取锁失败后，会进入`acquireQueued`方法，但是会先将调用`addWaiter`将当前线程包装成Node对象并入等待队列。

```java
	// 参数mode此时是Node.EXCLUSIVE，独占模式
	private Node addWaiter(Node mode) {
        // 当前线程包装为Node 
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        // 类初始化时，tail为null
        // 此处的判断为tail是否为空
        if (pred != null) {
            // 将当前的尾节点设置为自己的头节点
            node.prev = pred;
            // CAS设置，如果失败证明有线程在竞争
            if (compareAndSetTail(pred, node)) {
                // 形成双向链表
                pred.next = node;
                return node;
            }
        }
        // 进入这里场景有2种： 1. 队列为空(tail==null) 2. CAS失败
        enq(node);
        return node;
    }
```

```java
	// 将node插入队列，必要情况下初始化队列(队列有可能为null)
	private Node enq(final Node node) {
        // 无限循环
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize
                // 队列为空，必须初始化
                // 将head设置为空node
                if (compareAndSetHead(new Node()))
                    // 如果设置成功，尾头指向同一个node对象
                    tail = head;
            } else {
                // 队列不为空，将尾node设置尾head的上一个节点
                node.prev = t;
                // 通过CAS将tail设置为当前node
                // 这一步有可能失败，失败会继续循环，直到插入成功
                if (compareAndSetTail(t, node)) {
                    // 设置成功，形成双向链表
                    t.next = node;
                    return t;
                }
            }
        }
    }
```

`addWaiter`方法后，当前线程的Node对象已经进入等待队列中，随后会执行`acquireQueued`方法。

先重温一下`acquire`方法：	

```java
if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
```

`acquireQueued`方法返回值为true情况下会将当前线程阻塞。

```java
	final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            // 无限循环
            for (;;) {
                // p为当前node的上一节点
                final Node p = node.predecessor();
                // 如果p为head节点(注意head节点是获得锁的节点)，证明等待队列中第一位
                // 尝试获取锁，为什么是尝试，因为在第一次初始化阻塞队列时head是空node，不是正在执行的线程
                if (p == head && tryAcquire(arg)) {
                    // 获取锁成功，将自身设置为head节点
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    // 返回中断标识：false
                    return interrupted;
                }
                // 获取锁失败
                // 检查、更新节点状态，此方法返回true证明需要线程阻塞
                // 如果此方法返回false，会继续循环
                if (shouldParkAfterFailedAcquire(p, node) &&
                    // 执行这个方法的前提是前置方法返回true即需要阻塞，此方法就是阻塞线程
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
```

```java
	private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
    	// 上一节点状态为需要通知，正常状态，返回true中断
        // Node.SIGNAL == -1 意思是当前节点的下一节点需要被唤醒
        if (ws == Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            // 上一节点状态为被取消，跳过上一节点，将自身节点设置为上上节点的下节点(类似于删除上一节点)
            // 因为自身节点被唤醒取决于上一节点，要保持上一节点的有效
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE.  Indicate that we
             * need a signal, but don't park yet.  Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            // 上一节点状态为 0 或者 -2、-3
            // 每个新入队的节点，status都是0，所以这一步将前驱节点状态设为-1，即自己需要被唤醒
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        // 这里返回false，会在acquireQueued方法重新走一边循环
        return false;
    }
```

```java
	private final boolean parkAndCheckInterrupt() {
        // 挂起线程，会卡在这一步，等待唤醒
        LockSupport.park(this);
        return Thread.interrupted();
    }
```

代码就讲完了，开始分析可能出现的场景

初始-无锁态,线程a`tryAcquire`成功，直接返回。

线程a执行时，线程b尝试lock

- `tryAcquire`失败，线程加入等待队列
- 进入`acquireQueued`方法，此时的队列刚初始化完，p==head但是获取锁会失败
- 线程b中断



### 释放锁

线程在没有抢占到锁会，会通过`LockSupport.park(this)`中断，等待被唤醒

```java
	// 调用解锁
	public void unlock() {
    	sync.release(1);
	}

	// AQS 方法
	public final boolean release(int arg) {
        // state-1，返回true表示锁释放，需要唤醒其他线程
        if (tryRelease(arg)) {
            Node h = head;
            // 如果head==null或者h的等待状态为0，表示队列中没有需要唤醒的线程
            if (h != null && h.waitStatus != 0)
                // 唤醒后继节点
                unparkSuccessor(h);
            return true;
        }
        return false;
    }

	// 减state值，返回时true表示当前锁已经被释放掉(如果是重入，则不会被释放掉，返回false)
    protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
        	// 如果-1之后的state==0，证明当前线程占用结束
            if (c == 0) {
                // 表示释放线程、设置占用锁线程为null
                free = true;
                setExclusiveOwnerThread(null);
            }
        	// 更新值，此处不会有并发
            setState(c);
            return free;
        }

	// 唤醒后续线程
	private void unparkSuccessor(Node node) {
        /*
         * If status is negative (i.e., possibly needing signal) try
         * to clear in anticipation of signalling.  It is OK if this
         * fails or if status is changed by waiting thread.
         */
        int ws = node.waitStatus;
        // 将当前节点状态 将head的等待状态改成0
        // 如果不该改，唤醒的永远都是head节点
        if (ws < 0)
            compareAndSetWaitStatus(node, ws, 0);

        /*
         * Thread to unpark is held in successor, which is normally
         * just the next node.  But if cancelled or apparently null,
         * traverse backwards from tail to find the actual
         * non-cancelled successor.
         */
        // 唤醒后续节点，但是有可能节点已经取消了等待(waitStatus==1)
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            // 从队尾找，找到waitStatus<=0的所有节点中排在最前面的
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        // 唤醒
        if (s != null)
            LockSupport.unpark(s.thread);
    }
```

唤醒后，被阻塞的线程进入方法

```java
	private final boolean parkAndCheckInterrupt() {
        // 刚刚挂起的位置
        LockSupport.park(this);
        // 唤醒后此处会返回false，继而回到acquireQueued方法的循环中
        return Thread.interrupted();
    }
```

## 总结

总结一下吧。

在并发环境下，加锁和解锁需要以下三个部件的协调：

1. 锁状态。我们要知道锁是不是被别的线程占有了，这个就是 state 的作用，它为 0 的时候代表没有线程占有锁，可以去争抢这个锁，用 CAS 将 state 设为 1，如果 CAS 成功，说明抢到了锁，这样其他线程就抢不到了，如果锁重入的话，state进行 +1 就可以，解锁就是减 1，直到 state 又变为 0，代表释放锁，所以 lock() 和 unlock() 必须要配对啊。然后唤醒等待队列中的第一个线程，让其来占有锁。
2. 线程的阻塞和解除阻塞。AQS 中采用了 LockSupport.park(thread) 来挂起线程，用 unpark 来唤醒线程。
3. 阻塞队列。因为争抢锁的线程可能很多，但是只能有一个线程拿到锁，其他的线程都必须等待，这个时候就需要一个 queue 来管理这些线程，AQS 用的是一个 FIFO 的队列，就是一个链表，每个 node 都持有后继节点的引用。AQS 采用了 CLH 锁的变体来实现，感兴趣的读者可以参考这篇文章[关于CLH的介绍]( http://coderbee.net/index.php/concurrent/20131115/577)，写得简单明了。

## 示例图解析

下面属于回顾环节，用简单的示例来说一遍，如果上面的有些东西没看懂，这里还有一次帮助你理解的机会。

首先，第一个线程调用 reentrantLock.lock()，翻到最前面可以发现，tryAcquire(1) 直接就返回 true 了，结束。只是设置了 state=1，连 head 都没有初始化，更谈不上什么阻塞队列了。要是线程 1 调用 unlock() 了，才有线程 2 来，那世界就太太太平了，完全没有交集嘛，那我还要 AQS 干嘛。

如果线程 1 没有调用 unlock() 之前，线程 2 调用了 lock(), 想想会发生什么？

线程 2 会初始化 head【new Node()】，同时线程 2 也会插入到阻塞队列并挂起 (注意看这里是一个 for 循环，而且设置 head 和 tail 的部分是不 return 的，只有入队成功才会跳出循环)

```java
private Node enq(final Node node) {
    for (;;) {
        Node t = tail;
        if (t == null) { // Must initialize
            if (compareAndSetHead(new Node()))
                tail = head;
        } else {
            node.prev = t;
            if (compareAndSetTail(t, node)) {
                t.next = node;
                return t;
            }
        }
    }
}
```

首先，是线程 2 初始化 head 节点，此时 head==tail, waitStatus==0

![aqs-1](https://www.javadoop.com/blogimages/AbstractQueuedSynchronizer/aqs-1.png)

然后线程 2 入队：

![aqs-2](https://www.javadoop.com/blogimages/AbstractQueuedSynchronizer/aqs-2.png)

同时我们也要看此时节点的 waitStatus，我们知道 head 节点是线程 2 初始化的，此时的 waitStatus 没有设置， java 默认会设置为 0，但是到 shouldParkAfterFailedAcquire 这个方法的时候，线程 2 会把前驱节点，也就是 head 的waitStatus设置为 -1。

那线程 2 节点此时的 waitStatus 是多少呢，由于没有设置，所以是 0；

如果线程 3 此时再进来，直接插到线程 2 的后面就可以了，此时线程 3 的 waitStatus 是 0，到 shouldParkAfterFailedAcquire 方法的时候把前驱节点线程 2 的 waitStatus 设置为 -1。

![aqs-3](https://www.javadoop.com/blogimages/AbstractQueuedSynchronizer/aqs-3.png)

这里可以简单说下 waitStatus 中 SIGNAL(-1) 状态的意思，Doug Lea 注释的是：代表后继节点需要被唤醒。也就是说这个 waitStatus 其实代表的不是自己的状态，而是后继节点的状态，我们知道，每个 node 在入队的时候，都会把前驱节点的状态改为 SIGNAL，然后阻塞，等待被前驱唤醒。这里涉及的是两个问题：有线程取消了排队、唤醒操作。





### 文章引用

[java并发之ASQ源码分析](http://objcoding.com/2019/05/05/aqs-exclusive-lock/)

[一行一行源码分析清楚AbstractQueuedSynchronizer](https://www.javadoop.com/post/AbstractQueuedSynchronizer)


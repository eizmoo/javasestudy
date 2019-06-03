# 线程的五种状态
初始化、可运行、运行、阻塞(IO、等待池、锁池)、结束
初始化 -> 可运行 Thread::start
可运行 -> 运行 线程获取时间片
运行  ->  可运行 时间片用完
运行  ->  阻塞  需要用户输入、Thread::sleep、Thread::join
运行  ->  等待池 Object::wait
运行  ->  锁池 synchronized等，等待获取锁
等待池 ->  锁池 Object::notify\notifyAll
锁池  ->  可运行 获取锁
运行  ->  结束 run方法执行完毕

![状态流转图](http://upload-images.jianshu.io/upload_images/13491113-47750b2051d87aa1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

# 几种方式让线程进入等待状态
线程调用wait()后进入等待池，被notify进入锁池
线程直接调用synchronized进入锁池
锁池的获取锁后进入可运行

# 线程池
入参：核心线程数、最大线程数、线程失活时间、时间单位、线程工厂、拒绝策略、等待队列
拒绝策略：抛出异常、直接抛弃、抛弃队列最老的任务、由任务请求方去执行任务

# 如何打印线程栈
命令行中： jstack
代码中： Thread.getAllStackTraces()

# 并发包下的condition
需要Lock.lock，通过Lock.newCondition()获取
await、signal类似于wait、notify

# 如何排查内存泄漏
查看是否频繁fullGC（工具Jvisualvm）

# OOM出现场景
堆内存不足、GC发现GC效率不高、方法区内存不足、元空间内存不足

# 类双亲加载
一个类的加载将最先交给最父类的加载器，如果可以被父加载则不会用子加载

# SpringBoot特点
1. 创建独立的Spring应用程序
2. 嵌入的Tomcat，无需部署WAR文件
3. 简化Maven配置
4. 自动配置Spring
5. 绝对没有代码生成和对XML没有要求配置

# @config的类下bean和@compent引入的区别
区别不大，不过config下可以引入第三方的类，即进行配置JDK里面的第三方框架，@compent不能做到

# 如何实现自旋锁
cas，无锁
AtomicReference<Thread>::compareAndSet(this,null)
如果不为null则设置失败，即获取锁失败

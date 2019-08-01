Object类中的方法：
1. wait()/wait(long)/wait(long,int) --native
线程休眠，无参则一直睡眠，有参则睡眠指定时长，睡眠时可以被notify*()防范唤醒
2. notify()/notifyAll() --native
唤醒该对象的某个/全部阻塞/睡眠线程
3. hashCode() --native
作用在集合类中
4. toString()
类的描述
5. equals(Object)
比较两个对象是否相等，默认实现 `==`， 即比较对象地址
6. getClass() --native
获取类对象Class
7. clone() --native
克隆--浅拷贝/深拷贝
8. finalize()
在对象被第一次回收之前调用，第二次则不会被调用
# ThreadLocal

### ThreadLocalMap
ThreadLocalMap是ThreadLocal的内部类，其内部实现菜采用哈希表，不过与HashMap不同的是HashMap解决冲突用的是拉链法(数组+链表)，而ThreadLocalMap采用`开放定址法`。
#### 开放定址法
开放定址法不会创建链表，当关键字散列到的数组单元已经被另外一个关键字占用的时候，就会尝试在数组中寻找其他的单元，直到找到一个空的单元。探测数组空单元的方式有很多，这里介绍一种最简单的 -- 线性探测法。线性探测法就是从冲突的数组单元开始，依次往后搜索空单元，如果到数组尾部，再从头开始搜索（环形查找）。如下图所示：
![线性探测法](https://images2015.cnblogs.com/blog/690323/201703/690323-20170329205938858-247715200.jpg)

可以看出其实开放定址法一旦产生冲突后的解决办法是十分简单粗暴的，缺点页显而易见：霸占了别的元素的位置，出现冲突之后的hash对应下表已经不再准确了。

那么为什么ThreadLocalMap会用开放定址法呢，因为在 ThreadLocalMap 中的散列值分散的十分均匀，很少会出现冲突。并且 ThreadLocalMap 经常需要清除无用的对象，使用纯数组更加方便。

1. Entry对象，即ThreadLocalMap的对象数组，以ThreadLocal为key值
使用弱引用 TODO
```java
static class Entry extends WeakReference<ThreadLocal<?>> {
        Object value;
        // 以ThreadLocal对象为k的kv实体
        Entry(ThreadLocal<?> k, Object v) {
            super(k);
            value = v;
        }
    }
```
2. set方法

```java
private void set(ThreadLocal<?> key, Object value) {
        Entry[] tab = table;
        int len = tab.length;
        // 计算插入下标，threadLocalHashCode字段每次调用都会在之前的hash加上0x61c88647
        // 正是这个值才保证了散列表的少冲突
        int i = key.threadLocalHashCode & (len-1);
        // 循环，从指定的下标开始
        // 循环的递归条件是数组中下一个位置   
        for (Entry e = tab[i];
             e != null;
             e = tab[i = nextIndex(i, len)]) {
            ThreadLocal<?> k = e.get();
            // 同k，替换value
            if (k == key) {
                e.value = value;
                return;
            }
            // 如果k==null，但是对象不为空，证明这个v对应的k已经‘过时’，原来的k已经被垃圾回收
            if (k == null) {
                // 替换结果，这个方法里做了一些垃圾回收的操作
                replaceStaleEntry(key, value, i);
                return;
            }
        }
        // 指定下表为的entry为null
        tab[i] = new Entry(key, value);
        // 判断是否需要扩容
        int sz = ++size;
        if (!cleanSomeSlots(i, sz) && sz >= threshold)
            rehash();
    }
```

3. getEntry方法
```java
private Entry getEntry(ThreadLocal<?> key) {
        // 计算下标
        int i = key.threadLocalHashCode & (table.length - 1);
        Entry e = table[i];
        // 下标的值就是想要的
        if (e != null && e.get() == key)
            return e;
        else
            // 线性探测
            return getEntryAfterMiss(key, i, e);
    }
private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
        Entry[] tab = table;
        int len = tab.length;
        // 与set中的查找操作基本没有差别
        while (e != null) {
            ThreadLocal<?> k = e.get();
            if (k == key)
                return e;
            if (k == null)
                expungeStaleEntry(i);
            else
                i = nextIndex(i, len);
            e = tab[i];
        }
        return null;
    }
```
4. remove删除方法
就是将指定的Entry的k设置成null，然后调用清除过期实体的方法
```java
private void remove(ThreadLocal <?> key) {
    Entry[] tab = table;
    int len = tab.length;
    int i = key.threadLocalHashCode & (len - 1);
    for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
        if (e.get() == key) {
            e.clear();
            expungeStaleEntry(i);
            return;
        }
    }
}
```

看完ThreadLocalMap的增删改查后，ThreadLocal的存取就比较简单了。

### get
获取当前线程存储在`threadlocal`中的值：
```java
public T get() {
    // 根据当前线程，获取ThreadLocalMap
    Thread t = Thread.currentThread();
    // ThreadLocalMap是在线程Thread对象中保存的一个属性对象
    ThreadLocalMap map = getMap(t);
    // 如果Map不为空，获取map.Entry的value，强转后返回
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
    // map为null，初始化threadLocal的值后返回默认值
    return setInitialValue();
}
public void set(T value) {
    // 获取ThreadLocalMap
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    // 更新或者新增Map的值
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}
```
可以看出，对ThreadLocal的操作实质上就是对Thread中的ThreadLocalMap的操作。

### 内存泄漏
![对象引用](https://user-gold-cdn.xitu.io/2018/9/25/1660f7ebfeecebf2?imageView2/0/w/1280/h/960/format/webp/ignore-error/1)

ThreadLocalMap的Entry的k是对ThreadLocal对象的弱引用，而ThreadLocalMap拥有Entry数组的强引用，如果使用ThreadLocal::remove方法

Entry拥有两个引用链。一条是在Thread中的ThreadLocalMap的强引用，一条是将ThreadLocal作为K的弱引用，当把threadlocal实例置为null以后,没有任何强引用指向threadlocal实例,所以threadlocal将会被gc回收，但是Entry因为拥有Thread的引用而无法回收，在ThreadLocal被释放后，Thread回收前这段时间内发生了短暂的内存泄漏。更为严重的问题是：在使用线程池的场景下，每个线程都是复用的，永远不会释放，这这个场景下，ThreadLocal会出现内存泄漏的现象。
调用ThreadLocal::remove方法可以解决这个问题，tomcat的线程池也做到了每次请求完都会处理ThreadLocal再进行复用

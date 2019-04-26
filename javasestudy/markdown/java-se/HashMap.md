# HashMap
### 属性

> static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
默认初始化容量，1左移4位，也就是16

> static final int MAXIMUM_CAPACITY = 1 << 30;
最大容量，1左移30位，最大值小于等于这个值

> static final float DEFAULT_LOAD_FACTOR = 0.75f;
默认加载因子，加载因子是用来确认hashmap容量在使用多少时进行扩容的值，默认是3/4

> static final Entry<?,?>[] EMPTY_TABLE = {};
空Entry表，Entry就是HashMap的每一个键值对的保存实体对象

> transient int size;
记录hashmap中真实保存的键值对数量

### 方法
#### 构造方法
```java
/**
 * 设置初始容量和加载因子
 * 未指定时使用默认值
 */
public HashMap(int initialCapacity, float loadFactor) {
    //检验初始容量是否合理： >=0 && <= MAXIMUM_CAPACITY
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    //校验负载因子合法
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " + loadFactor);

    this.loadFactor = loadFactor;
    threshold = initialCapacity;
    //初始化方法，用于继承时重写，自定义构造
    init();
    }
```
```java
/**
* 传入一个map对象，将map里的全部Entrty转入新map中
*/
public HashMap(Map<? extends K, ? extends V> m) {
    this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,
                      DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
    inflateTable(threshold);

    putAllForCreate(m);
}

```




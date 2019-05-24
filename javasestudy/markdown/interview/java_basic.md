# JAVA中的几种基本数据类型是什么，各自占用多少字节
  
- byte 1字节  -128～127
- short 2字节 -32768～32767
- char 2字节 0~65535
- int 4字节 -2147483648~2147483647
- long 8字节 -9223372036854775808~9223372036854775807
- float 4字节 1.4E-45~3.4028235E38
- double 8字节 4.9E-324~1.7976931348623157E308
- boolean 1字节 true|false

# 基本类型自动装拆箱
### 装、拆箱方法
装箱使用valueOf()方法，拆箱使用xxValue() (例如Integer::intValue，返回的是int) 

### 缓存
装箱时(使用了valueOf()方法)，包装类有自有的缓存，缓存-128～127之间的包装类(Character是0～127)
Float和Double无缓存值。Boolean缓存的是true和false(全部值)，Byte缓存的也是全部值。

可以通过启动时的配置 `-Djava.lang.Integer.IntegerCache.high=100` 来配置缓存的上限。

### 缓存意义
缓存的意义目的主要是节省内存，由于更好的缓存效率，这也可以带来更快的代码。

基本上，Integer该类保留了Integer-128到127范围内的实例缓存，并且所有自动装箱，文字和用法Integer.valueOf()都将从该缓存中返回其覆盖范围的实例。

这是基于这样的假设，即这些小值比其他整数更频繁地出现，因此避免为每个实例使用不同对象的开销是有意义的（Integer对象占用12个字节之类的东西）。

# String类能被继承吗，为什么
不可继承，因为是final修饰的类。
### 优点 
- 线程安全
- 字符串池
- 创建HashCode不可变性

#String，StringBuffer，StringBuilder的区别
String是不可变的字符串类，内部采用final char[]数组维护
String在拼接时是通过创建SpringBuilder对象生成新的String对象
buffer和builder都是提供多次拼接append方法，不同在于buffer在一些方法上加了synchronized关键字，线程安全，但是速度会慢于builder

# ArrayList和LinkedList有什么区别
底层一个是数组一个是双向链表，所以差异就在数据结构上
arrayList 随机读取，读O(1)，插入的平均时间复杂度O(n)，因为是数组，需要动态扩容
linkedList 插入O(1)，读取O(n)，不需要动态扩容，但是数据量越大读取时间越长，因为是顺序读取

#讲讲类的实例化顺序，比如父类静态数据，构造函数，字段，子类静态数据，构造函数，字段，当new的时候，他们的执行顺序
1. 基类静态代码块，基类静态成员字段 （并列优先级，按代码中出现先后顺序执行）（只有第一次加载类时执行）
2. 派生类静态代码块，派生类静态成员字段 （并列优先级，按代码中出现先后顺序执行）（只有第一次加载类时执行）
3. 基类普通代码块，基类普通成员字段 （并列优先级，按代码中出现先后顺序执行）
4. 基类构造函数
5. 派生类普通代码块，派生类普通成员字段 （并列优先级，按代码中出现先后顺序执行）
6. 派生类构造函数

# 用过哪些Map类，都有什么区别，HashMap是线程安全的吗,并发下使用的Map是什么，他们内部原理分别是什么，比如存储方式，hashcode，扩容，默认容量等
treeMap底层红黑树，实现插入的key可按照规则排序，默认是key的升序
linkedHashMap链表+哈希表，保证插入顺序的哈希表,还有一个参数决定是否在此基础上再根据访问顺序(get,put)排序，通过双向链表维护顺序
hashMap

# 抽象类和接口的区别，类可以继承多个类么，接口可以继承多个接口么,类可以实现多个接口么
- 相同
接口和抽象类不可被实例化
抽象类和1.8之后的接口都可以有未实现的方法和已实现的方法。在抽象类中叫做抽象方法（无实现）和非抽象方法（有实现），接口中有实现的方法叫做默认方法。

- 不同
接口可继承多接口，一个类可实现多个接口;抽象类只能单继承
接口代表has A，抽象类代表is A，即抽象类是对本质的抽象，接口是对行为的抽象
继承抽象类的类如果不是抽象类，那么一定要继承方法。抽象类中的方法不能是static和private的

# 反射的原理，反射创建类实例的三种方式是什么
- 通过对象.getClass（）方式
- 通过类名.Class 方式
- 通过Class.forName 方式

# 反射中，Class.forName和ClassLoader区别
Class.forName除了将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块。
classloader只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容，只有在newInstance才会去执行static块
Class.forName(name,initialize,loader)带参数也可控制是否加载static块。并且只有调用了newInstance()方法采用调用构造函数，创建类的对象。只不过默认是true，所以默认解释并执行类中的static块
ClassLoader就是遵循双亲委派模型最终调用启动类加载器的类加载器，实现的功能是“通过一个类的全限定名来获取描述此类的二进制字节流”，获取到二进制流后放到JVM中。Class.forName()方法实际上也是调用的CLassLoader来实现的。

# 动态代理与cglib实现的区别
jdk动态代理要求代理类继承于接口
cglib生成代理类的子类，覆盖其中的所有方法，所以该类或方法不能声明称final的
当目标类没有实现接口时，使用cglib，否则使用JDK动态代理(实现接口也可以强制使用cglib)

# 为什么CGlib方式可以对接口实现代理
CGLib采用了非常底层的字节码技术，其原理是通过字节码技术为一个类创建子类，并在子类中采用方法拦截的技术拦截所有父类方法的调用，顺势织入横切逻辑。

# final的用途
作用在类：类不可被继承，其内部方法即也隐式被指定为final方法
作用在方法：方法不可被重写(可以被重载)
作用在变量：引用第一次定义之后不可变，引用的属性可变

# 单例实现方式
- 懒汉式，线程不安全
```java
public class 懒汉式 {

    private static A a;

    public static A getInstance() {
        if (a == null) {
            a = new A();
        }
        return a;
    }
}
```
- 饿汉式，线程安全，但是失去了延迟实例化
```java
public class 饿汉式 {

    private static A a = new A();

    public static A getInstance() {
        return a;
    }

}
```
- 懒汉式并发锁，线程安全，但是同一时间只有一个线程可以访问获取实例方法，极大降低并发
```java
public class 懒汉式并发锁 {
    private static A a;

    public synchronized static A getInstance() {
        if (a == null) {
            a = new A();
        }
        return a;
    }
    
}
```
- 双重检查锁，线程安全 volatile保证不会发生指令重排，双重检查保证在并发下也可以只实例化一次
```java
public class 双重检查锁 {

    private static volatile A a;

    public static A getInstance() {
        if (a == null) {
            synchronized (双重检查锁.class) {
                if (a == null) {
                    a = new A();
                }
            }
        }
        return a;
    }

}
```
- 枚举实现，推荐。枚举类类似于静态内部类，提供了序列化机制，绝对防止多次实例化，及时面对复杂的序列化或者反射攻击。
在《Effective Java》中提到单元素枚举类型已经成为实现Singleton的最佳方法。
```java
public enum 枚举实现 {
    INSTANCE;

    private A a;

    枚举实现() {
        this.a = new A();
    }

    public A getInstance() {
        return a;
    }
}
```
用法为`枚举实现.INSTANCE.getInstance();`

# 请结合OO设计理念，谈谈访问修饰符public、private、protected、default在应用设计中的作用
public公共，任何位置都可以访问
protected 同包及子类访问
default 同包访问
private 只有类自己可以访问，其他类不需要知道这个类

# 深拷贝和浅拷贝区别
浅拷贝 object::clone方法，实际是将示例的引用复制给新对象声明，实际两个声明是堆上同一对象
深拷贝 需要自己手动实现，意思是完全相同的两个实例对象

# error和exception的区别，CheckedException，RuntimeException的区别
error 程序无法处理的错误,发生于虚拟机自身或虚拟机试图执行应用,可能会导致中断线程
exception 操作或操作可能会引发的错误,可以被程序处理 分为两种CheckedException检查时异常和RuntimeException运行时异常
    检查时异常是必须要被try catch，运行时异常无强制要求try catch
    
# 请列出5个运行时异常
NullPointerException 空指针
ArrayIndexOutOfBoundsException 数组越界
ClassCastException 类型转换异常
IllegalArgumentException 非法参数异常
IOException 输入输出异常

# 在自己的代码中，如果创建一个java.lang.String类，这个类是否可以被类加载器加载？为什么
自定义类加载器可以实现加载自己的类，但是因为双亲委派机制，不可以加载跟jdk中现有的类重名的类。
如果想跳过这个限制，需要在自定义类加载器时重写classLoader方法，但是仍不可以加载自定义java包的类。
用自定义的类加载器强行加载，也会收到一个SecurityException。

# 说一说你对java.lang.Object对象中hashCode和equals方法的理解。在什么场景下需要重新实现这两个方法
equals方法比较两个对象是否相等，当使用属性或特殊方法判断对象是否一致时重新实现
hashCode方法计算hashcode，当需要在使用hash存储时为了让元素更分散或更密集时重新实现

主要在以哈希表为数据结构的集合类中。以HashMap为例，在判断一个key是否存在是根据hashCode和equals()方法实现的判断的。
如果使用一个对象作为hashMap的key，那么需要重写这两个方法，以实现自己判断两个对象是否相同
equals和hashcode关系： equals=true，hashCode一定相同; hashCode相同，equals不一定相等

# 在jdk1.5中，引入了泛型，泛型的存在是用来解决什么问题
泛型实现了参数化类型的概念，使代码可以用于多种类型。希望类或方法能够具备最广泛的表达能力
用于编译期检查，实际编译后会将泛型擦除以兼容1.5之前的代码

# 什么是序列化，怎么序列化，为什么序列化，反序列化会遇到什么问题，如何解决
序列化一般是指把结构化的对象变成无结构的字节流，便于存储、传输
序列化是指将对象按照某种协议格式（某种约定方式）放入一个buffer中，其目的是便于网络传输或持久存储
反序列化，就是将序列化后的buffer按照序列化时还原成原来的对象，这样程序就能直接使用还原的对象了

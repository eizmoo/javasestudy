# feign如何负载均衡
ribbon配合RestTemplate负载均衡
feign是RESTful化的RPC

# 注册中心挂掉了会怎么样
zuul正常访问，但是zuul的route会报错

# 线程池核心线程的摧毁
如果设置过期时间，就过期摧毁
线程调用exception，该线程也会被摧毁
线程池shutdown会摧毁

# hashMap为何是2次幂
为了扩容时的重哈希打散链表

# jdbc的class.forname做了什么
class.forname除了将类加载到jvm中，还会解析类，执行类的静态代码
Driver类中有静态代码块是执行的注册驱动

# 双亲加载

# 组合索引

# 接口幂等

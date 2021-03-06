# 五种数据结构
1. string
   - 最基础的字符串类型，一个key对应一个value
   - 最大可存储512M的值

2. list

   - k对应一个list
   - 按照插入顺序排序，可以添加一个元素到列表头或列表尾
   - 通过lpush和lrange操作

3. hash

   - string类型的field和value的映射表

     ![](https://images2018.cnblogs.com/blog/1368782/201808/1368782-20180821202632663-939669694.png)



4. set
   - 通过hashtable实现，概念类似于集合，可以进行交集、并集、差集
   - 元素无序，增增删查速度为O(1)
5. zset
   - 可排序的set，用户提供一个优先级score参数，插入是按照score从小到大的顺序排序

![](C:\Users\dataqin\Desktop\1565248142087.png)



# Redis为什么这么快

redis是基于内存的单进程单线程模型的KV型数据库，官方提供的数据是可以达到100000+的QPS（每秒内查询次数）。

原因：

1. 纯内存操作
2. 结构简单，对数据的操作也十分简单
3. 单线程，避免了上下文切换和竞争条件，也不会因为切换而消耗CPU，不需要考虑锁的问题
4. 多路IO复用模型，非阻塞IO
5. 自己构建了通信协议

# Redis分布式锁

setnx方法可以原子性的设置锁，如果设置成功证明获取锁，否则证明锁已存在。

之所以不采用先get再set是因为无法保障原子性.

但是自己维护锁的超时时间可能出现业务处理时间超过锁的有效时间的场景，从而导致并发。

reidsson提供了redis锁，通过看门狗机制保持redis的时间

# 为什么要在项目中使用redis

1. 登录信息的缓存，使用reids作为session保存机制。好处是将session交给中间件而不是系统内部保存，可以解决分布始中的session共享的问题。
2. 系统配置信息的缓存，因为信息不会被频繁的更改，存在redis可有效减少对数据库的访问，给其他需要频繁访问数据库的请求让路，减轻数据库压力。
3. 订单操作使用redis分布式锁，因为系统是分布式的，使用synchronize或者ReenterntLock无法解决多服务之间的冲突。
4. 服务项访问数据存放redis，有些服务项数据在短时间内不会改变，存放在redis中可以省钱。

主要是为了高性能和高并发。

# 使用了缓存可能出现的问题

1. 数据库缓存双写不一致

   - 高并发下出现

   - 使用先更新数据库再清楚缓存

2. 缓存雪崩

   - 缓存崩溃后请求全部去访问数据库导致宕机

3. 缓存穿透

   - 恶意批量访问一些不存在的值导致频繁访问数据库导致宕机(恶意攻击)

# redis线程模型

单线程工作 NIO异步

此处的单线程是指在处理网络请求的时候只有一个线程来处理，

# 持久化方式
### aof

将redis的操作日志追加到文件。

相比较于rdb，他弥补了数据的不一致性。恢复数据时会根据日志文件将所有操作完整的执行一遍以完成数据的恢复工作。

redis.conf中手动打开配置：

```
# 1. redis默认关闭，手动打开
appendonly yes
# 2. 指定本地数据库文件名，默认值为 appendonly.aof
appendfilename "appendonly.aof"
# 3. 指定更新日志条件
appendfsync everysec
```

更新条件appendfsync有三种选项：

1. always: 同步持久化，每次数据变化会立刻写入擦盘。性能差，数据完整性好
2. everysec: 出厂默认推荐，每秒异步记录一次(这一秒内的变化写在内存中，会丢失)
3. no: 不同步

日志文件超过一定大小后会触发重写，重写配置：

```
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
```

当aof文件大小是上次rewrite后大小的一倍且文件大于64m

- 优点

  

### rdb

将redis在内存中的数据定时dump到磁盘上。重启后可以从磁盘中恢复数据。

redis.conf中的默认配置：

```
# save <seconds时间，秒> <changes 更新操作次数>
save 900 1
save 300 10
save 60 10000
```

默认的配置为900秒内1次更新、300秒内10次更新、60秒内10000次更新则会保留一次快照

- 优点

  适合大规模的数据恢复

  如果要求的数据完整性A和一致性C要求不高，RDB是很好的选择

- 缺点

  数据的一致性不高，因为恢复的是上次的备份数据

  备份占用内存(redis备份时独立创建子进程，数据写入临时文件，此时内存占用是原来的两倍。在将临时文件替换掉原来的dump文件后内存恢复正常)

通过dump.rdb文件恢复数据时，将dump.rdb文件拷贝到安装目录bin目录下，重启redis服务即可。

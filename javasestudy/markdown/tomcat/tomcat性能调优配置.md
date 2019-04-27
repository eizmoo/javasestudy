# tomcat性能调优

## server.xml
### Connector标签
![connector](https://www.centos.bz/wp-content/uploads/2017/08/1-65.png)

- protocol
Connector在处理HTTP请求时，会使用不同的protocol。

不同的Tomcat版本支持的protocol不同，其中最典型的protocol包括BIO、NIO和APR
（Tomcat7中支持这3种，Tomcat8增加了对NIO2的支持，而到了Tomcat8.5和Tomcat9.0，则去掉了对BIO的支持）。

BIO是Blocking IO，顾名思义是阻塞的IO；NIO是Non-blocking IO，则是非阻塞的IO。
而APR是Apache Portable Runtime，是Apache可移植运行库，利用本地库可以实现高可扩展性、高性能。
Apr是在Tomcat上运行高并发应用的首选模式，但是需要安装apr、apr-utils、tomcat-native等包。

"HTTP/1.1" 使用的协议与tomcat版本有关
tomcat7中自动选取使用BIO或者APR（如果安装了APR使用ARP，否则BIO）
tomcat8中自动选取使用NIO或者APR（如果安装了APR使用APR，否则NIO）

- URIEncoding
设置tomcat字符集
- maxThreads
最大并发数，默认的最大请求数是150,即同时支持150并发。
- minSpareThreads
tomcat初始化时创建的线程数，默认25
- acceptCount
请求数量已满最大并发数时，后续的请求会进入等待队列， 这个参数是设置等待队列的长度
- maxKeepAliveRequests
在Tomcat里，默认长连接是打开的，当我们想关闭长连接时，只要将maxKeepAliveRequests设置为1就可以
maxKeepAliveRequests为-1时代表keep alive的数量无限制

> 参考 [详解tomcat连接数与线程数-CNBlog](https://www.cnblogs.com/kismetv/p/7806063.html)
如果想在某个Bean生成并装配完毕后执行自己的逻辑，可以什么方式实现？

答案BeanPostProcessor
BeanPostProcessor接口作用是：如果我们需要在Spring容器完成Bean的实例化、
配置和其他的初始化前后添加一些自己的逻辑处理，我们就可以定义一个或者多个BeanPostProcessor接口的实现，然后注册到容器中。

![初始化bean流程图](https://img-my.csdn.net/uploads/201210/22/1350888580_1225.jpg)
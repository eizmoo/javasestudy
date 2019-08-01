# AOP

### 原理

**为bean创建一个proxy，JDKproxy或者CGLIBproxy，然后在调用bean的方法时，会通过proxy来调用bean方法**

> 1. 通过AspectJAutoProxyBeanDefinitionParser类将AnnotationAwareAspectJAutoProxyCreator注册到Spring容器中
> 2. AnnotationAwareAspectJAutoProxyCreator类的postProcessAfterInitialization()方法将所有有advice的bean重新包装成proxy
> 3. 调用bean方法时通过proxy来调用，proxy依次调用增强器的相关方法，来实现方法切入


### dubbo 使用 nacos 作为注册中心

记录一下在使用的过程中踩的坑

### 问题1

使用maven仓库中的`com.alibaba.boot:nacos-config-spring-boot-starter:0.2.10`有bug

```log

    Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
    2022-01-21 14:42:08.787 ERROR 4403 --- [           main] o.s.boot.SpringApplication               : Application run failed
    
    org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'nacosConfigurationPropertiesBinder': Bean instantiation via constructor failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.alibaba.boot.nacos.config.binder.NacosBootConfigurationPropertiesBinder]: Constructor threw exception; nested exception is java.lang.NoClassDefFoundError: org/springframework/boot/context/properties/ConfigurationBeanFactoryMetadata
        at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:315) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:296) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1356) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1206) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:571) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:531) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:944) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:923) ~[spring-context-5.3.2.jar:5.3.2]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:588) ~[spring-context-5.3.2.jar:5.3.2]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:767) ~[spring-boot-2.4.1.jar:2.4.1]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:759) ~[spring-boot-2.4.1.jar:2.4.1]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:426) ~[spring-boot-2.4.1.jar:2.4.1]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:326) ~[spring-boot-2.4.1.jar:2.4.1]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1309) ~[spring-boot-2.4.1.jar:2.4.1]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1298) ~[spring-boot-2.4.1.jar:2.4.1]
        at com.realoldroot.test.provider.ProviderApplication.main(ProviderApplication.java:19) ~[main/:na]
    Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.alibaba.boot.nacos.config.binder.NacosBootConfigurationPropertiesBinder]: Constructor threw exception; nested exception is java.lang.NoClassDefFoundError: org/springframework/boot/context/properties/ConfigurationBeanFactoryMetadata
        at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:225) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:117) ~[spring-beans-5.3.2.jar:5.3.2]
        at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:311) ~[spring-beans-5.3.2.jar:5.3.2]
        ... 19 common frames omitted
    Caused by: java.lang.NoClassDefFoundError: org/springframework/boot/context/properties/ConfigurationBeanFactoryMetadata
        at com.alibaba.boot.nacos.config.binder.NacosBootConfigurationPropertiesBinder.<init>(NacosBootConfigurationPropertiesBinder.java:51) ~[nacos-config-spring-boot-autoconfigure-0.2.10.jar:0.2.10]
        at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:na]
        at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:na]
        at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:na]
        at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:490) ~[na:na]
        at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:212) ~[spring-beans-5.3.2.jar:5.3.2]
        ... 21 common frames omitted
    Caused by: java.lang.ClassNotFoundException: org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:581) ~[na:na]
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178) ~[na:na]
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:522) ~[na:na]
        ... 27 common frames omitted
```

其中`NacosBootConfigurationPropertiesBinder`类中使用的`ConfigurationBeanFactoryMetadata`在Spring5x中已经被删除了。
但是已经有热心的小伙伴修了这个问题 [#45](https://github.com/alibaba/spring-context-support/issues/45) [#194](https://github.com/nacos-group/nacos-spring-boot-project/pull/194)

但是官方还没有发布到maven仓库，所以如果要使用就需要自己把源码下载下来编译到本地maven仓库。

gradle配置里需要改一下依赖引用顺序，优先本地maven仓库，再从网上拉取。

```
    repositories {
        mavenLocal() // 放前面
        maven { url 'https://maven.aliyun.com/nexus/content/groups/public/' }
    }
```

### 问题2

`com.alibaba.boot:nacos-config-spring-boot-starter:0.2.11`与使用的dubbo2.7x版本不兼容。

```
Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2022-01-21 14:52:06.398 ERROR 4486 --- [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'helloController': Injection of @DubboReference dependencies is failed; nested exception is java.lang.ClassCastException: class java.lang.String cannot be cast to class java.lang.Class (java.lang.String and java.lang.Class are in module java.base of loader 'bootstrap')
	at com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor.postProcessPropertyValues(AbstractAnnotationBeanPostProcessor.java:183) ~[spring-context-support-1.0.11.jar:na]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1420) ~[spring-beans-5.3.2.jar:5.3.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:608) ~[spring-beans-5.3.2.jar:5.3.2]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:531) ~[spring-beans-5.3.2.jar:5.3.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[spring-beans-5.3.2.jar:5.3.2]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.3.2.jar:5.3.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[spring-beans-5.3.2.jar:5.3.2]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.2.jar:5.3.2]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:944) ~[spring-beans-5.3.2.jar:5.3.2]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:923) ~[spring-context-5.3.2.jar:5.3.2]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:588) ~[spring-context-5.3.2.jar:5.3.2]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:144) ~[spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:767) ~[spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:759) ~[spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:426) ~[spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:326) ~[spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1309) ~[spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1298) ~[spring-boot-2.4.1.jar:2.4.1]
	at com.realoldroot.test.consumer.ConsumerApplication.main(ConsumerApplication.java:15) ~[main/:na]
Caused by: java.lang.ClassCastException: class java.lang.String cannot be cast to class java.lang.Class (java.lang.String and java.lang.Class are in module java.base of loader 'bootstrap')
	at org.apache.dubbo.config.spring.util.DubboAnnotationUtils.resolveServiceInterfaceClass(DubboAnnotationUtils.java:95) ~[dubbo-2.7.12.jar:2.7.12]
	at org.apache.dubbo.config.spring.util.DubboAnnotationUtils.resolveInterfaceName(DubboAnnotationUtils.java:78) ~[dubbo-2.7.12.jar:2.7.12]
	at org.apache.dubbo.config.spring.beans.factory.annotation.ServiceBeanNameBuilder.<init>(ServiceBeanNameBuilder.java:65) ~[dubbo-2.7.12.jar:2.7.12]
	at org.apache.dubbo.config.spring.beans.factory.annotation.ServiceBeanNameBuilder.create(ServiceBeanNameBuilder.java:78) ~[dubbo-2.7.12.jar:2.7.12]
	at org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor.buildReferencedBeanName(ReferenceAnnotationBeanPostProcessor.java:358) ~[dubbo-2.7.12.jar:2.7.12]
	at org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor.buildInjectedObjectCacheKey(ReferenceAnnotationBeanPostProcessor.java:347) ~[dubbo-2.7.12.jar:2.7.12]
	at com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor.getInjectedObject(AbstractAnnotationBeanPostProcessor.java:404) ~[spring-context-support-1.0.11.jar:na]
	at com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor$AnnotatedFieldElement.inject(AbstractAnnotationBeanPostProcessor.java:626) ~[spring-context-support-1.0.11.jar:na]
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:119) ~[spring-beans-5.3.2.jar:5.3.2]
	at com.alibaba.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor.postProcessPropertyValues(AbstractAnnotationBeanPostProcessor.java:179) ~[spring-context-support-1.0.11.jar:na]
	... 18 common frames omitted
```

错误的地方在`DubboAnnotationUtils`类中`Class<?> interfaceClass = getAttribute(attributes, "interfaceClass");`
需要主动引入`1.0.10`版本才可以使用。[#7274](https://github.com/apache/dubbo/issues/7274)



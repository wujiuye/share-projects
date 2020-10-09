# Spring Cloud Kubernetes动态配置实现原理与源码分析

本篇内容：
* Spring Cloud Kubernetes动态配置的使用方式
* Spring Cloud Kubernetes动态配置实现原理
* Spring Cloud Kubernetes Config源码分析

## Spring Cloud Kubernetes动态配置的使用方式

关于如何使用动态配置以及监听配置改变，笔者在《Spring Cloud kubernetes入门项目sck-demo》这篇文章已经介绍了详细的使用步骤，
因此本篇只对使用做个总结，概括使用步骤。

* 1、与装载静态配置一样，编写Properties类；
例如编写用于装载数据库配置的DataBaseProperties类，然后在Properties类上添加@RefreshScope注解；
* 2、pom.xml文件中添加spring-cloud-kubernetes-config依赖；
* 3、在bootstrap.yaml文件中添加kubernetes-config的配置；
```yaml
spring:
  cloud:
    kubernetes:
      ## 开启自动拉取新配置功能，配置拉取周期为5秒，mode=polling是主动拉取方式
      reload:
        enabled: true
        mode: polling
        period: 5000
      config:
        ## 数组，引用的多个ConfigMap资源名称
        sources:
          - name: [ConfigMap资源名称]
```
* 4、在kubernetes控制台添加ConfigMap资源，在ConfigMap资源描述文件中添加配置；
* 5、监听Spring的事件，监听RefreshScopeRefreshedEvent事件或EnvironmentChangeEvent事件；

## Spring Cloud Kubernetes动态配置实现原理

与服务注册和发现一样，Spring Cloud Kubernetes实现动态刷新配置也是通过定时的调用Kubernetes API获取
ConfigMap资源的配置信息，即ConfigMap资源配置文件中的data项存储的信息。

再获取到配置信息后，将新获取的配置信息与当前正在使用的配置信息做比较，
如果对比发生了变化就调用Spring Cloud的动态配置API刷新配置，即调用ContextRefresher的refresh方法。

Spring Cloud在刷新配置之前会发送一个EnvironmentChangeEvent事件，在刷新配置之后，会发送一个RefreshScopeRefreshedEvent事件。

## Spring Cloud Kubernetes Config源码分析

Spring Cloud Kubernetes实现动态配置的源码再spring-cloud-kubernetes-config模块。
该模块也是一个starter包，在META-INF目录有个spring.factories文件。该文件配置
自动配置类ConfigReloadAutoConfiguration、引用配置类BootstrapConfiguration。

```text
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.cloud.kubernetes.config.reload.ConfigReloadAutoConfiguration

org.springframework.cloud.bootstrap.BootstrapConfiguration=\
org.springframework.cloud.kubernetes.config.BootstrapConfiguration
```

BootstrapConfiguration负责
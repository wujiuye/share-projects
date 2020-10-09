# Spring Cloud K8s Demo
* date: 2020/06/05 
* Team members: wujiuye

## 技术栈
* Docker、Kubernetes （容器技术）
* Spring Boot
* Spring Cloud Kubernetes （配置中心、注册中心（ConfigMap、ectd））
* Ribbon + OpenFeign （接口调用，不使用Ribbon的负载均衡，负载均衡由Kubernetes实现）
* Spring Cloud Gateway + Sentinel API网关+限流，实现在网关层限流 

## 目录或模块说明
* k8s                       存放k8s配置文件、配置中心文件（数据库等敏感数据放在配置中心配置）
* common-lib                组件库
* sck-demo-provider-api     服务提供者接口服务API
* sck-demo-provider         服务提供者
* sck-demo-consumer         服务消费者
* sck-demo-gateway-sentinel 服务网关

> 各模块下的Dockerfile为容易镜像配置文件、根目录下的.env被docker-compose为本地docker容易编排文件

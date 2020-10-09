# 构建部署相关命令

构建Jar包
```shell
mvn clean package -DskipTests
eval $(minikube docker-env)
```

构建镜像
```shell
docker build -t wujiuye/sck-demo-provider:v1.3.0 ./sck-demo-provider
docker build -t wujiuye/sck-demo-consumer:v1.3.0 ./sck-demo-consumer
```

更新容器镜像版本
```shell
kubectl set image deployment sck-demo-provider sck-demo-provider=wujiuye/sck-demo-provider:v1.3.0
kubectl set image deployment sck-demo-consumer sck-demo-consumer=wujiuye/sck-demo-consumer:v1.3.0
```

让服务可访问
```shell
minikube service sck-demo-consumer
minikube service sck-demo-provider
```

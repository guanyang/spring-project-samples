## spring-project-samples

### Getting Started
- [dubbo-sample](dubbo-sample/README.md): 基于dubbo3.2.6+springboot3.1.2+jdk17搭建示例
- [native-sample](native-sample/README.md): 基于spring native构建原生镜像示例
- [quarkus-sample](quarkus-sample/README.md): 基于quarkus构建原生镜像示例
- [virtualthread-sample](virtualthread-sample/README.md): 基于JDK21+springboot3搭建虚拟线程示例
- [webflux-sample](webflux-sample/README.md): 基于JDK21+spring webflux搭建响应式示例

### Documents
- [Spring-Native-vs-Quarkus](docs/Spring-Native-vs-Quarkus.md)
- [JDK21虚拟线程原理及实践](docs/JDK21虚拟线程原理及实践.md)
- [JDK21虚拟线程和webflux性能对决](docs/JDK21虚拟线程和webflux性能对决.md)
- [Spring Webflux使用subscribeOn和publishOn的最佳实践](docs/Spring%20Webflux使用subscribeOn和publishOn的最佳实践.md)

### 构建原生可执行文件
>示例工程：quarkus-sample，细节参考[构建quarkus原生可执行文件](https://cn.quarkus.io/guides/building-native-image)
- 方法一：如果本地已经安装`GraalVM`，则运行如下命令即可
```shell script
mvn clean package -DskipTests -Dnative
```
- 方法二：如果本地没有安装`GraalVM`，则运行如下命令，基于容器环境构建可执行文件，需要本地支持`docker`环境
```shell script
mvn clean package -DskipTests -Dnative -Dquarkus.native.container-build=true
```
- 以上命令将生成可执行文件: `./target/quarkus-sample-0.0.1-SNAPSHOT-runner`，直接运行即可。

### 构建容器镜像
- 方法一：使用容器镜像扩展，一个命令即可创建一个容器镜像，细节参考[容器镜像指南](https://cn.quarkus.io/guides/container-image)
```shell script
mvn clean package -Dnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true
```

- 方法二：手动使用微基础镜像，细节参考[运行时基础镜像](https://cn.quarkus.io/guides/quarkus-runtime-base-image)
>如果你没有删除生成的原生可执行文件，可以用以下方法构建docker镜像：
```shell script
docker build -f src/main/docker/Dockerfile.native-micro -t guanyangsunlight/spring-project-samples:quarkus-sample-native-0.0.1 .
```

### 基于docker-compose运行quarkus-sample示例
```shell
docker-compose -p docker-quarkus-sample -f src/main/docker/docker-compose.yml up -d
```

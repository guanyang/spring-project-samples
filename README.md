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

### K6压测
#### K6及dashboard安装
- k6安装，参考文档：https://k6.io/docs/get-started/installation/
- k6 dashboard安装，参考文档：https://github.com/grafana/xk6-dashboard

```text
1.Download xk6:
$> go install go.k6.io/xk6/cmd/xk6@latest

2.Build the binary:
$> xk6 build --with github.com/grafana/xk6-dashboard@latest

3.Usage:
$> ./k6 run --out dashboard script.js

          /\      |‾‾| /‾‾/   /‾‾/   
     /\  /  \     |  |/  /   /  /    
    /  \/    \    |     (   /   ‾‾\  
   /          \   |  |\  \ |  (‾)  | 
  / __________ \  |__| \__\ \_____/ .io

  execution: local
     script: script.js
     output: dashboard (:5665) http://127.0.0.1:5665
```

#### 压测脚本
- 总请求时长60s，并发从200开始，命令如下：

```text
k6 run --out dashboard=open -u 200 --duration 60s -e url=http://127.0.0.1:8081/hello/100 simple-test.js

-i：指定请求数量
-u：模拟并发数量
--duration：请求时长定义，例如：60s，1m
-e url：指定环境变量url，用于实际场景替换
--out: 指定结果输出，例如：dashboard=open表示指定到k6 dashboard，并自动打开浏览器http://127.0.0.1:5665
```

#### `simple-test.js`脚本说明

```javascript
import http from 'k6/http';
import { check } from 'k6';

export const options = {
    //配置阈值判断，不满足阈值的作为错误结果
    // thresholds: {
    //     http_req_failed: ['rate<0.01'], // http errors should be less than 1%
    //     http_req_duration: ['p(95)<200'], // 95% of requests should be below 200ms
    // },
    //配置压测阶段，可以定义多个阶段进行
    // stages: [
    //     { duration: '1m', target: 200 },
    //     { duration: '1m', target: 400 },
    //     { duration: '1m', target: 600 },
    //     { duration: '1m', target: 800 },
    //     { duration: '1m', target: 1000 },
    // ],
};

export default function () {
  const res = http.get(`${__ENV.url}`);
  check(res, {
    'is status 200': (r) => r.status === 200
  });
}
```

### 构建原生可执行文件

>示例工程：quarkus-sample，细节参考[构建quarkus原生可执行文件](https://cn.quarkus.io/guides/building-native-image)

- 方法一：如果本地已经安装`GraalVM`，则运行如下命令即可

```shell
mvn clean package -DskipTests -Dnative
```
- 方法二：如果本地没有安装`GraalVM`，则运行如下命令，基于容器环境构建可执行文件，需要本地支持`docker`环境

```shell
mvn clean package -DskipTests -Dnative -Dquarkus.native.container-build=true
```
- 以上命令将生成可执行文件: `./target/quarkus-sample-0.0.1-SNAPSHOT-runner`，直接运行即可。

### 构建容器镜像
- 方法一：使用容器镜像扩展，一个命令即可创建一个容器镜像，细节参考[容器镜像指南](https://cn.quarkus.io/guides/container-image)

```shell
mvn clean package -Dnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true
```

- 方法二：手动使用微基础镜像，细节参考[运行时基础镜像](https://cn.quarkus.io/guides/quarkus-runtime-base-image)

>如果你没有删除生成的原生可执行文件，可以用以下方法构建docker镜像：

```shell
docker build -f src/main/docker/Dockerfile.native-micro -t guanyangsunlight/spring-project-samples:quarkus-sample-native-0.0.1 .
```

### 基于docker-compose运行quarkus-sample示例

```shell
docker-compose -p docker-quarkus-sample -f src/main/docker/docker-compose.yml up -d
```

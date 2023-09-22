### 概述
- Quarkus首页放出的标语：超音速亚原子的Java（Supersonic Subatomic Java），它是为`OpenJDK HotSpot`和`GraalVM`量身定制的`Kubernetes Native Java`框架，基于同类最佳的 Java 库和标准制作而成。
- Quarkus的到来为开发Linux容器和 kubernetes 原生Java微服务带来了一个创新平台。
- Spring Boot是一个基于Java的框架，专注于企业应用，它可以简单使用所有Spring项目，并集成了许多开箱即用的功能，来帮助开发人员提高生产力。
- Spring Boot由于其约定优于配置方法，它根据依赖项自动注册默认配置，大大缩短了Java应用程序的开发周期。
- Java生态中新兴的技术体系
  - Spring Reactive(Spring WebFlux) → 背靠 Pivotal → 归属 VMware → 归属戴尔
  - Quarkus 和 Vert.x → 背靠 Eclipse 基金会 → 主要由 Red Hat 支持
  - Helidon → 背靠 Oracle
  - Micronaut → 背靠 Object Computing（Grails、OpenDDS）
  - Lagom → 背靠 Lightbend（Akka）
- 本文重点关注比较热门的`Spring Reactive`和`Quarkus`在原生镜像方面的差异。

### 对比分析
- **创新和生态系统**： Spring 有着长期的历史和丰富的生态系统，许多开发者对其已经非常熟悉。Spring Native 是 Spring 团队为了更好地适应云原生环境（如 Kubernetes）而推出的新项目。相比之下，Quarkus 较新，但它在设计上就考虑了现代云原生和微服务架构，因此在某些方面可能更具创新性。
- **性能和资源利用**： Quarkus 和 Spring Native 都声称可以提供更快的启动时间和减少的内存占用。然而，实际表现可能会因应用程序的具体情况以及你如何使用这些框架而变化。
- **开发体验**： Quarkus 提供的开发模式（live coding）允许开发者在不重启应用的情况下实时看到代码改变的效果，这可能会提高开发效率。Spring Native 则继承了 Spring Boot 的开发体验，使得许多开发者能够很快上手。
- **兼容性**： Spring Native 对于 Spring 生态中的部分库可能还存在兼容性问题，需要按照官方给出的指引进行调整。而 Quarkus 在设计时就尽可能考虑了广泛的兼容性，包括对于 Hibernate，Apache Camel，Eclipse MicroProfile 等开源库的支持。

### 启动&构建指标对比

| 指标项         | Spring Boot Native | Quarkus Native | Spring Boot JVM | Quarkus JVM |
|-------------|--------------------|----------------|-----------------|-------------|
| 启动耗时（秒）     | 0.244              | 0.104          | 6.156           | 2.230       |
| 启动内存（MB）    | 44.31              | 9.05           | 237.1           | 119.1       |
| 启动CPU使用率（%） | -                  | -              | 0.22            | 0.26        |
| 构建镜像大小（MB）  | 119.89             | 84.75          | 217.92          | 421.24      |
| 构建时长（秒）     | 592                | 444            | 90              | 17.938      |

### 性能测试

#### 资源版本
- Mysql: 8.0.32-1.el8
- Quarkus: 3.2.2.Final
- Spring Boot: 3.1.2
- JDK: 17.0.8
- Docker Engine: 24.0.5
- Docker Resource: 4C/8G

#### 压测源码&镜像
- 压测源码：https://github.com/guanyang/spring-project-samples
    - [native-sample](https://github.com/guanyang/spring-project-samples/tree/main/native-sample): 基于spring native构建原生镜像示例
    - [quarkus-sample](https://github.com/guanyang/spring-project-samples/tree/main/quarkus-sample): 基于quarkus构建原生镜像示例
- 镜像资源
    - Quarkus Native Image: guanyangsunlight/spring-project-samples:quarkus-sample-0.0.1-SNAPSHOT
    - Quarkus JVM Image: guanyangsunlight/spring-project-samples:quarkus-sample-0.0.1-SNAPSHOT-jvm
    - Spring Boot Native Image: guanyangsunlight/spring-project-samples:native-sample-0.0.1-SNAPSHOT
    - Spring Boot JVM Image: guanyangsunlight/spring-project-samples:native-sample-0.0.1-SNAPSHOT-jvm
    - MySQL Image: guanyangsunlight/spring-project-samples:sample-mysql-8.0.32
- Docker Compose文件
    - [quarkus-sample](https://github.com/guanyang/spring-project-samples/blob/main/quarkus-sample/src/main/docker/docker-compose.yml)
    - [native-sample](https://github.com/guanyang/spring-project-samples/blob/main/native-sample/src/main/docker/docker-compose.yml)

#### 压测架构
```mermaid
graph LR
A(K6施压机) --> B(应用容器)
B -->C(MySQL容器)
```
- 应用容器：`Spring Boot Native`,`Quarkus Native`,`Spring Boot JVM`,`Quarkus JVM`每个一个实例
- MySQL容器：Mysql8.0容器实例一个
- K6施压机: 2.2 GHz 四核Intel Core i7,16 GB 1600 MHz DDR3
- K6参考链接：[https://k6.io/docs/](https://k6.io/docs/)

#### 压测场景case
- Spring Boot Native: 性能指标（QPS、RT）,机器指标（CPU、内存）
- Quarkus Native: 性能指标（QPS、RT）,机器指标（CPU、内存）
- Spring Boot JVM: 性能指标（QPS、RT）,机器指标（CPU、内存）
- Quarkus JVM: 性能指标（QPS、RT）,机器指标（CPU、内存）

#### 压测服务接口
- 接口地址：${host}/api/test/get/{id}
- 接口说明：根据id查询数据库记录，host为服务地址，id为数据库记录主键
- 响应示例：
```
{
    "code": 200,
    "message": "OK",
    "data": {
        "id": 4,
        "version": 1,
        "deleted": 0,
        "createBy": "admin",
        "updateBy": "admin",
        "createTime": 1695312514000,
        "updateTime": 1695312532000,
        "username": "test41"
    }
}
```

#### 压测脚本
- 总请求时长300s，并发从50开始，并按照50步长增长，命令如下：
```
k6 run  -u 50 --duration 300s -e url=http://127.0.0.1:8082/api/test/get/4 simple-test.js

-i：指定请求数量
-u：模拟并发数量
--duration：请求时长定义，例如：60s，1m
-e url：指定环境变量url，用于实际场景替换
```
- 脚本输出样例
```
  scenarios: (100.00%) 1 scenario, 50 max VUs, 36s max duration (incl. graceful stop):
           * default: 50 looping VUs for 6s (gracefulStop: 30s)


     ✓ is status 200

     checks.........................: 100.00% ✓ 7761        ✗ 0   
     data_received..................: 1.9 MB  324 kB/s
     data_sent......................: 730 kB  121 kB/s
     http_req_blocked...............: avg=12.22µs min=1µs    med=3µs     max=3.26ms   p(90)=5µs     p(95)=6µs    
     http_req_connecting............: avg=7.1µs   min=0s     med=0s      max=2.19ms   p(90)=0s      p(95)=0s     
     http_req_duration..............: avg=38.56ms min=7.55ms med=34.26ms max=216.77ms p(90)=58.96ms p(95)=68.51ms
       { expected_response:true }...: avg=38.56ms min=7.55ms med=34.26ms max=216.77ms p(90)=58.96ms p(95)=68.51ms
     http_req_failed................: 0.00%   ✓ 0           ✗ 7761
     http_req_receiving.............: avg=52.57µs min=19µs   med=46µs    max=680µs    p(90)=81µs    p(95)=97µs   
     http_req_sending...............: avg=19.87µs min=7µs    med=16µs    max=1.27ms   p(90)=27µs    p(95)=38µs   
     http_req_tls_handshaking.......: avg=0s      min=0s     med=0s      max=0s       p(90)=0s      p(95)=0s     
     http_req_waiting...............: avg=38.49ms min=7.51ms med=34.18ms max=216.58ms p(90)=58.89ms p(95)=68.46ms
     http_reqs......................: 7761    1288.780058/s
     iteration_duration.............: avg=38.7ms  min=7.76ms med=34.4ms  max=218.51ms p(90)=59.08ms p(95)=68.64ms
     iterations.....................: 7761    1288.780058/s
     vus............................: 50      min=50        max=50
     vus_max........................: 50      min=50        max=50
```
- `simple-test.js`脚本说明
```
import http from 'k6/http';
import { check } from 'k6';

export default function () {
  const res = http.get(`${__ENV.url}`);
  check(res, {
    'is status 200': (r) => r.status === 200
  });
}
```

#### 压测指标
##### 被压机器指标
- CPU usage
- Memory usage

##### 被压机器性能指标
- QPS
- Avg Latency(ms)
- P95(ms)

#### 压测结果

| Case                      | QPS           | Avg Latency | P95      | CPU usage | Memory usage |
|---------------------------|---------------|-------------|----------|-----------|--------------|
| Quarkus Native,-u 50      | 1699.067212/s | 29.41ms     | 50.07ms  | 136.91%   | 23.15MB      |
| Quarkus Native,-u 100     | 1749.35664/s  | 57.14ms     | 89.9ms   | 148.9%    | 23.95MB      |
| Quarkus Native,-u 200     | 1765.832527/s | 113.23ms    | 167.43ms | 156.17%   | 25.5MB       |
| Spring Boot Native,-u 50  | 1114.946899/s | 44.82ms     | 85.11ms  | 226.5%    | 86.23MB      |
| Spring Boot Native,-u 100 | 1175.220721/s | 85.06ms     | 137.26ms | 236.07%   | 96.16MB      |
| Spring Boot Native,-u 200 | 1094.461679/s | 182.67ms    | 284.98ms | 246.32%   | 143.3MB      |
| Quarkus JVM,-u 50         | 2149.507697/s | 23.24ms     | 39ms     | 111.99%   | 173.6MB      |
| Quarkus JVM,-u 100        | 2186.80721/s  | 45.7m       | 69.6ms   | 126.62%   | 174.2MB      |
| Quarkus JVM,-u 200        | 2211.63056/s  | 90.38ms     | 130.41ms | 125.45%   | 180.1MB      |
| Spring Boot JVM,-u 50     | 1472.76853/s  | 33.93ms     | 65.69ms  | 199.08%   | 426.8MB      |
| Spring Boot JVM,-u 100    | 1624.997761/s | 61.51ms     | 97.37ms  | 211.01%   | 427.2MB      |
| Spring Boot JVM,-u 200    | 1611.01646/s  | 124.05ms    | 188.24ms | 214.96%   | 428.1MB      |

#### 压测总结
- Quarkus Native相较于Spring Boot Native资源消耗更低，性能更好。
- Quarkus JVM相较于Spring Boot JVM模式资源消耗更低，性能更好。
- Native原生相较于JVM模式整体资源消耗更低，启动更快，构建镜像更小。

### 优缺点及选型总结
#### Quarkus优缺点
- 优点
    - 高度优化的运行时性能和内存管理；
    - 对于开发者友好的开发模式，如实时编程（live coding）；
    - 广泛的兼容性，支持许多主流的 Java 开源库；
    - 面向云原生应用的设计。
- 缺点
    - 相对于 Spring，其社区规模还较小，可能在某些问题上找不到即时的帮助；
    - 尽管有广泛的兼容性，但并不包括所有的 Java 库。

#### Spring Boot Native优缺点
- 优点
    - 建立在 Spring 生态系统之上，易于为已经使用 Spring 的团队接受；
    - 与 Spring Boot 无缝集成，并且借助 GraalVM 提供媲美本机语言的启动速度和内存占用。
    - 支持 AOT（Ahead-of-Time Compilation）编译，可以提高启动速度。
- 缺点
    - 使用 Spring Boot Native 需要适应其构建过程中将应用程序转化为本地应用程序的复杂性；
    - 构建完成的应用程序虽然启动速度快、内存消耗低，但CPU使用率高于普通 JVM 程序。

#### 技术选型思考
- **项目需求**：如果项目需要快速启动，低内存消耗，那么两者都可以满足。如果已经在使用 Spring Stack，并且想要继续保持使用它，那么 Spring Boot Native 会更加合适。如果项目对开发效率有高要求，那么 Quarkus 可能更符合需求。

- **团队技能**：如果团队成员已经非常熟悉 Spring Stack，那么采用 Spring Boot Native 可能可以减少学习曲线。反之，如果团队愿意尝试新的技术，并且对响应式编程和函数式编程有兴趣，那么选择 Quarkus 可以是一个不错的选择。

- **社区支持和文档**：Spring 社区非常活跃，有大量的教程和指南。虽然 Quarkus 比较新，但也在积极扩大其社区，并提供了详细的文档。

>在实际决策过程中，最好能够根据具体情况进行技术选型，可能的话，可以在小规模的项目或者原型中尝试并评估这些框架。

### Quarkus对Spring开发者的额外好处
- **功能即服务 (FaaS)**：当编译为原生二进制文件时，Quarkus 应用程序可以在 0.0015 秒内启动，从而可以将现有的 Spring 和 Java API 知识与 FaaS 功能结合使用。（[Azure](https://quarkus.io/guides/azure-functions),[AWS Lambda](https://quarkus.io/guides/aws-lambda)）
- **实时编码**：从“Hello World”示例应用程序开始，然后将其转换为复杂的微服务，而无需重新启动应用程序。只需保存并重新加载浏览器即可查看沿途的变化。 Quarkus 实时编码“开箱即用”，与 IDE 无关。
- **支持反应式和命令式模型**：Quarkus有一个反应式核心，支持传统的命令式模型、反应式模型，或在同一应用程序中同时支持两者。
- **早期检测依赖注入错误**：Quarkus 在编译期间而不是在运行时捕获依赖项注入错误。
- **最佳框架和标准的结合**：Quarkus 在同一应用程序中支持 Spring API 兼容性、Eclipse Vert.x、MicroProfile（JAX-RS、CDI 等）、反应式流和消息传递等。参考《[Autowire MicroProfile into Spring Boot](https://developers.redhat.com/blog/2019/10/02/autowire-microprofile-into-spring-with-quarkus)》，可以在一个项目中同时使用 Spring 和 MicroProfile API。

### 参考文档
- [【Baeldung】Spring Boot vs Quarkus](https://www.baeldung.com/spring-boot-vs-quarkus)
- [【LogicMonitor】Quarkus vs Spring Boot](https://www.logicmonitor.com/blog/quarkus-vs-spring#h-about-logicmonitor)
- [Quarkus for Spring Developers](https://quarkus.io/blog/quarkus-for-spring-developers/)
- [Quarkus官方文档](https://quarkus.io/guides/)
- [Quarkus入门指南](https://quarkus.io/get-started/)
- [Quarkus官方代码生成](https://code.quarkus.io/)
- [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/#build-image)
- [GraalVM Native Image Support](https://docs.spring.io/spring-boot/docs/3.1.2/reference/html/native-image.html#native-image)
- [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
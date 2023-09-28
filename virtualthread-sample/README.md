## virtualthread-sample

### Getting Started
- 在`SpringBoot`中使用虚拟线程处理请求
```java
@EnableAsync
@Configuration
@ConditionalOnProperty(value = "spring.executor", havingValue = "virtual")
public class ThreadConfig {

    //为每个异步任务提供虚拟线程执行Executor
    @Bean
    public AsyncTaskExecutor applicationTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    //为tomcat提供虚拟线程执行Executor
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }

}
```
- 在`application.yml`中添加配置来启用虚拟线程
```yml
spring:
  #配置virtual表示启用虚拟线程，非virtual表示不启用，可以通过环境变量SPRING_EXECUTOR指定
  executor: ${SPRING_EXECUTOR:virtual}
```

- 添加测试入口进行虚拟线程测试
```java
@RestController
@SpringBootApplication
public class VirtualthreadSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualthreadSampleApplication.class, args);
    }

    @GetMapping("/hello/{timeMillis}")
    public Object hello2(@PathVariable long timeMillis) throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello World!");
        //查看当时线程信息，识别是否是虚拟线程
        map.put("thread", Thread.currentThread().toString());
        //模拟耗时IO操作
        Thread.sleep(timeMillis);
        return map;
    }

}
```

### K6压测

#### 压测脚本
- 总请求时长60s，并发从200开始，并按照200步长增长，命令如下：
```
k6 run -u 200 --duration 60s -e url=http://127.0.0.1:8081/hello/100 simple-test.js

-i：指定请求数量
-u：模拟并发数量
--duration：请求时长定义，例如：60s，1m
-e url：指定环境变量url，用于实际场景替换
```
#### `simple-test.js`脚本说明
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

#### 压测docker实例
```shell
## 启用虚拟线程实例
docker run --name virtualthread-sample-vt -p 8081:8080 -e SPRING_EXECUTOR=virtual -d guanyangsunlight/spring-project-samples:virtualthread-sample-0.0.1-SNAPSHOT

## 启用普通线程实例
docker run --name virtualthread-sample -p 8082:8080 -e SPRING_EXECUTOR=none -d guanyangsunlight/spring-project-samples:virtualthread-sample-0.0.1-SNAPSHOT
```

#### K6压测结果

| Case                    | QPS           | Avg Latency | P95      |
|-------------------------|---------------|-------------|----------|
| Spring Boot虚拟线程,-u 200  | 1620.869685/s | 123.09ms    | 149.42ms |
| Spring Boot虚拟线程,-u 400  | 2202.121674/s | 180.84ms    | 277.14ms |
| Spring Boot虚拟线程,-u 600  | 3195.845398/s | 186.44ms    | 256.03ms |
| Spring Boot虚拟线程,-u 800  | 3780.654388/s | 210.28ms    | 294.79ms |
| Spring Boot虚拟线程,-u 1000 | 4250.384928/s | 234.17ms    | 319.92ms |
| Spring Boot虚拟线程,-u 1200 | 4479.450088/s | 266.15ms    | 370.17ms |
| Spring Boot普通线程,-u 200  | 1418.709029/s | 140.69ms    | 218.24ms |
| Spring Boot普通线程,-u 400  | 1888.860872/s | 210.91ms    | 247.39ms |
| Spring Boot普通线程,-u 600  | 1889.607486/s | 315.49ms    | 373.9ms  |
| Spring Boot普通线程,-u 800  | 1954.985051/s | 405.99ms    | 428.44ms |
| Spring Boot普通线程,-u 1000 | 1917.568269/s | 516.33ms    | 585.76ms |

#### K6压测总结
>以上实例都是在jvm默认参数及tomcat线程池默认200大小场景下进行，没有进行任何调优配置
- 采用虚拟线程模式，随着并发数的提高，性能提升比较明显，整体性能明显优于普通线程模式。
- 采用普通线程模式，由于tomcat默认线程池配置，增加并发数并不能明显提升QPS，由于阻塞等待导致耗时边长。
- 虚拟线程在执行到`IO`操作或`Blocking`操作时，会自动切换到其他虚拟线程执行，从而避免当前线程等待，可以高效通过少数线程去调度大量虚拟线程，最大化提升线程的执行效率。

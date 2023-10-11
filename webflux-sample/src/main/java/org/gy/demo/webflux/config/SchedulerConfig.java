package org.gy.demo.webflux.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Configuration
@EnableConfigurationProperties(SchedulerProperties.class)
public class SchedulerConfig {

    @Bean("virtualThreadScheduler")
    public Scheduler customScheduler(SchedulerProperties properties) {
        //定义虚拟线程调度器大小，保持跟平台线程池大小一样
        System.setProperty("jdk.virtualThreadScheduler.maxPoolSize", String.valueOf(properties.getThreadCap()));
        return Schedulers.fromExecutorService(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean("platformThreadScheduler")
    public Scheduler platformThreadScheduler(SchedulerProperties properties) {
        return Schedulers.newBoundedElastic(properties.getThreadCap(), properties.getQueuedTaskCap(), "boundedElastic", properties.getTtlSeconds());
    }

}

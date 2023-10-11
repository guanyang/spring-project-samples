package org.gy.demo.webflux.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.scheduler", ignoreUnknownFields = true)
public class SchedulerProperties {

    private int threadCap = 200;

    private int queuedTaskCap = 20000;

    private int ttlSeconds = 60;
}

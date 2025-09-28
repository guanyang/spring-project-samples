package org.gy.demo.mq.event;

import io.github.guanyang.mq.annotation.EnableMQ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author guanyang
 */
@EnableMQ
@SpringBootApplication
public class MqSampleEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqSampleEventApplication.class, args);
    }

}

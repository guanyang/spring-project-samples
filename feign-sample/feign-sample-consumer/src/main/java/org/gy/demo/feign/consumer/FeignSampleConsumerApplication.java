package org.gy.demo.feign.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author gy
 */
@EnableFeignClients(basePackages="org.gy.demo.feign.api")
@SpringBootApplication
public class FeignSampleConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignSampleConsumerApplication.class, args);
    }

}

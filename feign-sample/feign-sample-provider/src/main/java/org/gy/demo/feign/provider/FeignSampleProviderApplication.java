package org.gy.demo.feign.provider;

import io.github.guanyang.xss.annotation.EnableXss;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gy
 */
@EnableXss
@SpringBootApplication
public class FeignSampleProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignSampleProviderApplication.class, args);
    }

}

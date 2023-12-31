package org.gy.demo.nativesample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "org.gy.demo.nativesample.mapper")
public class NativeSampleApplication {


    @GetMapping("/hello")
    public Object hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello World!");
        return map;
    }

    public static void main(String[] args) {
        SpringApplication.run(NativeSampleApplication.class, args);
    }

}

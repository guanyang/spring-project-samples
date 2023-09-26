package org.gy.demo.virtualthread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class VirtualthreadSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualthreadSampleApplication.class, args);
    }


    @GetMapping("/hello")
    public Object hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello World!");
        map.put("thread", Thread.currentThread().toString());
        return map;
    }

}

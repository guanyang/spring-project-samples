package org.gy.demo.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@SpringBootApplication
public class WebfluxSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxSampleApplication.class, args);
    }

    @GetMapping("/hello")
    public Mono<Object> hello1() {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello World!");
        map.put("thread", Thread.currentThread().toString());
        return Mono.just(map);
    }

    @GetMapping("/hello/{timeMillis}")
    public Mono<Object> hello2(@PathVariable long timeMillis) {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello World!");
        map.put("thread", Thread.currentThread().toString());
        return Mono.delay(Duration.ofMillis(timeMillis)).thenReturn(map);
    }

}

package org.gy.demo.webflux.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.gy.demo.webflux.service.HelloWorldService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class HelloWorldController {

    @Resource
    private HelloWorldService service;

    @Resource(name = "virtualThreadScheduler")
    private Scheduler virtualThreadScheduler;

    @Resource(name = "platformThreadScheduler")
    private Scheduler platformThreadScheduler;

    @GetMapping("/hello")
    public Mono<Object> hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello World!");
        map.put("thread", Thread.currentThread().toString());
        return Mono.just(map);
    }

    @GetMapping("/hello/{timeMillis}")
    public Mono<Object> hello(@PathVariable long timeMillis) {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello World!");
        map.put("thread", Thread.currentThread().toString());
        return Mono.delay(Duration.ofMillis(timeMillis)).thenReturn(map);
    }

    @GetMapping("/hello1/{timeMillis}")
    public Mono<Object> hello1(@PathVariable long timeMillis) {
        return Mono.just(timeMillis).publishOn(virtualThreadScheduler).map(service::hello);
    }

    @GetMapping("/hello2/{timeMillis}")
    public Mono<Object> hello2(@PathVariable long timeMillis) {
        return Mono.just(timeMillis).publishOn(platformThreadScheduler).map(service::hello);
    }

    @GetMapping("/hello3/{timeMillis}")
    public Object hello3(@PathVariable long timeMillis) {
        return service.hello(timeMillis);
    }
}

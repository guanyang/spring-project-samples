package org.gy.demo.webflux.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.gy.demo.webflux.service.HelloWorldService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

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
        return Mono.fromSupplier(service::hello);
    }

    @GetMapping("/hello/{timeMillis}")
    public Mono<Object> hello(@PathVariable long timeMillis) {
        return Mono.delay(Duration.ofMillis(timeMillis)).thenReturn(service.hello());
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

    @GetMapping("/hello4/{timeMillis}")
    public Mono<Object> hello4(@PathVariable long timeMillis) {
        return Mono.delay(Duration.ofMillis(timeMillis)).subscribeOn(virtualThreadScheduler).thenReturn(service.hello());
    }

    @GetMapping("/hello5/{timeMillis}")
    public Mono<Object> hello5(@PathVariable long timeMillis) {
        return Mono.delay(Duration.ofMillis(timeMillis)).subscribeOn(platformThreadScheduler).thenReturn(service.hello());
    }
}

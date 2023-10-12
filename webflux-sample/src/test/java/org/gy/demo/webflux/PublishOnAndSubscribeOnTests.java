package org.gy.demo.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class PublishOnAndSubscribeOnTests {

    @Test
    public void testPublishOnAndSubscribeOn() {
        Flux.just("Hello").map(s -> {
            System.out.println("[1] Thread name: " + Thread.currentThread().getName());
            return s.concat(" World");
        }).publishOn(Schedulers.newParallel("thread-publishOn")).map(s -> {
            System.out.println("[2] Thread name: " + Thread.currentThread().getName());
            return s;
        }).subscribeOn(Schedulers.newSingle("thread-subscribeOn")).subscribe(s -> {
            System.out.println("[3] Thread name: " + Thread.currentThread().getName());
            System.out.println(s);
        });
    }
}

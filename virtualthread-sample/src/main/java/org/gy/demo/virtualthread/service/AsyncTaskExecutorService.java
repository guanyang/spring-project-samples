package org.gy.demo.virtualthread.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncTaskExecutorService {

    @Async
    public void run() {
        log.info("Async task method has been called {}", Thread.currentThread());
    }
}

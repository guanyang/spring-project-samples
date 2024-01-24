package org.gy.demo.virtualthread.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SchedulerService {

    @Scheduled(fixedDelayString = "15000")
    public void run() {
        log.info("Scheduled method has been called {}", Thread.currentThread());
    }
}

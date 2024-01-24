package org.gy.demo.virtualthread;

import jakarta.annotation.Resource;
import org.gy.demo.virtualthread.service.AsyncTaskExecutorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@EnableAsync
@EnableScheduling
@RestController
@SpringBootApplication
public class VirtualthreadSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualthreadSampleApplication.class, args);
    }


    @GetMapping("/hello")
    public Object hello1() {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello1 World!");
        map.put("thread", Thread.currentThread().toString());
        return map;
    }

    @GetMapping("/hello/{timeMillis}")
    public Object hello2(@PathVariable long timeMillis) throws InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello2 World!");
        map.put("thread", Thread.currentThread().toString());
        Thread.sleep(timeMillis);
        return map;
    }


    @Resource
    private AsyncTaskExecutorService asyncTaskExecutorService;

    @GetMapping("/hello/async")
    public Object hello3() throws InterruptedException {
        //异步调用
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello3 World!");
        map.put("thread", Thread.currentThread().toString());
        asyncTaskExecutorService.run();
        return map;
    }

}

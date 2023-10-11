package org.gy.demo.webflux.service;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HelloWorldService {

    /**
     * 模拟耗时操作
     *
     * @param timeMillis 模拟时长，单位：毫秒
     * @return Map
     */
    @SneakyThrows
    public Map<String, Object> hello(long timeMillis) {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello World!");
        map.put("thread", Thread.currentThread().toString());
        map.put("timeMillis", timeMillis);
        //模拟耗时操作
        if (timeMillis > 0) {
            Thread.sleep(timeMillis);
        }
        return map;
    }
}

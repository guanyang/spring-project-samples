package org.gy.demo.feign.provider.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 测试入口
 *
 * @author gy
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping(value = {"/api/test/v1", "/api/private/v1", "/api/auth/v1", "/api/{type}/v1"})
public class TestController {

    private static final Map<Integer, String> CACHE_DATA = new ConcurrentHashMap<>();

    @RequestMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("error", 0);
        result.put("msg", "ok");
        result.put("data", System.currentTimeMillis());
        return result;
    }


    @RequestMapping("/hello/{times}")
    public Map<String, Object> hello1(@PathVariable int times) {
        Map<String, Object> result = new HashMap<>();
        result.put("error", 0);
        result.put("msg", "ok");
        result.put("data", System.currentTimeMillis());
        try {
            //模拟耗时
            Thread.sleep(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/hello/data/{size}")
    public Map<String, Object> hello2(@PathVariable int size) {
        Map<String, Object> result = new HashMap<>();
        result.put("error", 0);
        result.put("msg", "ok");
        int charSzie = size * 1024;
        String data = CACHE_DATA.computeIfAbsent(charSzie, k -> createDataSize(k));
        result.put("data", data);
        return result;
    }

    @RequestMapping("/hello/data/{size}/{times}")
    public Map<String, Object> hello3(@PathVariable int size, @PathVariable int times) {
        Map<String, Object> result = new HashMap<>();
        result.put("error", 0);
        result.put("msg", "ok");
        int charSzie = size * 1024;
        String data = CACHE_DATA.computeIfAbsent(charSzie, k -> createDataSize(k));
        result.put("data", data);
        try {
            Thread.sleep(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/hello/data/{size}/{times}/{headerKey}")
    public Map<String, Object> hello3(@PathVariable int size, @PathVariable int times, @PathVariable String headerKey,
                                      HttpServletRequest request) {
        String header = request.getHeader(headerKey);
        Map<String, Object> result = new HashMap<>();
        result.put("error", 0);
        result.put("msg", "ok");
        result.put("header", header);
        int charSzie = size * 1024;
        String data = CACHE_DATA.computeIfAbsent(charSzie, k -> createDataSize(k));
        result.put("data", data);
        try {
            Thread.sleep(times);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static String createDataSize(int msgSize) {
        return "a".repeat(Math.max(0, msgSize));
    }

}

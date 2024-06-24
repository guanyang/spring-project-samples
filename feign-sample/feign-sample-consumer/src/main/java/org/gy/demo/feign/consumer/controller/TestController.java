package org.gy.demo.feign.consumer.controller;

import jakarta.annotation.Resource;
import org.gy.demo.feign.api.dto.TestRequest;
import org.gy.demo.feign.api.dto.TestResponse;
import org.gy.demo.feign.api.dto.support.Result;
import org.gy.demo.feign.api.service.SampleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gy
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private SampleService sampleService;

    @GetMapping("/hello")
    public Result<TestResponse> hello(@Validated TestRequest request) {
        return sampleService.hello(request);
    }

}

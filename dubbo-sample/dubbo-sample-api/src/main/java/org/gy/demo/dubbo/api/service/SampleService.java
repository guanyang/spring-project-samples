package org.gy.demo.dubbo.api.service;

import org.gy.demo.dubbo.api.dto.TestRequest;
import org.gy.demo.dubbo.api.dto.TestResponse;
import org.gy.demo.dubbo.api.dto.support.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 */
@RequestMapping("/api/test")
public interface SampleService {

    @GetMapping("/hello")
    Result<TestResponse> hello(TestRequest request);

}

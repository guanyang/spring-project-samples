package org.gy.demo.dubbo.provider.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.gy.demo.dubbo.api.dto.TestRequest;
import org.gy.demo.dubbo.api.dto.TestResponse;
import org.gy.demo.dubbo.api.dto.support.Result;
import org.gy.demo.dubbo.api.service.SampleService;
import org.springframework.stereotype.Component;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 */
@Slf4j
@Component
@DubboService(version = "1.0.0", timeout = 3000, validation = "jvalidationNew", filter = {"customValidationFilter"})
public class SampleServiceImpl implements SampleService {

    @Override
    public Result<TestResponse> hello(TestRequest request) {
        TestResponse response = new TestResponse();
        response.setName(request.getName());
        response.setAge(request.getAge());
        response.setTime(System.currentTimeMillis());
        return Result.success(response);
    }
}

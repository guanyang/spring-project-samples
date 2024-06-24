package org.gy.demo.feign.provider.controller;

import org.gy.demo.feign.api.dto.TestRequest;
import org.gy.demo.feign.api.dto.TestResponse;
import org.gy.demo.feign.api.dto.support.Result;
import org.gy.demo.feign.api.service.SampleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gy
 */
@RestController
@RequestMapping(SampleService.SERVICE_PATH)
public class SampleController implements SampleService {

    @Override
    public Result<TestResponse> hello(TestRequest request) {
        TestResponse response = new TestResponse();
        response.setName(request.getName());
        response.setAge(request.getAge());
        response.setTime(System.currentTimeMillis());
        return Result.success(response);
    }
}

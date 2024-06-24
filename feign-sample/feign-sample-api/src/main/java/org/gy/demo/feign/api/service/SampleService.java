package org.gy.demo.feign.api.service;


import org.gy.demo.feign.api.dto.TestRequest;
import org.gy.demo.feign.api.dto.TestResponse;
import org.gy.demo.feign.api.dto.support.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 */
@FeignClient(name = SampleService.SERVICE_NAME, path = SampleService.SERVICE_PATH)
public interface SampleService {

    String SERVICE_NAME = "sampleService";

    String SERVICE_PATH = "/test";

    @GetMapping(value = "/hello")
    Result<TestResponse> hello(@SpringQueryMap @Validated TestRequest request);

}

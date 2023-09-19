package org.gy.demo.dubbo.api.service;

import org.gy.demo.dubbo.api.dto.TestRequest;
import org.gy.demo.dubbo.api.dto.TestResponse;
import org.gy.demo.dubbo.api.dto.support.Result;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 */
public interface SampleService {

    Result<TestResponse> hello(TestRequest request);

}

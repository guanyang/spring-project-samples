package org.gy.demo.mq.event.service;


import io.github.guanyang.core.dto.Response;
import io.github.guanyang.mq.core.EventMessageConsumerService;

public interface DemoEventMessageService extends EventMessageConsumerService<String, Response<Object>> {

    void dynamicEventTest(String data);
}

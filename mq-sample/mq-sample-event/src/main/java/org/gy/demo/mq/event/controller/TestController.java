package org.gy.demo.mq.event.controller;

import io.github.guanyang.core.dto.Response;
import io.github.guanyang.mq.core.EventMessageProducerService;
import io.github.guanyang.mq.core.support.EventMessageServiceManager;
import io.github.guanyang.mq.model.EventMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.gy.demo.mq.event.model.EventType;
import org.gy.demo.mq.event.service.DemoEventMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.guanyang.mq.model.IMessageType.MessageTypeCode.DEFAULT_NORMAL;


/**
 * @author gy
 */
@Tag(name = "测试API")
@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Resource(name = "demoEventMessageService")
    private DemoEventMessageService demoEventMessageService;

    @Operation(summary = "事件测试")
    @GetMapping("/dynamicEvent")
    public Response<Void> dynamicEvent(String data, Integer handleType, String bizKey) {
        EventType eventType = EventType.DYNAMIC_DEMO_EVENT;
        EventMessage<String> req = EventMessage.of(eventType, data, bizKey);
        log.info("[dynamicEvent]发送消息开始：{}", req);
        EventMessageProducerService service = EventMessageServiceManager.getSendService(DEFAULT_NORMAL);
        if (handleType == null || handleType == 1) {
            demoEventMessageService.dynamicEventTest(data);
        } else if (handleType == 2) {
            service.asyncSend(req);
        } else if (handleType == 3) {
            service.directHandle(req);
        }
        log.info("[dynamicEvent]发送消息结束");
        return Response.asSuccess();
    }

}

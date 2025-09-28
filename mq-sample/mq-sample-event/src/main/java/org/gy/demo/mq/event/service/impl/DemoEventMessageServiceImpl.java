package org.gy.demo.mq.event.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import io.github.guanyang.core.dto.Response;
import io.github.guanyang.core.exception.BizException;
import io.github.guanyang.mq.annotation.DynamicEventStrategy;
import io.github.guanyang.mq.core.support.AbstractEventMessageConsumerService;
import io.github.guanyang.mq.core.support.EventMessageServiceManager;
import io.github.guanyang.mq.model.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.gy.demo.mq.event.service.DemoEventMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static io.github.guanyang.mq.model.IEventType.DefaultEventType.DEMO_EVENT;
import static io.github.guanyang.mq.model.IMessageType.MessageTypeCode.DEFAULT_KAFKA;
import static io.github.guanyang.mq.model.IMessageType.MessageTypeCode.DEFAULT_NORMAL;
import static org.gy.demo.mq.event.model.EventType.EventTypeCode.DYNAMIC_DEMO_EVENT_CODE;


/**
 * 功能描述：示例事件处理
 *
 * @author gy
 */
@Slf4j
@Service("demoEventMessageService")
public class DemoEventMessageServiceImpl extends AbstractEventMessageConsumerService<String, Response<Object>> implements DemoEventMessageService {

    @Override
    protected Class<String> getDataType() {
        return String.class;
    }

    @Override
    public String convert(EventMessage<?> event) {
        return Optional.ofNullable(event).map(EventMessage::getData).map(Object::toString).orElse(StrUtil.EMPTY);
    }

    @Override
    protected Response<Object> internalExecute(String data) {
        log.info("[DEMO_EVENT]消息数据:{}", data);
        return Response.asSuccess(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DynamicEventStrategy(eventTypeCode = DYNAMIC_DEMO_EVENT_CODE, messageTypeCode = {DEFAULT_KAFKA, DEFAULT_NORMAL})
    public void dynamicEventTest(String data) {
        boolean transactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        boolean currentServiceActive = EventMessageServiceManager.isCurrentServiceActive();
        log.info("[DYNAMIC_DEMO_EVENT]消息数据: data={},transactionActive={},currentServiceActive={}", data, transactionActive, currentServiceActive);
        if (Objects.equals(data, "error")) {
            throw new BizException(500, "测试异常");
        }
    }

    @Override
    public String getEventTypeCode() {
        return DEMO_EVENT.getCode();
    }

    @Override
    public Set<String> getMessageTypeCode() {
        return Sets.newHashSet(DEFAULT_NORMAL, DEFAULT_KAFKA);
    }
}

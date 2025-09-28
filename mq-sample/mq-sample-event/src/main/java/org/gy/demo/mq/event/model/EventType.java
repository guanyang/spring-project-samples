package org.gy.demo.mq.event.model;

import cn.hutool.core.lang.Assert;
import io.github.guanyang.core.annotation.CommonService;
import io.github.guanyang.core.support.IStdEnum;
import io.github.guanyang.mq.model.IEventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.gy.demo.mq.event.model.EventType.EventTypeCode.DYNAMIC_DEMO_EVENT_CODE;


/**
 * 功能描述：消息事件类型
 *
 * @author gy
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
@CommonService
public enum EventType implements IStdEnum<String>, IEventType {

    //消息事件类型，编码必须唯一
    DYNAMIC_DEMO_EVENT(DYNAMIC_DEMO_EVENT_CODE, "动态示例事件"),

    ;

    /**
     * 事件类型编码，必须唯一
     */
    private final String code;
    /**
     * 事件类型描述
     */
    private final String desc;

    public static EventType codeOf(String code) {
        EventType deletedEnum = EventType.codeOf(code, null);
        Assert.notNull(deletedEnum, "unknown EventType code:" + code);
        return deletedEnum;
    }

    public static EventType codeOf(String code, EventType defaultEnum) {
        return IStdEnum.codeOf(EventType.class, code, defaultEnum);
    }

    public interface EventTypeCode {
        String DYNAMIC_DEMO_EVENT_CODE = "DYNAMIC_DEMO_EVENT";
    }

}

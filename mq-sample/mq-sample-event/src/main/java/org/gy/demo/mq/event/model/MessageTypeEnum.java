package org.gy.demo.mq.event.model;

import io.github.guanyang.core.annotation.CommonService;
import io.github.guanyang.core.support.IStdEnum;
import io.github.guanyang.mq.model.IMessageType;
import io.github.guanyang.mq.model.MqType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

import static io.github.guanyang.mq.model.MqType.ROCKETMQ;


/**
 * 功能描述：消息类型
 *
 * @author gy
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
@CommonService
public enum MessageTypeEnum implements IStdEnum<String>, IMessageType {

    CUSTOM_NORMAL("custom", "自定义普通消息", ROCKETMQ),

    ;

    /**
     * 消息配置标识
     */
    private final String code;
    /**
     * 描述
     */
    private final String desc;
    /**
     * MQ类型
     */
    private final MqType mqType;

    public static MessageTypeEnum codeOf(String code) {
        MessageTypeEnum item = codeOf(code, null);
        return Objects.requireNonNull(item, () -> "unknown MessageTypeEnum error:" + code);
    }

    public static MessageTypeEnum codeOf(String code, MessageTypeEnum defaultEnum) {
        return IStdEnum.codeOf(MessageTypeEnum.class, code, defaultEnum);
    }
}

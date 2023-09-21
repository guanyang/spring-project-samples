package org.gy.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DeletedEnum {

    //正常枚举
    NO(0, "正常"), //删除枚举
    YES(1, "删除");

    private final Integer code;

    private final String desc;

    private static final Map<Integer, DeletedEnum> DEFAULT_MAP;

    static {
        DEFAULT_MAP = Stream.of(values()).collect(Collectors.toMap(DeletedEnum::getCode, Function.identity(), (k1, k2) -> k1));
    }

    public static DeletedEnum codeOf(Integer code, DeletedEnum defaultType) {
        return DEFAULT_MAP.getOrDefault(code, defaultType);
    }

    public static DeletedEnum codeOf(Integer code) {
        DeletedEnum deletedEnum = codeOf(code, null);
        return Objects.requireNonNull(deletedEnum, () -> "unknown DeletedEnum code:" + code);
    }


}

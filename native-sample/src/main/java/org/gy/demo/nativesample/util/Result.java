package org.gy.demo.nativesample.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.io.Serial;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 * @date 2023/8/9 14:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class Result<T> extends BaseResult {
    @Serial
    private static final long serialVersionUID = -1822322929921254884L;

    private T data;

    public static <T> Result<T> success(T data) {
        return of(HttpStatus.OK, data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return of(HttpStatus.OK, msg, data);
    }


    public static <T> Result<T> error(HttpStatus status) {
        return of(status, null);
    }

    public static <T> Result<T> error(HttpStatus status, String msg) {
        return of(status, msg, null);
    }


    public static <T> Result<T> of(HttpStatus status, T data) {
        return of(status, status.getReasonPhrase(), data);
    }

    public static <T> Result<T> of(HttpStatus status, String msg, T data) {
        return of(status.value(), msg, data);
    }

    public static <T> Result<T> of(int code, String msg, T data) {
        Result<T> result = new Result<>();
        result.wrapResult(code, msg);
        result.setData(data);
        return result;
    }
}

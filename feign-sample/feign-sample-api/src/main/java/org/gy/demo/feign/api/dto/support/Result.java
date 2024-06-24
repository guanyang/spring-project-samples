package org.gy.demo.feign.api.dto.support;

import java.io.Serial;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class Result<T> extends BaseResult {

    @Serial
    private static final long serialVersionUID = -1822322929921254884L;

    private T data;

    public static <T> Result<T> success(T data) {
        return of(BaseErrorCode.SUCCESS, data);
    }

    public static <T> Result<T> success(String msg, T data) {
        return of(BaseErrorCode.SUCCESS, msg, data);
    }


    public static <T> Result<T> error(ErrorCodeI status) {
        return of(status, null);
    }

    public static <T> Result<T> error(ErrorCodeI status, String msg) {
        return of(status, msg, null);
    }


    public static <T> Result<T> of(ErrorCodeI status, T data) {
        return of(status, status.getMessage(), data);
    }

    public static <T> Result<T> of(ErrorCodeI status, String msg, T data) {
        return of(status.getCode(), msg, data);
    }

    public static <T> Result<T> of(int code, String msg, T data) {
        Result<T> result = new Result<>();
        result.wrapResult(code, msg);
        result.setData(data);
        return result;
    }

}

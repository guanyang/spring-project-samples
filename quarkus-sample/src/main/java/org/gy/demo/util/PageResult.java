package org.gy.demo.util;

import jakarta.ws.rs.core.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PageResult<T> extends BaseResult {
    @Serial
    private static final long serialVersionUID = -3424014589767966265L;

    private List<T> data = Collections.emptyList();
    private long total;

    public static <T> PageResult<T> success(long total, List<T> data) {
        return of(Response.Status.OK, total, data);
    }

    public static <T> PageResult<T> success(String msg, long total, List<T> data) {
        return of(Response.Status.OK, msg, total, data);
    }


    public static <T> PageResult<T> error(Response.Status status) {
        return of(status, 0, Collections.emptyList());
    }

    public static <T> PageResult<T> error(Response.Status status, String msg) {
        return of(status, msg, 0, Collections.emptyList());
    }


    public static <T> PageResult<T> of(Response.Status status, long total, List<T> data) {
        return of(status, status.getReasonPhrase(), total, data);
    }

    public static <T> PageResult<T> of(Response.Status status, String msg, long total, List<T> data) {
        return of(status.getStatusCode(), msg, total, data);
    }


    public static <T> PageResult<T> of(int code, String msg, long total, List<T> data) {
        PageResult<T> result = new PageResult<>();
        result.wrapResult(code, msg);
        result.setTotal(total);
        result.setData(data);
        return result;
    }


}

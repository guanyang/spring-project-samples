package org.gy.demo.util;

import jakarta.ws.rs.core.Response;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public abstract class BaseResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 3224014589767966265L;

    public static final Response.Status SUCCESS = Response.Status.OK;
    public static final Response.Status ERROR = Response.Status.BAD_REQUEST;

    private int code = SUCCESS.getStatusCode();

    private String message = SUCCESS.getReasonPhrase();

    public boolean success() {
        return SUCCESS.getStatusCode() == code;
    }

    public void wrapResult(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }
}

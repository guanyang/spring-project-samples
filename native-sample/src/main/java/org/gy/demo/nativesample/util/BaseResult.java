package org.gy.demo.nativesample.util;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public abstract class BaseResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 3224014589767966265L;

    public static final HttpStatus SUCCESS = HttpStatus.OK;
    public static final HttpStatus ERROR = HttpStatus.BAD_REQUEST;

    private int code = SUCCESS.value();

    private String message = SUCCESS.getReasonPhrase();

    public boolean success() {
        return SUCCESS.value() == code;
    }

    public void wrapResult(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }
}

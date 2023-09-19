package org.gy.demo.dubbo.api.dto.support;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static org.gy.demo.dubbo.api.dto.support.BaseErrorCode.SUCCESS;

@Data
public abstract class BaseResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 3224014589767966265L;

    public static final int SUCCESS_CODE = SUCCESS.getCode();
    public static final String SUCCESS_MSG = SUCCESS.getMessage();

    private int code = SUCCESS_CODE;

    private String message = SUCCESS_MSG;

    public boolean success() {
        return SUCCESS_CODE == code;
    }

    public void wrapResult(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }
}

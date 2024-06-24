package org.gy.demo.feign.api.dto.support;


import static org.gy.demo.feign.api.dto.support.BaseErrorCode.SUCCESS;

import java.io.Serial;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author gy
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseResult extends BaseDTO {

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

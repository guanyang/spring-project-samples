package org.gy.demo.feign.api.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.gy.demo.feign.api.dto.support.BaseDTO;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class TestRequest extends BaseDTO {

    @Serial
    private static final long serialVersionUID = -9022845908217257188L;


    @NotNull(message = "name不能为空")
    @Length(min = 2, max = 32, message = "name字符数只能介于2~32之间")
    private String name;

    @NotNull(message = "age不能为空")
    @Range(min = 1, max = 100, message = "age只能介于1~100之间")
    private Integer age;
}

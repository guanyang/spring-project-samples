package org.gy.demo.feign.api.dto;

import java.io.Serial;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.gy.demo.feign.api.dto.support.BaseDTO;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class TestResponse extends BaseDTO {

    @Serial
    private static final long serialVersionUID = -4703617309588032686L;

    private String name;

    private Integer age;

    private Long time;
}

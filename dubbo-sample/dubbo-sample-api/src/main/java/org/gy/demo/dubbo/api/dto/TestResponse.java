package org.gy.demo.dubbo.api.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 */
@Data
public class TestResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1717195822521183586L;

    private String name;

    private Integer age;

    private Long time;
}

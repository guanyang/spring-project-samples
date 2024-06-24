package org.gy.demo.feign.api.dto.support;

import com.alibaba.fastjson2.JSON;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public abstract class BaseDTO implements Serializable {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

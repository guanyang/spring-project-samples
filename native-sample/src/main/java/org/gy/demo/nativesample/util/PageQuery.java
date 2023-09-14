package org.gy.demo.nativesample.util;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -1828037735162653632L;

    @Min(message = "页码必须大于0", value = 1)
    private int pageNum = 1;

    @Min(message = "页面大小必须大于0", value = 1)
    @Max(message = "页面大小不能超过100", value = 100)
    private int pageSize = 10;

    public int getOffset() {
        return pageNum > 0 ? (pageNum - 1) * pageSize : 0;
    }

}

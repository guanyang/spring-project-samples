package org.gy.demo.nativesample.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * <p>
 * 新演示表
 * </p>
 *
 * @author gy
 * @since 2023-07-14
 */
@Data
@Accessors(chain = true)
@Table("hello_world_new")
public class HelloWorldNew implements Serializable {

    @Serial
    private static final long serialVersionUID = -7641102286866070790L;
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 删除状态，0正常 1删除
     */
    private Integer deleted;

    /**
     * 创建人
     */
    @Column("create_by")
    private String createBy;

    /**
     * 编辑人
     */
    @Column("update_by")
    private String updateBy;

    /**
     * 创建时间
     */
    @Column("create_time")
    private ZonedDateTime createTime;

    /**
     * 更新时间
     */
    @Column("update_time")
    private ZonedDateTime updateTime;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String VERSION = "version";

    public static final String DELETED = "deleted";

    public static final String CREATE_BY = "create_by";

    public static final String UPDATE_BY = "update_by";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}

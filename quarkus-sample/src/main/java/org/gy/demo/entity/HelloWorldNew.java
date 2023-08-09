package org.gy.demo.entity;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowIterator;
import io.vertx.mutiny.sqlclient.RowSet;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 新演示表
 * </p>
 *
 * @author gy
 * @since 2023-07-14
 */
@Getter
@Setter
@Accessors(chain = true)
public class HelloWorldNew implements Serializable {

    @Serial
    private static final long serialVersionUID = 2011846515266133491L;
    /**
     * 主键
     */
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
    private String createBy;

    /**
     * 编辑人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String VERSION = "version";

    public static final String DELETED = "deleted";

    public static final String CREATE_BY = "create_by";

    public static final String UPDATE_BY = "update_by";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static HelloWorldNew from(Row row) {
        HelloWorldNew entity = new HelloWorldNew();
        entity.setId(row.getLong(ID));
        entity.setName(row.getString(NAME));
        entity.setVersion(row.getInteger(VERSION));
        entity.setDeleted(row.getInteger(DELETED));
        entity.setCreateBy(row.getString(CREATE_BY));
        entity.setUpdateBy(row.getString(UPDATE_BY));
        entity.setCreateTime(row.getLocalDateTime(CREATE_TIME));
        entity.setUpdateTime(row.getLocalDateTime(UPDATE_TIME));
        return entity;
    }

    public static HelloWorldNew from(RowSet<Row> rows) {
        return Optional.ofNullable(rows).map(RowSet::iterator)
                .filter(RowIterator::hasNext)
                .map(RowIterator::next)
                .map(HelloWorldNew::from)
                .orElse(null);
    }

}

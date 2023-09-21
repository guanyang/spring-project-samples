package org.gy.demo.service.impl;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlClientHelper;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.gy.demo.entity.HelloWorldNew;
import org.gy.demo.service.HelloWorldNewService;
import org.gy.demo.util.PageQuery;
import org.gy.demo.util.PageResult;
import org.gy.demo.util.Result;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@ApplicationScoped
public class HelloWorldNewServiceImpl implements HelloWorldNewService {

    @Inject
    MySQLPool client;

    @Override
    public Uni<PageResult<HelloWorldNew>> list(PageQuery query) {
        return SqlClientHelper.usingConnectionUni(client, con -> con.query("select count(1) from hello_world_new where deleted=0").execute().map(rows -> rows.iterator().next().getLong(0)).chain(r -> {
            if (r == 0) {
                List<HelloWorldNew> list = new ArrayList<>();
                return Uni.createFrom().item(PageResult.success(0, list));
            } else {
                Tuple tuple = Tuple.of(query.getOffset(), query.getPageSize());
                Multi<HelloWorldNew> users = con.preparedQuery("select id, name, version, deleted, create_by, update_by, create_time, update_time from hello_world_new where deleted=0 order by id desc limit ?,?").execute(tuple).onItem().transformToMulti(RowSet::toMulti).map(HelloWorldNew::from);
                return users.collect().asList().map(list -> PageResult.success(r, list));
            }
        }));
    }

    @Override
    public Uni<Result<HelloWorldNew>> get(Long id) {
        return client.preparedQuery("select id, name, version, deleted, create_by, update_by, create_time, update_time from hello_world_new where id = ?").execute(Tuple.of(id)).map(HelloWorldNew::from).map(u -> u == null ? Result.error(Response.Status.BAD_REQUEST) : Result.success(u));
    }

    @Override
    public Uni<Result<HelloWorldNew>> add(HelloWorldNew entity) {
        Tuple tuple = Tuple.of(entity.getName(), entity.getVersion(), entity.getDeleted(), entity.getCreateBy(), entity.getUpdateBy());
        return SqlClientHelper.inTransactionUni(client, con -> con.preparedQuery("insert into hello_world_new(name, version, deleted, create_by, update_by) values(?,?,?,?,?)").execute(tuple).onItem().transformToUni(r -> con.query("SELECT LAST_INSERT_ID()").execute().map(rows -> {
            Long id = rows.iterator().next().getLong(0);
            entity.setId(id);
            return Result.success(entity);
        })));
    }

    @Override
    public Uni<Result<Boolean>> update(HelloWorldNew entity) {
        Tuple tuple = Tuple.of(entity.getName(), entity.getVersion(), entity.getDeleted(), entity.getUpdateBy(), entity.getId());
        return SqlClientHelper.inTransactionUni(client, con -> con.preparedQuery("update hello_world_new set name =?,version=?,deleted=?,update_by=? where id=?").execute(tuple).map(rows -> rows.rowCount() >= 1).map(Result::success));
    }

    @Override
    public Uni<Result<Boolean>> delete(Long id) {
        return SqlClientHelper.inTransactionUni(client, con -> con.preparedQuery("update hello_world_new set deleted=1 where id=?").execute(Tuple.of(id)).map(rows -> rows.rowCount() >= 1).map(Result::success));
    }

}

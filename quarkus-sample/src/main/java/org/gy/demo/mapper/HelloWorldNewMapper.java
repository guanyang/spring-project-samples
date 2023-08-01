package org.gy.demo.mapper;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.gy.demo.entity.HelloWorldNew;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 * @date 2023/8/1 17:25
 */
@ApplicationScoped
public class HelloWorldNewMapper {

    @Inject
    MySQLPool client;

    public Multi<HelloWorldNew> findAll() {
        return client.query("select id, name, version, deleted, create_by, update_by, create_time, update_time from hello_world_new order by id desc").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(HelloWorldNew::from);
    }

    public Uni<HelloWorldNew> findById(Long id) {
        return client.preparedQuery("select id, name, version, deleted, create_by, update_by, create_time, update_time from hello_world_new WHERE id = ?").execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? HelloWorldNew.from(iterator.next()) : null);
    }

    public Uni<Boolean> save(String name) {
        return client.preparedQuery("INSERT INTO hello_world_new (name) VALUES (?)").execute(Tuple.of(name))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    public Uni<Boolean> delete(Long id) {
        return client.preparedQuery("DELETE FROM hello_world_new WHERE id = ?").execute(Tuple.of(id))
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

}

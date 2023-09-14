package org.gy.demo.nativesample.mapper;

import org.gy.demo.nativesample.entity.HelloWorldNew;
import org.gy.demo.nativesample.util.PageQuery;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HelloWorldNewRepository extends R2dbcRepository<HelloWorldNew, Long> {


    @Query("SELECT * FROM hello_world_new WHERE deleted = 0 order by id desc limit :#{#query.offset},:#{#query.pageSize}")
    Flux<HelloWorldNew> page(@Param("query") PageQuery query);

    @Modifying
    @Query("UPDATE hello_world_new SET name = :#{#entity.name},deleted = :#{#entity.deleted} where id = :#{#entity.id}")
    Mono<Integer> update(@Param("entity") HelloWorldNew entity);
}

package org.gy.demo.service;


import io.smallrye.mutiny.Uni;
import org.gy.demo.entity.HelloWorldNew;
import org.gy.demo.util.PageQuery;
import org.gy.demo.util.PageResult;
import org.gy.demo.util.Result;

public interface HelloWorldNewService {

    Uni<PageResult<HelloWorldNew>> list(PageQuery query);

    Uni<Result<HelloWorldNew>> get(Long id);

    Uni<Result<HelloWorldNew>> add(HelloWorldNew entity);

    Uni<Result<Boolean>> update(HelloWorldNew entity);

    Uni<Result<Boolean>> delete(Long id);
}

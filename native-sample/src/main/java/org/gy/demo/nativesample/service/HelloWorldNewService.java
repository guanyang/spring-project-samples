package org.gy.demo.nativesample.service;

import org.gy.demo.nativesample.entity.HelloWorldNew;
import org.gy.demo.nativesample.util.PageQuery;
import org.gy.demo.nativesample.util.PageResult;
import org.gy.demo.nativesample.util.Result;
import reactor.core.publisher.Mono;

public interface HelloWorldNewService {

    Mono<PageResult<HelloWorldNew>> list(PageQuery query);

    Mono<Result<HelloWorldNew>> get(Long id);

    Mono<Result<HelloWorldNew>> add(HelloWorldNew entity);

    Mono<Result<Boolean>> update(HelloWorldNew entity);

    Mono<Result<Boolean>> delete(Long id);
}

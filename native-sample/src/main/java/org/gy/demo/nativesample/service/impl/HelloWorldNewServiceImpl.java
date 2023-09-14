package org.gy.demo.nativesample.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.gy.demo.nativesample.entity.HelloWorldNew;
import org.gy.demo.nativesample.enums.DeletedEnum;
import org.gy.demo.nativesample.mapper.HelloWorldNewRepository;
import org.gy.demo.nativesample.service.HelloWorldNewService;
import org.gy.demo.nativesample.util.PageQuery;
import org.gy.demo.nativesample.util.PageResult;
import org.gy.demo.nativesample.util.Result;
import org.springframework.data.domain.Example;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Service
public class HelloWorldNewServiceImpl implements HelloWorldNewService {

    @Resource
    private HelloWorldNewRepository repository;

    @Resource
    private R2dbcEntityTemplate template;

    @Override
    public Mono<PageResult<HelloWorldNew>> list(PageQuery query) {
        HelloWorldNew example = new HelloWorldNew();
        example.setDeleted(DeletedEnum.NO.getCode());
        Mono<Long> count = repository.count(Example.of(example));
        return count.flatMap(c -> {
            if (c == 0) {
                return Mono.just(PageResult.success(0, Collections.emptyList()));
            } else {
                Flux<HelloWorldNew> page = repository.page(query);
                return page.collectList().map(list -> PageResult.success(c, list));
            }
        });
    }

    @Override
    public Mono<Result<HelloWorldNew>> get(Long id) {
        return repository.findById(id).map(Result::success).defaultIfEmpty(Result.error(HttpStatus.BAD_REQUEST));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Result<HelloWorldNew>> add(HelloWorldNew entity) {
        return repository.save(entity).map(Result::success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Result<Boolean>> update(HelloWorldNew entity) {
        return repository.update(entity).map(r -> r > 0).map(Result::success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Result<Boolean>> delete(Long id) {
        return repository.deleteById(id).thenReturn(Result.success(Boolean.TRUE));
    }

}

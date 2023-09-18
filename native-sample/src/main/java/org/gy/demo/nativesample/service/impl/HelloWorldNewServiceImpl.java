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
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static org.springframework.data.domain.Sort.Order.desc;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.relational.core.query.Criteria.where;

@Slf4j
@Service
public class HelloWorldNewServiceImpl implements HelloWorldNewService {

    @Resource
    private HelloWorldNewRepository repository;

    @Resource
    private R2dbcEntityTemplate template;

    @Override
    public Mono<PageResult<HelloWorldNew>> list(PageQuery query) {
        Query queryWrapper = Query.query(where(HelloWorldNew.DELETED).is(DeletedEnum.NO.getCode()));
        return template.count(queryWrapper, HelloWorldNew.class).flatMap(c -> {
            if (c == 0) {
                return Mono.just(PageResult.success(0, Collections.emptyList()));
            } else {
                Query pageQuery = queryWrapper.sort(by(desc(HelloWorldNew.ID))).offset(query.getOffset()).limit(query.getPageSize());
                Flux<HelloWorldNew> page = template.select(pageQuery, HelloWorldNew.class);
                return page.collectList().map(list -> PageResult.success(c, list));
            }
        });

//        HelloWorldNew example = new HelloWorldNew();
//        example.setDeleted(DeletedEnum.NO.getCode());
//        Mono<Long> count = repository.count(Example.of(example));
//        return count.flatMap(c -> {
//            if (c == 0) {
//                return Mono.just(PageResult.success(0, Collections.emptyList()));
//            } else {
//                Flux<HelloWorldNew> page = repository.page(query);
//                return page.collectList().map(list -> PageResult.success(c, list));
//            }
//        });
    }

    @Override
    public Mono<Result<HelloWorldNew>> get(Long id) {
        Query queryWrapper = Query.query(where(HelloWorldNew.ID).is(id));
        return template.selectOne(queryWrapper, HelloWorldNew.class).map(Result::success).defaultIfEmpty(Result.error(HttpStatus.BAD_REQUEST));
//        return repository.findById(id).map(Result::success).defaultIfEmpty(Result.error(HttpStatus.BAD_REQUEST));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Result<HelloWorldNew>> add(HelloWorldNew entity) {
        return template.insert(entity).map(Result::success);
//        return repository.save(entity).map(Result::success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Result<Boolean>> update(HelloWorldNew entity) {
        Query queryWrapper = Query.query(where(HelloWorldNew.ID).is(entity.getId()));
        Map<SqlIdentifier, Object> updateMap = new HashMap<>();
        updateWrapper(updateMap, HelloWorldNew.NAME, entity::getName);
        updateWrapper(updateMap, HelloWorldNew.DELETED, entity::getDeleted);
        updateWrapper(updateMap, HelloWorldNew.VERSION, entity::getVersion);
        updateWrapper(updateMap, HelloWorldNew.UPDATE_BY, entity::getUpdateBy);
        return template.update(queryWrapper, Update.from(updateMap), HelloWorldNew.class).map(r -> r > 0).map(Result::success);

//        return repository.update(entity).map(r -> r > 0).map(Result::success);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Result<Boolean>> delete(Long id) {
        Query queryWrapper = Query.query(where(HelloWorldNew.ID).is(id));
        return template.delete(queryWrapper, HelloWorldNew.class).map(r -> r > 0).map(Result::success);
//        return repository.deleteById(id).thenReturn(Result.success(Boolean.TRUE));
    }

    private static void updateWrapper(Map<SqlIdentifier, Object> updateMap, String column, Supplier<Object> value) {
        Optional.ofNullable(value.get()).ifPresent(v -> updateMap.put(SqlIdentifier.unquoted(column), v));
    }

}

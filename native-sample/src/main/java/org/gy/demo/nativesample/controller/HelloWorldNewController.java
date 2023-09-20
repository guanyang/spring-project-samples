package org.gy.demo.nativesample.controller;

import lombok.extern.slf4j.Slf4j;
import org.gy.demo.nativesample.entity.HelloWorldNew;
import org.gy.demo.nativesample.service.HelloWorldNewService;
import org.gy.demo.nativesample.util.PageQuery;
import org.gy.demo.nativesample.util.PageResult;
import org.gy.demo.nativesample.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/test")
public class HelloWorldNewController {

    @Autowired
    private HelloWorldNewService helloWorldService;

    @GetMapping("/list")
    public Mono<PageResult<HelloWorldNew>> list(PageQuery query) {
        return helloWorldService.list(query);
    }

    @GetMapping("/get/{id}")
    public Mono<Result<HelloWorldNew>> get(@PathVariable Long id) {
        return helloWorldService.get(id);
    }

    @PostMapping("/add")
    public Mono<Result<HelloWorldNew>> add(@RequestBody HelloWorldNew entity) {
        return helloWorldService.add(entity);
    }

    @PostMapping("/update")
    public Mono<Result<Boolean>> update(@RequestBody HelloWorldNew entity) {
        return helloWorldService.update(entity);
    }

    @PostMapping("/delete/{id}")
    public Mono<Result<Boolean>> delete(@PathVariable Long id) {
        return helloWorldService.delete(id);
    }

}

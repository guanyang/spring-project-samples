package org.gy.demo.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.gy.demo.entity.HelloWorldNew;
import org.gy.demo.service.HelloWorldNewService;
import org.gy.demo.util.PageQuery;
import org.gy.demo.util.PageResult;
import org.gy.demo.util.Result;
import org.jboss.resteasy.reactive.RestPath;

@Slf4j
@Path("/api/test")
public class HelloWorldNewController {

    @Inject
    HelloWorldNewService helloWorldService;

    @GET
    @Path("/list")
    public Uni<PageResult<HelloWorldNew>> list(@Valid @BeanParam PageQuery query) {
        return helloWorldService.list(query);
    }

    @GET
    @Path("/get/{id}")
    public Uni<Result<HelloWorldNew>> get(@RestPath Long id) {
        return helloWorldService.get(id);
    }

    @POST
    @Path("/add")
    public Uni<Result<HelloWorldNew>> add(HelloWorldNew entity) {
        return helloWorldService.add(entity);
    }

    @POST
    @Path("/update")
    public Uni<Result<Boolean>> update(HelloWorldNew entity) {
        return helloWorldService.update(entity);
    }

    @POST
    @Path("/delete/{id}")
    public Uni<Result<Boolean>> delete(@RestPath Long id) {
        return helloWorldService.delete(id);
    }

}

package org.gy.demo;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.gy.demo.entity.HelloWorldNew;
import org.gy.demo.mapper.HelloWorldNewMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 * @date 2023/8/1 16:18
 */
@Path("/api/test")
public class TestResource {

    @Inject
    HelloWorldNewMapper mapper;

    @GET
    @Path("/hello")
    public Object hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello from RESTEasy Reactive");
        return map;
    }

    @GET
    @Path("/list")
    public Multi<HelloWorldNew> list() {
        return mapper.findAll();
    }

    @GET
    @Path("/get/{id}")
    public Uni<Response> getSingle(Long id) {
        return mapper.findById(id)
                .onItem().transform(item -> item != null ? Response.ok(item) : Response.status(Response.Status.NOT_FOUND))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("/save")
    public Uni<Response> create(@FormParam("name") String name) {
        return mapper.save(name)
                .onItem().transform(status -> Response.ok(status).build());
    }

    @DELETE
    @Path("/delete/{id}")
    public Uni<Response> delete(Long id) {
        return mapper.delete(id)
                .onItem().transform(status -> Response.ok(status).build());
    }
}

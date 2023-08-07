package org.gy.demo;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.runtime.client.config.RedisConfig;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.gy.demo.entity.HelloWorldNew;
import org.gy.demo.mapper.HelloWorldNewMapper;
import org.gy.demo.redis.RedisExample;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

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

    @Inject
    RedisExample redisExample;

    @Inject
    RedisConfig redisConfig;

    @GET
    @Path("/hello")
    public Object hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello from RESTEasy Reactive");
        return map;
    }

    @GET
    @Path("/config")
    public Object config() {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> envMap = System.getenv();
        Properties properties = System.getProperties();
        result.put("env", envMap);
        result.put("properties", properties);
        result.put("redisConfig", redisConfig);
        return result;
    }

    @GET
    @Path("/list")
    public Multi<HelloWorldNew> list() {
        return mapper.findAll();
    }

    @GET
    @Path("/get/{id}")
    public Uni<Response> getSingle(Long id) {
        return mapper.findById(id).onItem().transform(item -> item != null ? Response.ok(item) : Response.status(Response.Status.NOT_FOUND)).onItem().transform(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("/save")
    public Uni<Response> create(@FormParam("name") String name) {
        return mapper.save(name).onItem().transform(status -> Response.ok(status).build());
    }

    @DELETE
    @Path("/delete/{id}")
    public Uni<Response> delete(Long id) {
        return mapper.delete(id).onItem().transform(status -> Response.ok(status).build());
    }

    @POST
    @Path("/cache/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> save(HelloWorldNew req) {
        return redisExample.set(String.valueOf(req.getId()), req).onItem().transform(status -> Response.ok(status).build());
    }

    @GET
    @Path("/cache/get/{id}")
    public Uni<Response> get(Long id) {
        return redisExample.get(String.valueOf(id)).onItem().transform(r -> Response.ok(r).build());
    }

    @GET
    @Path("/redisCache/get/{id}")
    @CacheResult(cacheName = "cacheDemo")
    public HelloWorldNew redisCacheGet(@CacheKey Long id) {
        HelloWorldNew entity = new HelloWorldNew();
        entity.setId(id);
        entity.setName(UUID.randomUUID().toString());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return entity;
    }
}

package org.gy.demo;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.runtime.client.config.RedisConfig;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.gy.demo.entity.HelloWorldNew;
import org.gy.demo.grpc.HelloConsumerService;
import org.gy.demo.grpc.HelloRequest;
import org.gy.demo.mapper.HelloWorldNewMapper;
import org.gy.demo.redis.RedisExample;
import org.gy.demo.util.Result;

import java.time.LocalDateTime;
import java.util.*;

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

    @Inject
    HelloConsumerService helloConsumerService;

    @GET
    @Path("/hello")
    public Uni<Object> hello() {
        Map<String, Object> map = new HashMap<>();
        map.put("time", System.currentTimeMillis());
        map.put("msg", "Hello from RESTEasy Reactive");
        return Uni.createFrom().item(map);
    }

    @GET
    @Path("/config")
    public Uni<Object> config() {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> envMap = System.getenv();
        Properties properties = System.getProperties();
        result.put("env", envMap);
        result.put("properties", properties);
        result.put("redisConfig", redisConfig);
        return Uni.createFrom().item(result);
    }

    @GET
    @Path("/list")
    public Uni<Result<List<HelloWorldNew>>> list() {
        return mapper.findAll().collect().asList().map(Result::success);
    }

    @GET
    @Path("/get/{id}")
    public Uni<Result<HelloWorldNew>> getSingle(Long id) {
        return mapper.findById(id).map(Result::success);
    }

    @PUT
    @Path("/save")
    public Uni<Result<Long>> create(@FormParam("name") String name) {
        return mapper.save(name).map(Result::success);
    }

    @DELETE
    @Path("/delete/{id}")
    public Uni<Result<Boolean>> delete(Long id) {
        return mapper.delete(id).map(Result::success);
    }

    @POST
    @Path("/cache/save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Result<Boolean>> save(HelloWorldNew req) {
        return redisExample.set(String.valueOf(req.getId()), req).map(Result::success);
    }

    @GET
    @Path("/cache/get/{id}")
    public Uni<Result<HelloWorldNew>> get(Long id) {
        return redisExample.get(String.valueOf(id)).map(Result::success);
    }

    @GET
    @Path("/redisCache/get/{id}")
    @CacheResult(cacheName = "cacheDemo")
    public Uni<Result<HelloWorldNew>> redisCacheGet(@CacheKey Long id) {
        HelloWorldNew entity = new HelloWorldNew();
        entity.setId(id);
        entity.setName(UUID.randomUUID().toString());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return Uni.createFrom().item(Result.success(entity));
    }

    @GET
    @Path("/grpc/{name}")
    public Uni<Result<String>> hello(String name) {
        return helloConsumerService.sayHello(HelloRequest.newBuilder().setName(name).build()).map(helloReply -> Result.success(helloReply.getMessage()));
    }
}

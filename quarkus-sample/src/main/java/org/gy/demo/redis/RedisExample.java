package org.gy.demo.redis;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.hash.ReactiveHashCommands;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.gy.demo.entity.HelloWorldNew;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 * @date 2023/8/1 19:43
 */
@ApplicationScoped
public class RedisExample {

//    @Inject
//    ReactiveRedisDataSource reactiveDataSource;
//    @Inject
//    RedisDataSource redisDataSource;
//    @Inject
//    RedisAPI redisAPI;

    private static final String MY_KEY = "my-key";
    private static final int EXPIRE_TIME = 300;

    private final ReactiveHashCommands<String, String, HelloWorldNew> commands;
    private final ReactiveKeyCommands<String> keyCommands;

    public RedisExample(ReactiveRedisDataSource ds) {
        commands = ds.hash(HelloWorldNew.class);
        keyCommands = ds.key();
    }

    public Uni<Boolean> set(String field, HelloWorldNew value) {
        Uni<Boolean> result = commands.hset(MY_KEY, field, value);
        return result.chain(r -> keyCommands.expire(MY_KEY, EXPIRE_TIME));
    }

    public Uni<HelloWorldNew> get(String field) {
        return commands.hget(MY_KEY, field);
    }

}

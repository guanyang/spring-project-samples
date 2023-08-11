package org.gy.demo.grpc;


import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 * @date 2023/8/8 09:53
 */
@Slf4j
@GrpcService
public class HelloProviderService implements Greeter {

    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        log.info("[Greeter]sayHello: {}", request.getName());
        return Uni.createFrom().item(() -> HelloReply.newBuilder().setMessage("Hello " + request.getName()).build());
    }
}

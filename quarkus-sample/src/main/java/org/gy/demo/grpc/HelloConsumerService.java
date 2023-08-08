package org.gy.demo.grpc;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 * @date 2023/8/8 10:00
 */
@ApplicationScoped
public class HelloConsumerService {

    @GrpcClient
    Greeter hello;

    public Uni<HelloReply> sayHello(HelloRequest request) {
        return hello.sayHello(request);
    }

}

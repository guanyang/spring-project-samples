package org.gy.demo.grpc;


import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

/**
 * 功能描述：
 *
 * @author gy
 * @version 1.0.0
 * @date 2023/8/8 09:53
 */
@GrpcService
public class HelloProviderService implements Greeter {

    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        return Uni.createFrom().item(() -> HelloReply.newBuilder().setMessage("Hello " + request.getName()).build());
    }
}

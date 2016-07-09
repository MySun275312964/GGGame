package com.gg.example.test;

import java.io.IOException;

import com.gg.example.common.ExampleConst;
import com.gg.example.test.protocol.TestGrpc;

import io.grpc.internal.ServerImpl;
import io.grpc.netty.NettyServerBuilder;

/**
 * @author guofeng.qin
 */
public class TestServer {
    public static void main(String[] args) throws IOException {
        ServerImpl server = NettyServerBuilder.forPort(ExampleConst.Test.TestServerPort)
                .addService(TestGrpc.bindService(new TestService())).build();
        server.start();

        while (!server.isShutdown()) {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

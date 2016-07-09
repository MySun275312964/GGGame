package com.gg.gate.gRPC.test;

import java.io.IOException;

import com.gg.gate.gRPC.proto.GeoXGrpc;

import io.grpc.internal.ServerImpl;
import io.grpc.netty.NettyServerBuilder;

/**
 * @author guofeng.qin
 */
public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerImpl server =
                NettyServerBuilder.forPort(9876).addService(GeoXGrpc.bindService(new GeoXService())).build().start();
        server.awaitTermination();
    }
}

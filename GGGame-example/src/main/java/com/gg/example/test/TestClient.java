package com.gg.example.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gg.common.Constants;
import com.gg.common.JsonHelper;
import com.gg.example.common.ExampleConst;
import com.gg.example.test.protocol.Example;
import com.gg.example.test.protocol.Example.TestRequest;
import com.gg.example.test.protocol.Example.TestResponse;
import com.gg.example.test.protocol.TestGrpc;
import com.gg.example.test.protocol.TestGrpc.TestStub;

import io.grpc.internal.ManagedChannelImpl;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * @author guofeng.qin
 */
public class TestClient {
    private static final Logger logger = LoggerFactory.getLogger(TestClient.class);

    public static void main(String[] args) {
        ManagedChannelImpl channel =
                NettyChannelBuilder.forAddress(Constants.Localhost, ExampleConst.Test.TestServerPort)
                        .negotiationType(NegotiationType.PLAINTEXT).build();
        TestStub stub = TestGrpc.newStub(channel);
        StreamObserver<TestRequest> req = stub.post(new StreamObserver<Example.TestResponse>() {

            @Override
            public void onNext(TestResponse arg0) {
                logger.info(JsonHelper.toJson(arg0));
            }

            @Override
            public void onError(Throwable arg0) {
                logger.warn("client error... ", arg0);
            }

            @Override
            public void onCompleted() {
                logger.warn("client onCompleted... ");
            }
        });

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            req.onNext(TestRequest.newBuilder().setId(System.currentTimeMillis() + "").build());
        }
    }
}

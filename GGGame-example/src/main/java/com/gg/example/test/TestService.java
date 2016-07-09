package com.gg.example.test;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gg.example.test.protocol.Example;
import com.gg.example.test.protocol.Example.TestEntry;
import com.gg.example.test.protocol.Example.TestRequest;
import com.gg.example.test.protocol.Example.TestResponse;
import com.gg.example.test.protocol.TestGrpc;

import io.grpc.stub.StreamObserver;

/**
 * @author guofeng.qin
 */
public class TestService implements TestGrpc.Test {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Override
    public StreamObserver<TestRequest> post(StreamObserver<TestResponse> responseObserver) {
        return new StreamObserver<Example.TestRequest>() {

            @Override
            public void onCompleted() {
                logger.warn("service onCompleted...");
            }

            @Override
            public void onError(Throwable arg0) {
                logger.warn("service onError...", arg0);
            }

            @Override
            public void onNext(Example.TestRequest arg0) {
                if (arg0 != null) {
                    List<TestEntry> list = new ArrayList<TestEntry>();
                    for (int i = 0; i < 100; i++) {
                        list.add(TestEntry.newBuilder().setId(i).setName("TestName:" + i).build());
                    }
                    TestResponse resp = TestResponse.newBuilder().addAllNames(list).build();
                    responseObserver.onNext(resp);
                }
            }
        };
    }
}

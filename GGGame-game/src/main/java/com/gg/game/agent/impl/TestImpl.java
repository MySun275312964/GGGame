package com.gg.game.agent.impl;

import com.gg.game.proto.Test;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * Created by guofeng.qin on 2016/8/26.
 */
public class TestImpl extends Test.ITest {

    @Override
    public void connect(RpcController controller, Test.TestRequest request, RpcCallback<Test.TestResponse> done) {
        Test.TestResponse resp = Test.TestResponse.newBuilder().setCode(111).build();
        done.run(resp);
    }
}

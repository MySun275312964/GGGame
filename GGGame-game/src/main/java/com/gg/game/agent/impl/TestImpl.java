package com.gg.game.agent.impl;

import com.gg.game.proto.GameProto;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * Created by guofeng.qin on 2016/8/26.
 */
public class TestImpl extends GameProto.ITest {

    @Override
    public void connect(RpcController controller, GameProto.TestRequest request,
            RpcCallback<GameProto.TestResponse> done) {
        GameProto.TestResponse resp = GameProto.TestResponse.newBuilder().setCode(111).build();
        done.run(resp);
    }
}

package com.gg.core.net;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * Created by hunter on 8/21/16.
 */
public class NetPBController implements RpcController {
    @Override
    public void reset() {

    }

    @Override
    public boolean failed() {
        return false;
    }

    @Override
    public String errorText() {
        return null;
    }

    @Override
    public void startCancel() {

    }

    @Override
    public void setFailed(String reason) {

    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void notifyOnCancel(RpcCallback<Object> callback) {

    }
}

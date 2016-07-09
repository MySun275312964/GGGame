package com.gg.core.actor.codec;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class ResponseMessage extends Message {
    private Object result;
    private Throwable error;

    public ResponseMessage() {}

    public ResponseMessage(Object result, Throwable error) {
        this.result = result;
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

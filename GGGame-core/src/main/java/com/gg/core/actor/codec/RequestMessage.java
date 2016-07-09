package com.gg.core.actor.codec;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class RequestMessage extends Message {
    private String func;
    private String argsDesc;
    private Object[] args;

    public RequestMessage() {}

    public RequestMessage(String func, String argsDesc, Object[] args) {
        this.func = func;
        this.argsDesc = argsDesc;
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getFunc() {
        return func;
    }

    public String getArgsDesc() {
        return argsDesc;
    }
}

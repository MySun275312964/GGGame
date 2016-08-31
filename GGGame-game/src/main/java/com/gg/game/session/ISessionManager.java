package com.gg.game.session;

import com.gg.core.net.IMsgDispatch;
import com.google.protobuf.MessageOrBuilder;

/**
 * Created by guofeng.qin on 2016/8/31.
 */
public interface ISessionManager extends IMsgDispatch {
    void push(String key, MessageOrBuilder msg);

    void pushAll(MessageOrBuilder msg);
}

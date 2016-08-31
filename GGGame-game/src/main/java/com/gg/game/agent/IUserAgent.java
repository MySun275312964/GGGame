package com.gg.game.agent;

import com.google.protobuf.MessageOrBuilder;

/**
 * Created by guofeng.qin on 2016/8/31.
 */
public interface IUserAgent {
    String getRoleId();

    void push(MessageOrBuilder msg);
}

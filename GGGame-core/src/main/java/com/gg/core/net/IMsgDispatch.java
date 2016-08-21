package com.gg.core.net;

import com.gg.core.net.codec.Net;
import com.google.protobuf.RpcCallback;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by guofeng.qin on 2016/8/19.
 */
public interface IMsgDispatch {
    void process(ChannelHandlerContext ctx, Net.Request request, RpcCallback<?> callback);
}

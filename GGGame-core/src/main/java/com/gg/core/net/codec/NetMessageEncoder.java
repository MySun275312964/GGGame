package com.gg.core.net.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by guofeng.qin on 2016/8/19.
 */
public class NetMessageEncoder extends MessageToMessageEncoder<Net.NetMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Net.NetMessage msg, List<Object> out) throws Exception {

    }
}

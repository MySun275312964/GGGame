package com.gg.core.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteOrder;

/**
 * Created by guofeng.qin on 2016/8/19.
 */
public class NetMessageEncoder extends MessageToByteEncoder<Net.NetMessage> {
    private ByteOrder byteOrder;

    public NetMessageEncoder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Net.NetMessage msg, ByteBuf out) throws Exception {
        out = out.order(byteOrder);
        // 消息类型，4 字节
        out.writeInt(msg.getType().getNumber());
        // 请求序号，2 字节
        out.writeShort(msg.getIndex());
        // 消息体
        byte[] payload = msg.getPayload().getBytes();
        if (payload != null) {
            out.writeBytes(payload);
        }
    }
}

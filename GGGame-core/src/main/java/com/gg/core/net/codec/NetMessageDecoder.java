package com.gg.core.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by guofeng.qin on 2016/8/19.
 */
public class NetMessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    private ByteOrder byteOrder;

    public NetMessageDecoder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.readableBytes() < 8) { // 长度小于附加控制信息长度，非法消息包，直接扔掉
            msg.release();
            return;
        }
        msg = msg.order(byteOrder);
        // 消息类型, 4 字节
        int type = msg.readInt();
        // 请求序号, 2 字节
        int index = msg.readUnsignedShort();
        // 消息体
        byte[] payload = msg.readBytes(msg.readableBytes()).array();
        Net.NetMessage netMessage = Net.NetMessage.newBuilder().setType(Net.MessageType.forNumber(type)).setIndex(index).setPayload(new String(payload)).build();
        out.add(netMessage);
    }
}

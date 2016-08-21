package com.gg.core.net;

import com.gg.common.StringUtils;
import com.gg.core.net.codec.Net;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.Channel;

/**
 * Created by hunter on 8/21/16.
 */
public class NetPBCallback<T> implements RpcCallback<T> {

    private Channel channel;
    private int index;

    public NetPBCallback(Channel channel, int index) {
        this.channel = channel;
        this.index = index;
    }

    @Override
    public void run(T parameter) {
        JsonFormat.Printer printer = JsonFormat.printer();
        Net.Response.Builder respBuilder = Net.Response.newBuilder();
        try {
            String name = parameter.getClass().getSimpleName();
            String jstr = printer.print((MessageOrBuilder) parameter);
            ByteString bytes = ByteString.copyFromUtf8(jstr);
            respBuilder.setName(name).setResult(bytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            String name = "SystemError";
            String msg = StringUtils.join("", "SystemError", e.getMessage());
            respBuilder.setName(name).setError(ByteString.copyFromUtf8(msg));
        }
        Net.Response resp = respBuilder.build();
        Net.NetMessage.Builder netMessageBuilder = Net.NetMessage.newBuilder();
        netMessageBuilder.setIndex(index);
        try {
            ByteString respBytes = ByteString.copyFromUtf8(printer.print(resp));
            netMessageBuilder.setType(Net.MessageType.RESPONSE);
            netMessageBuilder.setPayload(respBytes);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            netMessageBuilder.setType(Net.MessageType.ERROR);
            netMessageBuilder.setPayload(ByteString.EMPTY);
        }
        Net.NetMessage netMessage = netMessageBuilder.build();
        channel.writeAndFlush(netMessage);
    }

    public static final class Helper {
        public static void disconnect(Channel channel) {
            Net.NetMessage netMessage = Net.NetMessage.newBuilder().setType(Net.MessageType.DISCONNECT).setIndex(0).setPayload(ByteString.EMPTY).build();
            channel.writeAndFlush(netMessage).addListener((f) ->
                    channel.close()
            );
        }
    }
}

package com.gg.core.net;

import com.gg.common.GGLogger;
import com.gg.common.StringUtils;
import com.gg.core.net.codec.Net;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by hunter on 8/21/16.
 */
public class NetPBCallback<T> implements RpcCallback<T> {
    private static final GGLogger logger = GGLogger.getLogger(NetPBCallback.class);

    private Channel channel;
    private int index;
    private AtomicBoolean responsed = new AtomicBoolean(false);

    public NetPBCallback(Channel channel, int index) {
        this.channel = channel;
        this.index = index;
    }

    private boolean hasResponsed() {
        return responsed.get();
    }

    protected boolean doResponse() {
        return responsed.compareAndSet(false, true);
    }

    @Override
    public void run(T parameter) {
        if (doResponse()) {
            JsonFormat.Printer printer = JsonFormat.printer();
            Net.Response.Builder respBuilder = Net.Response.newBuilder();
            try {
                String name = parameter.getClass().getSimpleName();
                String jstr = printer.print((MessageOrBuilder) parameter);
                respBuilder.setName(name).setResult(jstr);
                logger.info("Resp: {}:{}.", name, jstr);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                String name = "SystemError";
                String msg = StringUtils.join("", "SystemError", e.getMessage());
                respBuilder.setName(name).setError(msg);
                logger.info("Error Resp: {}:{}.", name, msg);
            }
            Net.Response resp = respBuilder.build();
            Net.NetMessage.Builder netMessageBuilder = Net.NetMessage.newBuilder();
            netMessageBuilder.setIndex(index);
            try {
                netMessageBuilder.setType(Net.MessageType.RESPONSE);
                netMessageBuilder.setPayload(printer.print(resp));
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                netMessageBuilder.setType(Net.MessageType.ERROR);
                netMessageBuilder.setPayload(e.getMessage());
            }
            Net.NetMessage netMessage = netMessageBuilder.build();
            channel.writeAndFlush(netMessage);
        }
    }

    public static final class Helper {
        public static void disconnect(Channel channel) {
            Net.NetMessage netMessage = Net.NetMessage.newBuilder().setType(Net.MessageType.DISCONNECT).setIndex(0).build();
            channel.writeAndFlush(netMessage).addListener((f) ->
                    channel.close()
            );
        }
    }
}

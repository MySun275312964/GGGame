package com.gg.core.net;

import com.gg.common.Constants;
import com.gg.common.GGLogger;
import com.gg.core.net.codec.Net;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;

/**
 * Created by guofeng.qin on 2016/7/4.
 */
public class GameServerHandler extends SimpleChannelInboundHandler<Net.NetMessage> {
    private static final GGLogger logger = GGLogger.getLogger(GameServerHandler.class);

    private IMsgDispatch defaultDispatch;

    public GameServerHandler(IMsgDispatch dispatch) {
        this.defaultDispatch = dispatch;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Net.NetMessage msg) throws Exception {
        Channel channel = ctx.channel();
        Attribute<IMsgDispatch> attr = channel.attr(Constants.Net.DispatchKey);
        IMsgDispatch dispatch = attr.get();
        if (dispatch == null) {
            dispatch = defaultDispatch;
        }
        int index = msg.getIndex();
        switch (msg.getType()) {
            case CONNECT:
            case REQUEST:
                NetPBCallback callback = new NetPBCallback(ctx.channel(), index);
                Net.Request.Builder reqBuilder = Net.Request.newBuilder();
                JsonFormat.Parser jsonParser = JsonFormat.parser();
                logger.info("payload: {}.", msg.getPayload());
                String str = "{\"instance\":\"ISessionManager\",\"method\":\"connect\",\"payload\":\"{\\\"username\\\":\\\"testusername\\\",\\\"token\\\":\\\"testtoken\\\",\\\"extra\\\":null}\"}";
                logger.info("Payload equal: {}", (str.equals(msg.getPayload())));
                // jsonParser.merge(str, reqBuilder);
                jsonParser.merge(msg.getPayload(), reqBuilder);
                Net.Request request = reqBuilder.build();
                try {
                    dispatch.process(ctx, request, callback);
                } catch (Throwable throwable) {
                    logger.error("Error: ", throwable);
                    if (callback.doResponse()) {
                        Net.NetMessage netMessage = Net.NetMessage.newBuilder().setIndex(index).setType(Net.MessageType.ERROR).setPayload(throwable.getMessage()).build();
                        ctx.writeAndFlush(netMessage);
                    }
                }
                break;
            default:
                Net.NetMessage netMessage = Net.NetMessage.newBuilder().setIndex(index).setType(Net.MessageType.UNRECOGNIZED).build();
                ctx.writeAndFlush(netMessage);
                break;
        }
    }
    
    public static void main(String[] args) throws InvalidProtocolBufferException {
        String str = "{\"instance\":\"ISessionManager\",\"method\":\"connect\",\"payload\":\"{\\\"username\\\":\\\"testusername\\\",\\\"token\\\":\\\"testtoken\\\",\\\"extra\\\":null}\"}";
        Net.Request.Builder reqBuilder = Net.Request.newBuilder();
        JsonFormat.Parser jsonParser = JsonFormat.parser();
        logger.info("payload: {}.", str);
        jsonParser.merge(str, reqBuilder);
        Net.Request request = reqBuilder.build();
        System.out.println(request.getPayload());
    }
}

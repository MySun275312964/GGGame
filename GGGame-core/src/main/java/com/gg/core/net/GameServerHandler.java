package com.gg.core.net;

import com.gg.common.Constants;
import com.gg.common.GGLogger;
import com.gg.core.net.codec.Net;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;

/**
 * Created by guofeng.qin on 2016/7/4.
 */
public class GameServerHandler extends SimpleChannelInboundHandler<Net.Request> {
    private static final GGLogger logger = GGLogger.getLogger(GameServerHandler.class);

    private IMsgDispatch defaultDispatch;

    public GameServerHandler(IMsgDispatch dispatch) {
        this.defaultDispatch = dispatch;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Net.Request msg) throws Exception {
        Channel channel = ctx.channel();
        Attribute<IMsgDispatch> attr = channel.attr(Constants.Net.DispatchKey);
        IMsgDispatch dispatch = attr.get();
        if (dispatch == null) {
            dispatch = defaultDispatch;
        }
        dispatch.process(ctx, msg);
    }
}

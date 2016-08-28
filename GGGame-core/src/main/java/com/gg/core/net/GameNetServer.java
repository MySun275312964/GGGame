package com.gg.core.net;

import com.gg.core.net.codec.NetMessageDecoder;
import com.gg.core.net.codec.NetMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.ByteOrder;

import static com.gg.common.Constants.Net.DefaultHeadLength;
import static com.gg.common.Constants.Net.RPC_TIMEOUT;

/**
 * Created by guofeng.qin on 2016/7/4.
 */
public class GameNetServer {
    private GameNetConfig config;
    private IMsgDispatch defaultDispatch;

    public GameNetServer(GameNetConfig config, IMsgDispatch dispatch) {
        init(config, dispatch);
    }

    private void init(GameNetConfig config, IMsgDispatch dispatch) {
        this.config = config;
        this.defaultDispatch = dispatch;
    }

    public ChannelFuture bind(String host, int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(config.getParentGroup(), config.getChildGroup()).channel(config.getEvtLoopClass())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(RPC_TIMEOUT, 0, 0));
                        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(byteOrder, Integer.MAX_VALUE, 0, DefaultHeadLength, 0,
                                DefaultHeadLength, true));
                        pipeline.addLast(new NetMessageDecoder(byteOrder));
                        pipeline.addLast(new LengthFieldPrepender(byteOrder, DefaultHeadLength, 0, false));
                        pipeline.addLast(new NetMessageEncoder(byteOrder));
                        pipeline.addLast(new GameServerHandler(defaultDispatch));
                    }
                }).option(ChannelOption.SO_BACKLOG, 256).option(ChannelOption.SO_BACKLOG, 256)
                .option(ChannelOption.SO_REUSEADDR, true).option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return bootstrap.bind(host, port);
    }

}

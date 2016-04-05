package com.gg.gate;

import com.gg.gate.codec.GateDecoder;
import com.gg.gate.codec.GateEncoder;
import com.gg.gate.common.GateConst;
import com.gg.gate.metrics.BytesMetricsCollector;
import com.gg.gate.metrics.BytesMetricsHandler;
import com.gg.gate.metrics.MessageMetricsCollector;
import com.gg.gate.metrics.MessageMetricsHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author guofeng.qin
 */
public class GateServer {
	public ChannelFuture start(String host, int port, ChannelHandler handler) {
		GateTimeoutHandler timeoutHandler = new GateTimeoutHandler();
		BytesMetricsCollector bytesMetricsCollector = new BytesMetricsCollector();
		MessageMetricsCollector msgMetricsCollector = new MessageMetricsCollector();
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		bootstrap
			.group(group)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addFirst("idleEventHandler", new IdleStateHandler(0, 0, GateConst.DefaultConnectTimeout));
					pipeline.addAfter("idleStateHandler", "idleEventHandler", timeoutHandler); // 超时处理不经过编解码模块，如果需要经过的话需要移到编解码模块后面
					pipeline.addFirst("byteMetrics", new BytesMetricsHandler(bytesMetricsCollector));

					pipeline.addLast("decoder", new GateDecoder());
					pipeline.addLast("encoder", new GateEncoder());
					pipeline.addLast("messageMetrics", new MessageMetricsHandler(msgMetricsCollector));
					pipeline.addLast("handler", handler);
				}
			})
			.option(ChannelOption.SO_BACKLOG, 128)
			.option(ChannelOption.SO_REUSEADDR, true)
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_KEEPALIVE, true);
		
		return bootstrap.bind(host, port);
	}
}

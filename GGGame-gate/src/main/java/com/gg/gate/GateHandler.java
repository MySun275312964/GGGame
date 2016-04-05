package com.gg.gate;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author guofeng.qin
 */
@Component
@Sharable
public class GateHandler extends ChannelInboundHandlerAdapter {
	
}

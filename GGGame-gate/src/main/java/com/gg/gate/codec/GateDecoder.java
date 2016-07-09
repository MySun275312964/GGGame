package com.gg.gate.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author guofeng.qin
 */
public class GateDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(GateDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // logger.info(in.toString());
        logger.info("Readable: " + in.readableBytes());
        if (in.readableBytes() == 15) {
            // byte[] r = new byte[4];
            // ByteBuf b = in.readBytes(r);
            int i = in.readInt();
            logger.info("" + i);
        }
    }
}

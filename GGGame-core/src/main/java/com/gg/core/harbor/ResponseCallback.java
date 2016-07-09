package com.gg.core.harbor;

import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.MessageType;

/**
 *
 * @author guofeng.qin
 */
// TODO ... 每个请求会生成这个对象，考虑是否直接用Tunnel代替
public class ResponseCallback {

    private HarborStreamTunnel tunnel;
    private MessageType type;

    public ResponseCallback(MessageType type, HarborStreamTunnel tunnel) {
        this.type = type;
        this.tunnel = tunnel;
    }

    public boolean response(HarborMessage msg) {
        if (type != MessageType.Request) {
            throw new RuntimeException("can not response to a " + type.getValueDescriptor().getName());
        }
        return tunnel.sendToRemote(msg);
    }
}

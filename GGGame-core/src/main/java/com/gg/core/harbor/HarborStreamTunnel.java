package com.gg.core.harbor;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gg.common.KryoHelper;
import com.gg.core.harbor.protocol.HarborOuterClass.HandshakeMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.MessageType;
import com.gg.core.master.protocol.MasterOuterClass.MasterRegisterMessage;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

/**
 * @author guofeng.qin
 */
public class HarborStreamTunnel implements StreamObserver<HarborMessage> {
    private static final Logger logger = LoggerFactory.getLogger(HarborStreamTunnel.class);

    private StreamObserver<HarborMessage> destStream;
    private HarborDispatch dispatch;
    private String identity;
    private volatile boolean usable = true;
    private AtomicReference<Thread> Lock = new AtomicReference<>();

    public HarborStreamTunnel(HarborDispatch dispatch, StreamObserver<HarborMessage> dest) {
        this.dispatch = dispatch;
        this.destStream = dest;
    }

    public void setRemoteStream(String service, MasterRegisterMessage msg, HandshakeMessage handshake,
            StreamObserver<HarborMessage> destStream) {
        String key = msg.getHost() + ":" + msg.getPort();
        identity = key;
        setRemoteStream(service, key, handshake, destStream);
    }

    public void setRemoteStream(String service, HandshakeMessage handshake, StreamObserver<HarborMessage> destStream) {
        setRemoteStream(service, getKey(handshake), handshake, destStream);
    }

    private void setRemoteStream(String service, String key, HandshakeMessage handshake,
            StreamObserver<HarborMessage> destStream) {
        if (usable) {
            this.destStream = destStream;
            sendToRemote(HarborHelper.buildHarborMessage(MessageType.Handshake, handshake));
            dispatch.remoteHarborHandshake(service, key, this);
        }
    }

    @Override
    public void onCompleted() {
        usable = false;
        dispatch.onCompleted(identity);
    }

    @Override
    public void onError(Throwable error) {
        usable = false;
        dispatch.onError(identity, error);
    }

    private String getKey(HandshakeMessage handshake) {
        return handshake.getSource().getHost() + ":" + handshake.getSource().getPort();
    }

    private HandshakeMessage parseHandshakeMsg(ByteString payload) {
        return KryoHelper.readClassAndObject(payload.toByteArray());
    }

    @Override
    public void onNext(HarborMessage msg) {
        if (msg.getType() == MessageType.Handshake) {
            HandshakeMessage handshake = parseHandshakeMsg(msg.getPayload(0));
            if (handshake == null) {
                logger.error("handshake message decode error.");
                // TODO ... 握手错误处理
                // destStream.onError(new RuntimeException("handshake message
                // decode error."));
                return;
            }
            logger.info("receive handshake: " + handshake.getSource().getName() + ":" + handshake.getSource().getHost()
                    + ":" + handshake.getSource().getPort());
            identity = getKey(handshake);
            String sourceName = handshake.getSource().getName();
            dispatch.remoteHarborHandshake(sourceName, identity, this);
        } else {
            dispatch.onMessage(identity, msg);
        }
    }

    public boolean sendToRemote(HarborMessage msg) {
        if (usable && destStream != null) {
            Thread currentThread = Thread.currentThread();
            while (!Lock.compareAndSet(null, currentThread)) {
            } // spinlock
            try {
                destStream.onNext(msg);
            } finally {
                Lock.set(null); // unlock
            }
            return true;
        }
        dispatch.removeRemote(identity);
        return false;
    }
}

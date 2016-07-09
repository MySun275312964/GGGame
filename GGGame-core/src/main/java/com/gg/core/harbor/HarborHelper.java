package com.gg.core.harbor;

import java.util.ArrayList;
import java.util.List;

import com.gg.common.KryoHelper;
import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.MessageType;
import com.google.protobuf.ByteString;

/**
 * @author guofeng.qin
 */
public class HarborHelper {
    public static HarborMessage buildHarborMessage(MessageType type, Object... msg) {
        List<ByteString> payloads = new ArrayList<ByteString>();
        if (msg != null) {
            for (Object m : msg) {
                payloads.add(ByteString.copyFrom(KryoHelper.writeClassAndObject(m)));
            }
        }
        return HarborMessage.newBuilder().setType(type).addAllPayload(payloads).build();
    }

    public static HarborMessage buildHarborMessage2(MessageType type, int sid, int rid, String instance, String method,
            Object... msg) {
        List<ByteString> payloads = new ArrayList<ByteString>();
        if (msg != null) {
            for (Object m : msg) {
                payloads.add(ByteString.copyFrom(KryoHelper.writeClassAndObject(m)));
            }
        }
        return HarborMessage.newBuilder().setType(type).setSid(sid).setRid(rid).setSource(GGHarbor.getSelf())
                .setInstance(instance).setMethod(method).addAllPayload(payloads).build();
    }

    public static HarborMessage buildHarborResponse(int sid, int rid, Object... msg) {
        List<ByteString> payloads = new ArrayList<ByteString>();
        if (msg != null) {
            for (Object m : msg) {
                payloads.add(ByteString.copyFrom(KryoHelper.writeClassAndObject(m)));
            }
        }
        return HarborMessage.newBuilder().setType(MessageType.Response).setSid(sid).setRid(rid).addAllPayload(payloads)
                .build();
    }
}

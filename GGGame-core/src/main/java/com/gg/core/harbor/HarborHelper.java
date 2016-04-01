package com.gg.core.harbor;

import java.util.ArrayList;
import java.util.List;

import com.gg.common.JsonHelper;
import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.MessageType;

/**
 * @author guofeng.qin
 */
public class HarborHelper {
	public static HarborMessage buildHarborMessage(MessageType type, Object... msg) {
		List<String> payloads = new ArrayList<String>();
		if (msg != null) {
			for (Object m : msg) {
				payloads.add(JsonHelper.toJson(m));
			}
		}
		return HarborMessage.newBuilder().setType(type).addAllPayload(payloads).build();
	}

	public static HarborMessage buildHarborMessage2(MessageType type, int sid, int rid, String instance, String method,
			Object... msg) {
		List<String> payloads = new ArrayList<String>();
		if (msg != null) {
			for (Object m : msg) {
				payloads.add(JsonHelper.toJson(m));
			}
		}
		return HarborMessage.newBuilder().setType(type).setSid(sid).setRid(rid).setSource(GGHarbor.getSelf())
				.setInstance(instance).setMethod(method).addAllPayload(payloads).build();
	}
}

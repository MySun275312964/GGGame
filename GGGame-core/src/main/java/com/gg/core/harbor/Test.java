package com.gg.core.harbor;

import com.gg.common.JsonHelper;
import com.gg.core.harbor.protocol.HarborOuterClass.HandshakeMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.Service;

/**
 * @author guofeng.qin
 */
public class Test {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		Service s = Service.newBuilder().setHost("testhost").setPort(9999).build();
		HandshakeMessage hm = HandshakeMessage.newBuilder().setSource(s).build();

		String str = JsonHelper.toJson(hm);
		System.out.println(str);

		HandshakeMessage hm2 = JsonHelper.fromJson(str, HandshakeMessage.class);
		System.out.println(hm2);

	}
}

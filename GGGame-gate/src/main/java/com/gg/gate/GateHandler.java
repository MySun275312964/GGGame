package com.gg.gate;

import org.springframework.stereotype.Component;

import com.gg.core.harbor.IHarborHandler;
import com.gg.core.harbor.ResponseCallback;
import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;

/**
 * @author guofeng.qin
 */
@Component
public class GateHandler implements IHarborHandler {
	@Override
	public void onMessage(HarborMessage msg, ResponseCallback response) {
		
	}
}

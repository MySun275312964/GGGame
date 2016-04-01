package com.gg.core.harbor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.MessageType;

/**
 * @author guofeng.qin
 */
public class HarborDispatch {
	private AtomicInteger requestId = new AtomicInteger(0); // request id index
	private Map<Integer, HarborFutureTask> rmap = new ConcurrentHashMap<>();

	private Map<String, HarborStreamTunnel> harborMap = new HashMap<>();
	private Map<String, String> nameKeyMap = new HashMap<>();
	private Executor exepool;

	public HarborDispatch(Executor exepool) {
		this.exepool = exepool;
	}

	public void onCompleted(String identity) {
		// TODO ...
	}

	private void handleResponse(HarborMessage msg) {
		int rid = msg.getRid();
		HarborFutureTask future = rmap.get(rid);
		if (future != null) {
			if (future.isAsync()) { // 异步调用，需要把逻辑引导到exepool中执行
				exepool.execute(() -> {
					future.finish(msg.getPayloadList());
				}); 
			} else { // 同步调用，直接设置完成状态
				future.finish(msg.getPayloadList());
			}
		} else {
			// TODO ... 响应对应的请求找不到，如何处理
		}
	}
	
	private void handleMessage(HarborMessage msg) {
		// 反射调用对应的方法
	}

	public void onMessage(String identity, HarborMessage msg) {
		if (msg.getType() == MessageType.Response) { // handle response
			handleResponse(msg);
		} else {
			handleMessage(msg);
		}
	}

	public void onError(String identity, Throwable error) {
		// TODO ...
	}

	public void remoteHarborHandshake(String service, String key, HarborStreamTunnel tunnel) {
		if (harborMap.containsKey(key)) {
			// TODO ...
		}
		harborMap.put(key, tunnel);
		nameKeyMap.put(service, key);
	}

	public void post(String identity, HarborMessage msg) {
		harborMap.get(identity).sendToRemote(msg);
	}

	public void call(String service, HarborMessage msg, HarborFutureTask future) {
		int reqid = requestId.incrementAndGet();
		msg.toBuilder().setRid(reqid);
		rmap.put(reqid, future);
		post(nameKeyMap.get(service), msg);
	}
}

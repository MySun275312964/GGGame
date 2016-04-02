package com.gg.core.harbor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.ReflectionUtils;

import com.gg.common.JsonHelper;
import com.gg.common.StringUtil;
import com.gg.core.Async;
import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.MessageType;

/**
 * @author guofeng.qin
 */
public class HarborDispatch {
	private AtomicInteger requestId = new AtomicInteger(0); // request id index
	private Map<Integer, HarborFutureTask> rmap = new ConcurrentHashMap<>();
	private Map<String, Class<?>> instanceCacheMap = new ConcurrentHashMap<>();
	private Map<String, MethodEntry> methodCacheMap = new ConcurrentHashMap<>();
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
					future.finish(msg.getPayload(0));
				});
			} else { // 同步调用，直接设置完成状态
				future.finish(msg.getPayload(0));
			}
		} else {
			// TODO ... 响应对应的请求找不到，如何处理
		}
	}

	private MethodEntry getMethodWith(String instanceName, String methodName) {
		String tag = StringUtil.join(":", instanceName, methodName);
		MethodEntry methodEntry = methodCacheMap.get(tag);
		if (methodEntry == null) { // cache miss
			Class<?> instance = instanceCacheMap.get(instanceName);
			if (instance == null) {
				try {
					instance = Class.forName(instanceName);
					if (instance != null) {
						instanceCacheMap.put(instanceName, instance);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			if (instance != null) {
				Method ms[] = ReflectionUtils.getAllDeclaredMethods(instance);
				Method method = null;
				if (ms != null) {
					for (Method m : ms) {
						if (m.getName().equals(methodName)) {
							method = m;
							break;
						}
					}
				}
				if (method != null) {
					Object target = GGHarbor.getCtx().getBean(instance);
					ReflectionUtils.makeAccessible(method);
					methodEntry = new MethodEntry(method, target);
					methodCacheMap.put(tag, methodEntry);
				}

			}
		}

		return methodEntry;
	}

	private void invokeMethodInExepoll(Runnable runnable) {
		exepool.execute(runnable);
	}

	private void handleMessage(HarborMessage msg) {
		// TODO ... 异常处理
		// 反射调用对应的方法
		String instanceName = msg.getInstance();
		String methodName = msg.getMethod();
		MethodEntry methodEntry = getMethodWith(instanceName, methodName);
		if (methodEntry != null) {
			Method method = methodEntry.method;
			// 反序列化参数
			List<String> payloads = msg.getPayloadList();
			Class<?>[] ptypes = method.getParameterTypes();
			List<Object> params = new ArrayList<Object>();
			if (ptypes != null) {
				for (int i = 0; i < ptypes.length; i++) {
					Class<?> ptype = ptypes[i];
					String json = payloads.get(i);
					Object param = JsonHelper.fromJson(json, ptype);
					params.add(param);
				}
			}
			invokeMethodInExepoll(() -> {
				try {
					Object result = method.invoke(methodEntry.target, params.toArray(new Object[0]));
					if (msg.getType() == MessageType.Request) { // need response
						Async asyncs[] = method.getAnnotationsByType(Async.class);
						if (asyncs != null && asyncs.length > 0) { // async
																	// function
							HarborFutureTask future = (HarborFutureTask) result;
							future.addCallback((obj) -> {
								post(msg.getSource().getName(),
										HarborHelper.buildHarborResponse(msg.getSid(), msg.getRid(), obj));
							});
						} else { // normal function
							post(msg.getSource().getName(),
									HarborHelper.buildHarborResponse(msg.getSid(), msg.getRid(), result));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
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

	public void post(String service, HarborMessage msg) {
		harborMap.get(nameKeyMap.get(service)).sendToRemote(msg);
	}

	public void call(String service, HarborMessage msg, HarborFutureTask future) {
		int reqid = requestId.incrementAndGet();
		msg = msg.toBuilder().setRid(reqid).build();
		rmap.put(reqid, future);
		harborMap.get(nameKeyMap.get(service)).sendToRemote(msg);
	}

	static class MethodEntry {
		public Method method;
		public Object target;

		public MethodEntry(Method method, Object target) {
			this.method = method;
			this.target = target;
		}
	}
}

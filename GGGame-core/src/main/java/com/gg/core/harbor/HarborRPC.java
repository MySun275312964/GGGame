package com.gg.core.harbor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.gg.core.Async;
import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.MessageType;

/**
 * @author guofeng.qin
 */
public class HarborRPC implements InvocationHandler {

	private String service;

	public HarborRPC(String service) {
		this.service = service;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		HarborDispatch dispatch = GGHarbor.getDispatch();
		Async asyncs[] = method.getAnnotationsByType(Async.class);
		if (asyncs != null && asyncs.length > 0) { // async invoke
			Async async = asyncs[0];
			Class<?> clazz = async.clazz();
			HarborFutureTask future = HarborFutureTask.buildTask(clazz, true);
			HarborMessage msg = HarborHelper.buildHarborMessage2(MessageType.Request, 0, 0, service, method.getName(),
					args);
			dispatch.call(service, msg, future);
			return future;
		} else {
			Class<?> clazz = method.getReturnType();
			HarborFutureTask future = HarborFutureTask.buildTask(clazz, false);
			HarborMessage msg = HarborHelper.buildHarborMessage2(MessageType.Request, 0, 0, service, method.getName(),
					args);
			dispatch.call(service, msg, future);
			return future.get();
		}
	}

	public static <T> T getHarbor(String service, Class<T> clazz) {
		HarborRPC proxy = new HarborRPC(service);
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), proxy);
	}
}

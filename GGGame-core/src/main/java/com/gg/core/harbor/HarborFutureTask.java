package com.gg.core.harbor;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;

import com.gg.common.KryoHelper;

/**
 * @author guofeng.qin
 */
public class HarborFutureTask extends FutureTask<Object> {
	private HarborFutureCallable future;
	private boolean async;
	private Consumer<Object> callback;
	private Object Lock = new Object();

	private HarborFutureTask(Callable<Object> callable, boolean async) {
		super(callable);
		this.future = (HarborFutureCallable) callable;
		this.async = async;
	}

	public void remoteFinish(byte[] value) {
		Object obj = KryoHelper.readClassAndObject(value);
		doFinish(obj);
	}

	private void doFinish(Object obj) {
		// 同步调用, 直接finish
		if (!async) {
			future.finish(obj);
			run();
			return;
		}
		// 异步调用，已经设置了callback，直接调用callback
		if (callback != null) {
			callback.accept(obj);
			return;
		}
		// 异步调用，没有设置callback，可能会有资源竞争，加锁
		synchronized (Lock) {
			if (callback != null) { // double check
				callback.accept(obj);
				return;
			}
			future.finish(obj);
			run();
		}
	}

	public void finish(Object obj) {
		doFinish(obj);
	}

	public void addCallback(Consumer<Object> call) {
		if (isDone()) { // 已经返回了，则不用锁
			call.accept(future.getValue());
			return;
		}
		// 执行结果还没返回，加锁设置callback
		synchronized (Lock) {
			if (isDone()) { // double check
				call.accept(future.getValue());
			} else {
				callback = call;
			}
		}
	}

	public boolean isAsync() {
		return async;
	}

	public static HarborFutureTask buildTask(boolean async) {
		return new HarborFutureTask(new HarborFutureCallable(), async);
	}

	public static HarborFutureTask buildTask() {
		return new HarborFutureTask(new HarborFutureCallable(), true);
	}

	static class HarborFutureCallable implements Callable<Object> {
		public Object value;

		@Override
		public Object call() throws Exception {
			return value;
		}

		public void finish(Object value) {
			this.value = value;
		}

		public Object getValue() {
			return value;
		}
	}
}

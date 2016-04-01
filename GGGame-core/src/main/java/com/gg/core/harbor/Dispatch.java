//package com.gg.core.harbor;
//
//import java.lang.reflect.Method;
//
//import org.springframework.context.ApplicationContext;
//import org.springframework.util.ReflectionUtils;
//
//import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;
//
///**
// * @author guofeng.qin
// */
//public class Dispatch implements IHarborHandler {
//	private ApplicationContext ctx;
//
//	private Dispatch(ApplicationContext ctx) {
//		this.ctx = ctx;
//	}
//
//	private static class Holder {
//		private final static Dispatch instance = new Dispatch(null);
//	}
//
//	public static Dispatch getInstance() {
//		return Holder.instance;
//	}
//
//	@Override
//	public void onCompleted() {
//
//	}
//
//	@Override
//	public void onMessage(HarborMessage msg) {
//		Object instance = ctx.getBean(msg.getInstance());
//		if (instance != null) {
//			Method method = ReflectionUtils.findMethod(instance.getClass(), msg.getMethod());
//			if (method != null) {
//				// call method
//			}
//		}
//	}
//
//	@Override
//	public void onError(Throwable error) {
//
//	}
//
//	@Override
//	public void post(HarborMessage msg) {
//
//	}
//
//}

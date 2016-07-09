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
    private String instance;

    public HarborRPC(String service, String instance) {
        this.service = service;
        this.instance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HarborDispatch dispatch = GGHarbor.getDispatch();
        Async asyncs[] = method.getAnnotationsByType(Async.class);
        if (asyncs != null && asyncs.length > 0) { // async invoke
            HarborFutureTask future = HarborFutureTask.buildTask(true);
            HarborMessage msg =
                    HarborHelper.buildHarborMessage2(MessageType.Request, 0, 0, instance, method.getName(), args);
            dispatch.call(service, msg, future);
            return future;
        } else {
            Class<?> clazz = method.getReturnType();
            String rname = clazz.getName();
            HarborMessage msg =
                    HarborHelper.buildHarborMessage2(MessageType.Request, 0, 0, instance, method.getName(), args);
            if (rname.length() == 4 && "void".equalsIgnoreCase(rname)) {
                msg = msg.toBuilder().setType(MessageType.Post).build();
                dispatch.post(service, msg);
                return null;
            } else {
                HarborFutureTask future = HarborFutureTask.buildTask(false);
                dispatch.call(service, msg, future);
                return future.get();
            }
        }
    }

    public static <T> T getHarbor(String service, Class<T> clazz) {
        HarborRPC proxy = new HarborRPC(service, clazz.getName());
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, proxy);
    }
}

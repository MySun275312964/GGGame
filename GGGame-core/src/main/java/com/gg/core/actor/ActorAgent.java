package com.gg.core.actor;

import com.gg.core.actor.codec.RequestMessage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class ActorAgent implements InvocationHandler {
    private ActorBase self;
    private ActorRef remote;

    public ActorAgent(ActorRef remote) {
        this(null, remote);
    }

    public ActorAgent(ActorBase self, ActorRef remote) {
        this.self = self;
        this.remote = remote;
        if (remote == null) {
            throw new RuntimeException("ActorRef can not be null.");
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> rtnType = method.getReturnType();
        RequestMessage request = new RequestMessage(method.getName(), Helper.getMethodDesc(method), args);
        if (Void.TYPE.equals(rtnType)) {
            remote.tell(request, self.self());
            return null;
        } else {
            CompletableFuture<? super Object> future = new CompletableFuture<>();
            remote.ask(request, self, future);
            return future;
        }
    }

    public static <T> T getAgent(Class<?> targetClass, ActorBase self, ActorRef remoteRef) {
        ActorAgent agent = new ActorAgent(self, remoteRef);
        return (T) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class<?>[] {targetClass}, agent);
    }
}

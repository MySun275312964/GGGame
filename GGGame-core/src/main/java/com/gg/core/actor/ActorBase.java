package com.gg.core.actor;

import com.gg.common.JsonHelper;
import com.gg.core.actor.codec.ActorMessage;
import com.gg.core.actor.codec.RequestMessage;
import com.gg.core.actor.codec.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public abstract class ActorBase implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ActorBase.class);

    private Queue<ActorMessage> messages = new ConcurrentLinkedQueue<>();
    private Map<Integer, BiConsumer<? super Object, ? super Throwable>> callbackMap = new ConcurrentHashMap<>();

    private AtomicBoolean dispatching = new AtomicBoolean(false); // true if is in dispatching

    private Map<String, Method> methodMap = new HashMap<>();

    private ActorSystem system;

    private ActorRef selfRef;

    private ActorIdentity identity;

    public ActorBase(ActorSystem system) {
        this.system = system;
        initMethodMap();
    }

    private boolean intoDispatching() {
        return dispatching.compareAndSet(false, true);
    }

    private boolean offDispatching() {
        return dispatching.compareAndSet(true, false);
    }

    private boolean isDispatching() {
        return dispatching.get();
    }

    private void initMethodMap() {
        Method[] methods = getClass().getDeclaredMethods();
        if (methods != null) {
            for (Method method : methods) {
                method.setAccessible(true);
                String desc = Helper.getMethodDesc(method);
                methodMap.put(desc, method);
            }
        }

    }

    private Method getMethod(String desc) {
        return methodMap.get(desc);
    }

    @Override
    public void run() {
        try {
            while (!messages.isEmpty()) {
                ActorMessage msg = messages.remove();
                int msgType = msg.getType();
                if (msgType == ActorSystem.TypeRequest) {
                    RequestMessage request = (RequestMessage) msg.getMsg();
                    Method method = getMethod(request.getArgsDesc());
                    if (method != null) {
                        boolean hasReturn = !method.getReturnType().equals(Void.TYPE);
                        try {
                            Object rtn = method.invoke(this, request.getArgs());
                            if (hasReturn) {
                                CompletableFuture<?> future = (CompletableFuture<?>) rtn;
                                // 这里不需要调度到特定线程执行, 因为发送消息操作是线程安全的
                                future.whenComplete((r, e) -> {
                                    ResponseMessage resp = new ResponseMessage(r, e);
                                    system.sendResponseMessageTo(identity.getId(), msg, resp);
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            ResponseMessage resp = new ResponseMessage(null, e);
                            system.sendResponseMessageTo(identity.getId(), msg, resp);
                        }
                    } else {
                        String errorMsg = "Method not found. " + JsonHelper.toJson(request);
                        logger.error(errorMsg);
                        ResponseMessage resp = new ResponseMessage(null, new RuntimeException(errorMsg));
                        system.sendResponseMessageTo(identity.getId(), msg, resp);
                    }
                }
                if (msgType == ActorSystem.TypeResponse) {
                    int sid = msg.getSid();
                    ResponseMessage resp = (ResponseMessage) msg.getMsg();
                    BiConsumer<? super Object, ? super Throwable> callback = callbackMap.remove(sid);
                    if (callback != null) {
                        if (resp != null) {
                            callback.accept(resp.getResult(), resp.getError());
                        } else {
                            logger.error("no response");
                            callback.accept(null, new RuntimeException("Runtime Error."));
                        }
                    } else {
                        logger.warn("no callback with sid {}, {}", sid, JsonHelper.toJson(msg));
                    }
                }
            }
        } finally {
            offDispatching();
        }
    }

    protected void post(ActorMessage msg) {
        messages.add(msg);
        if (!isDispatching() && !messages.isEmpty()) {
            if (intoDispatching()) {
                try {
                    system.emit(this);
                } catch (Throwable e) {
                    logger.error("ActorSystem emit error.");
                    offDispatching();
                    throw e;
                }
            }
        }
    }

    protected void addCallback(int sid, BiConsumer<? super Object, ? super Throwable> callback) {
        callbackMap.put(sid, callback);
    }

    public ActorRef self() {
        return selfRef;
    }

    public void setIdentity(ActorIdentity identity, ActorRef selfRef) {
        this.identity = identity;
        this.selfRef = selfRef;
    }

    public int getQueueSize() {
        return messages.size();
    }
}

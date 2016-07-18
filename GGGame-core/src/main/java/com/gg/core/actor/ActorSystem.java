package com.gg.core.actor;


import com.gg.core.actor.codec.ActorMessage;
import com.gg.core.actor.codec.Message;
import com.gg.core.actor.codec.RequestMessage;
import com.gg.core.actor.codec.ResponseMessage;
import com.gg.core.actor.harbor.ActorHarbor;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class ActorSystem {
    private static final Logger logger = LoggerFactory.getLogger(ActorSystem.class);

    public static final int TypeRequest = 1;
    public static final int TypeResponse = 2;

    private String name = null;

    private ForkJoinPool workPool = null;
    // private Executor workPool = null;

    private Map<Integer, ActorBase> actorMap = new ConcurrentHashMap<>();
    private Map<String, Integer> actorNameReverseMap = new ConcurrentHashMap<>();
    private AtomicInteger actorIndex = new AtomicInteger(0);
    private AtomicInteger sessionIndex = new AtomicInteger(0);

    private ActorHarbor harbor;

    public ActorSystem(String name) {
        this(name, Runtime.getRuntime().availableProcessors());
    }

    public void setHarbor(ActorHarbor harbor) {
        this.harbor = harbor;
    }

    public ActorSystem(String name, int parallelism) {
        this.name = name;
        workPool = new ForkJoinPool(parallelism);
        // workPool = Executors.newSingleThreadExecutor();
    }

    public void emit(ActorBase actor) {
        workPool.execute(actor);
    }

    public ActorRef actor(String name, ActorBase actor) {
        if (actorNameReverseMap.containsKey(name)) {
            throw new RuntimeException("Actor Already Exist with name " + name);
        }

        int index = actorIndex.incrementAndGet();
        ActorRef ref = new ActorRef(index, this);
        actor.setIdentity(new ActorIdentity(index, name), ref);

        actorNameReverseMap.put(name, index);
        actorMap.put(index, actor);

        return ref;
    }

    public ActorRef actor(String name) {
        // FIXME ... 需不需要加缓存
        Integer atrId = actorNameReverseMap.get(name);
        if (atrId == null) {
            throw new RuntimeException("no actor named " + name);
        }
        ActorRef ref = new ActorRef(atrId, this);
        return ref;
    }

    public ActorRef remoteActor(String systemName, String name) {
        // TODO ... ActorRef 需不需要系统缓存起来?
        ActorRef actorRef = new ActorRef(this, systemName, name, false);
        return actorRef;
    }

    private int sendMessageTo(int sender, int receiver, int type, Message msg) {
        int sid = sessionIndex.incrementAndGet();
        return sendMessageTo(sender, receiver, type, sid, msg);
    }

    private int sendMessageTo(int sender, int receiver, int type, int sid, Message msg) {
        ActorBase actor = actorMap.get(receiver);
        if (actor != null) {
            ActorMessage actorMsg = new ActorMessage(sid, receiver, sender, type, msg);
            actor.post(actorMsg);
            return sid;
        } else {
            logger.error("{}, no actor with id {}.", sender, receiver);
            return 0;
        }
    }

    public int sendRequestMessageTo(ActorRef sender, ActorRef receiver, RequestMessage msg) {
        return sendMessageTo(sender.getId(), receiver.getId(), TypeRequest, msg);
    }

    private int sendResponseMessageTo(int sender, int receiver, int sid, ResponseMessage msg) {
        return sendMessageTo(sender, receiver, TypeResponse, sid, msg);
    }

    public int sendResponseMessageTo(int sender, ActorMessage sourceMsg, ResponseMessage msg) {
        String sourceSenderSystem = sourceMsg.getSenderSystem();
        if (!StringUtil.isNullOrEmpty(sourceSenderSystem) && !name.equals(sourceSenderSystem)) { // response to remote
            return sendRemoteResponseMessageTo(sender, sourceMsg.getSenderSystem(), null, sourceMsg.getSender(), sourceMsg.getSid(), msg);
        } else {
            return sendResponseMessageTo(sender, sourceMsg.getSender(), sourceMsg.getSid(), msg);
        }
    }

    private void checkHarbor() {
        if (harbor == null) {
            throw new RuntimeException("Harbor can't be null.");
        }
    }

    private int sendRemoteMessageTo(int sender, String recvSystem, String receiver, int receiverId, int type, int sid, Message msg) {
        checkHarbor();
        ActorMessage atrMsg = new ActorMessage(sid, recvSystem, receiver, receiverId, sender, type, msg);
        harbor.send(atrMsg);
        return sid;
    }

    private int sendRemoteMessageTo(int sender, String recvSystem, String receiver, int receiverId, int type, Message msg) {
        int sid = sessionIndex.incrementAndGet();
        return sendRemoteMessageTo(sender, recvSystem, receiver, receiverId, type, sid, msg);
    }

    public int sendRemoteRequestMessageTo(int sender, String recvSystem, String receiver, int receiverId, Message msg) {
        return sendRemoteMessageTo(sender, recvSystem, receiver, receiverId, TypeRequest, msg);
    }

    public int sendRemoteResponseMessageTo(int sender, String recvSystem, String receiver, int receiverId, int sid, Message msg) {
        return sendRemoteMessageTo(sender, recvSystem, receiver, receiverId, TypeResponse, sid, msg);
    }

    public int forward(ActorMessage msg) {
        ActorBase actor = null;
        if (msg.getReceiver() <= 0) {
            actor = actorMap.get(actorNameReverseMap.get(msg.getReceiverName()));
        } else {
            actor = actorMap.get(msg.getReceiver());
        }
        if (actor != null) {
            actor.post(msg);
        } else {
            // TODO ...
            logger.error("Actor Not Exist.");
        }
        return msg.getSid();
    }

    public String getName() {
        return name;
    }
}

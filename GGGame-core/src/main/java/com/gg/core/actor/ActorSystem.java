package com.gg.core.actor;


import com.gg.core.actor.codec.ActorMessage;
import com.gg.core.actor.codec.Message;
import com.gg.core.actor.codec.RequestMessage;
import com.gg.core.actor.codec.ResponseMessage;
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

    private Map<Integer, ActorBase> actorMap = new ConcurrentHashMap<>();
    private Map<String, Integer> actorNameReverseMap = new ConcurrentHashMap<>();
    private AtomicInteger actorIndex = new AtomicInteger(0);
    private AtomicInteger sessionIndex = new AtomicInteger(0);

    public ActorSystem(String name) {
        this(name, Runtime.getRuntime().availableProcessors());
    }

    public ActorSystem(String name, int parallelism) {
        this.name = name;
        workPool = new ForkJoinPool(parallelism);
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

    public int sendMessageTo(int sender, int receiver, int type, Message msg) {
        int sid = sessionIndex.incrementAndGet();
        return sendMessageTo(sender, receiver, type, sid, msg);
    }

    public int sendMessageTo(int sender, int receiver, int type, int sid, Message msg) {
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

    public int sendResponseMessageTo(int sender, int receiver, int sid, ResponseMessage msg) {
        return sendMessageTo(sender, receiver, TypeResponse, sid, msg);
    }
}

package com.gg.core.actor;

import com.gg.core.actor.codec.RequestMessage;

import java.util.concurrent.CompletableFuture;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class ActorRef {
    private int id;
    private ActorSystem system;

    public ActorRef() {}

    public ActorRef(int id) {
        this.id = id;
    }

    public ActorRef(int id, ActorSystem system) {
        this.id = id;
        this.system = system;
    }

    public int getId() {
        return id;
    }

    public void setSystem(ActorSystem system) {
        this.system = system;
    }

    public int tell(RequestMessage msg, ActorRef sender) {
        return system.sendRequestMessageTo(sender, this, msg);
    }

    public void ask(RequestMessage msg, ActorBase sender, CompletableFuture<? super Object> future) {
        int sid = tell(msg, sender.self());
        sender.addCallback(sid, (r, e) -> {
            if (e != null) {
                future.completeExceptionally(e);
            } else {
                future.complete(r);
            }
        });
    }
}

package com.gg.core.actor;

import com.gg.core.actor.codec.RequestMessage;

import java.util.concurrent.CompletableFuture;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class ActorRef {
    private String systemName;
    private String name;
    private int id;
    private boolean local = true;
    private ActorSystem system;

    public ActorRef() {}

    public ActorRef(int id) {
        this.id = id;
    }

    public ActorRef(ActorSystem system, String systemName, String name, boolean local) {
        this.system = system;
        this.systemName = systemName;
        this.name = name;
        this.local = local;
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
        if (local) {
            return system.sendRequestMessageTo(sender, this, msg);
        } else {
            return system.sendRemoteRequestMessageTo(sender.getId(), systemName, name, id, msg);
        }
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

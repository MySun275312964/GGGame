package com.gg.core.actor;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class ActorIdentity {
    private int id;
    private String name;

    public ActorIdentity() {}

    public ActorIdentity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

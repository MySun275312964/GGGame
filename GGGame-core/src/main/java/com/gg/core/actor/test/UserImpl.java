package com.gg.core.actor.test;

import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class UserImpl extends ActorBase implements IUser {
    private Map<Integer, String> nameMap = new HashMap<>();

    public UserImpl(ActorSystem system) {
        super(system);
    }

    @Override
    public void addUser(Integer id, String name, Integer age) {
        nameMap.put(id, name);
    }

    @Override
    public CompletableFuture<String> getUserName(int id) {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.complete(nameMap.get(id));
        return future;
    }
}

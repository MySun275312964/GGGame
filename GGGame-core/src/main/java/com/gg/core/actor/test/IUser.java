package com.gg.core.actor.test;

import java.util.concurrent.CompletableFuture;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public interface IUser {
    void addUser(Integer id, String name, Integer age);

    CompletableFuture<String> getUserName(int id);
}

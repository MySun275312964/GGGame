package com.gg.core.actor.test;

import com.gg.common.Constants;
import com.gg.core.actor.ActorRef;
import com.gg.core.actor.ActorSystem;
import com.gg.core.actor.harbor.ActorHarbor;

import java.io.IOException;

/**
 * Created by guofeng.qin on 2016/7/16.
 */
public class UserApp {
    public static void main(String[] args) throws IOException {
        ActorSystem system = new ActorSystem("usersystem");
        ActorHarbor harbor = new ActorHarbor(system, "127.0.0.1", 16968, Constants.Localhost, Constants.MasterPort);

        system.setHarbor(harbor);

        UserImpl test = new UserImpl(system);
        ActorRef userRef = system.actor("user", test);

        System.in.read();
    }
}

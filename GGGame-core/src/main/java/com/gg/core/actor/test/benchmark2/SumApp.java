package com.gg.core.actor.test.benchmark2;

import com.gg.common.Constants;
import com.gg.core.actor.ActorSystem;
import com.gg.core.actor.harbor.ActorHarbor;

import java.io.IOException;

/**
 * Created by guofeng.qin on 2016/7/18.
 */
public class SumApp {
    public static void main(String[] args) throws IOException {
        ActorSystem system = new ActorSystem("system-sum", 1);
        ActorHarbor harbor = new ActorHarbor(system, "127.0.0.1", 1987, "127.0.0.1", Constants.MasterPort);

        system.setHarbor(harbor);

        com.gg.core.actor.test.benchmark2.SumActor sum = new SumActor(system);

        system.actor("sum", sum);

        System.in.read();
    }
}

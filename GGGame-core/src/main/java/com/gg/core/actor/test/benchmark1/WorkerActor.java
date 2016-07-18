package com.gg.core.actor.test.benchmark1;

import com.gg.core.actor.ActorAgent;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;

/**
 * Created by guofeng.qin on 2016/7/18.
 */
public class WorkerActor extends ActorBase implements IWorker {

    private ISum sumAgent;

    public WorkerActor(ActorSystem system) {
        super(system);
        sumAgent = ActorAgent.getAgent(ISum.class, this, system.actor("sum"));
    }

    @Override
    public void doWork(String str) {
        sumAgent.putResult(str);
    }
}

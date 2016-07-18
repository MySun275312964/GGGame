package com.gg.core.actor.test.benchmark2;

import com.gg.common.Constants;
import com.gg.core.actor.ActorAgent;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorRef;
import com.gg.core.actor.ActorSystem;
import com.gg.core.actor.harbor.ActorHarbor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guofeng.qin on 2016/7/18.
 */
public class Benchmark extends ActorBase implements IBenchmark {

    private List<IWorker> workerList = new ArrayList<>();

    public Benchmark(ActorSystem system) {
        super(system);
    }

    public static void main(String[] args) throws IOException {
        ActorSystem system = new ActorSystem("Benchmark");
        ActorHarbor harbor = new ActorHarbor(system, Constants.Localhost, 1986, Constants.Localhost, Constants.MasterPort);
        system.setHarbor(harbor);

        Benchmark bench = new Benchmark(system);
        ActorRef benchRef = system.actor("bench", bench);
        IBenchmark benchAgent = ActorAgent.getAgent(IBenchmark.class, bench, benchRef);
        benchAgent.init();
        benchAgent.start();

        System.in.read();
    }

    @Override
    public void init() {
        System.out.println("InitThread: " + Thread.currentThread().getId());
        for (int i = 0; i < 8; i++) {
            WorkerActor worker = new WorkerActor(system);
            ActorRef workerRef = system.actor("worker-" + i, worker);
            IWorker workerAgent = ActorAgent.getAgent(IWorker.class, this, workerRef);
            workerList.add(workerAgent);
        }
    }

    @Override
    public void start() {
        ISum sumAgent = ActorAgent.getAgent(ISum.class, this, system.remoteActor("system-sum", "sum"));
        sumAgent.start();
        System.out.println("StartThread: " + Thread.currentThread().getId());
        for (int i = 0; i < SumActor.SUM; i++) {
            workerList.get(i & 7).doWork(i + "");
        }
    }
}

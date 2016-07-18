package com.gg.core.actor.test.benchmark1;

import com.gg.core.actor.ActorAgent;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorRef;
import com.gg.core.actor.ActorSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guofeng.qin on 2016/7/18.
 */
public class Benchmark extends ActorBase implements IBenchmark {

    private List<IWorker> workerList = new ArrayList<>();

    private ISum sumAgent;

    public Benchmark(ActorSystem system) {
        super(system);
    }

    public static void main(String[] args) throws IOException {
        ActorSystem system = new ActorSystem("Benchmark");

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
        SumActor sum = new SumActor(system);
        ActorRef sumRef = system.actor("sum", sum);
        sumAgent = ActorAgent.getAgent(ISum.class, this, sumRef);
        for (int i = 0; i < 8; i++) {
            WorkerActor worker = new WorkerActor(system);
            ActorRef workerRef = system.actor("worker-" + i, worker);
            IWorker workerAgent = ActorAgent.getAgent(IWorker.class, this, workerRef);
            workerList.add(workerAgent);
        }
    }

    @Override
    public void start() {
        System.out.println("StartThread: " + Thread.currentThread().getId());
        for (int i = 0; i < SumActor.SUM; i++) {
            workerList.get(i & 7).doWork(i + "");
        }
    }
}

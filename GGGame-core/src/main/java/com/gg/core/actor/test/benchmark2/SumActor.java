package com.gg.core.actor.test.benchmark2;

import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;

/**
 * Created by guofeng.qin on 2016/7/18.
 */
public class SumActor extends ActorBase implements ISum {

    // public static final int SUM = 10000000;
    public static final int SUM = 1000000;

    private long start;

    public SumActor(ActorSystem system) {
        super(system);
    }

    private int total = 0;


    @Override
    public void putResult(String res) {
        total += 1;
        if (total == SUM) {
            long end = System.currentTimeMillis();
            System.out.println("End..." + (end - start));
        }
    }

    @Override
    public void start() {
        start = System.currentTimeMillis();
    }
}

package com.gg.core.actor.test;

import com.gg.common.Constants;
import com.gg.core.actor.ActorAgent;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorRef;
import com.gg.core.actor.ActorSystem;
import com.gg.core.actor.harbor.ActorHarbor;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static com.gg.core.actor.ActorAgent.getAgent;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class Test extends ActorBase implements ITest {

    public Test(ActorSystem system) {
        super(system);
    }

    public void test(ActorRef userRef) {
        IUser user = getAgent(IUser.class, this, userRef);
        user.addUser(100, "老王头儿", 10);
        long start = System.currentTimeMillis();
        CompletableFuture<String> userNameFuture = user.getUserName(100);
        userNameFuture.whenComplete((r, e) -> {
            if (e != null) {
                System.out.println("Error...");
                e.printStackTrace();
            } else {
                System.out.println("Name is " + r);
                long end = System.currentTimeMillis();
                System.out.println("Total: " + (end - start));
                long start2 = System.currentTimeMillis();
                CompletableFuture<String> userNameFuture2 = user.getUserName(100);
                userNameFuture2.whenComplete((r2, e2) -> {
                    if (e2 != null) {
                        System.out.println("Error...");
                        e.printStackTrace();
                    } else {
                        System.out.println("Name is " + r);
                        long end2 = System.currentTimeMillis();
                        System.out.println("Total2: " + (end2 - start2));
                    }
                });
            }
        });
    }

    public static void main(String[] args) throws IOException {
        ActorSystem system = new ActorSystem("ActorTestSystem");
        ActorHarbor harbor = new ActorHarbor(system, "127.0.0.1", 16969, Constants.Localhost, Constants.MasterPort);

        system.setHarbor(harbor);

        Test test = new Test(system);
        ActorRef testRef = system.actor("test", test);

        ActorRef userRef = system.remoteActor("usersystem", "user");

        System.in.read();

        ITest it = ActorAgent.getAgent(ITest.class, test, testRef);

        it.startTest(userRef);

        System.in.read();
    }

    @Override
    public void startTest(ActorRef user) {
        test(user);
    }
}

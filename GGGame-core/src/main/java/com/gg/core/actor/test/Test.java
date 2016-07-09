package com.gg.core.actor.test;

import com.gg.core.actor.ActorAgent;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorRef;
import com.gg.core.actor.ActorSystem;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class Test extends ActorBase {

    public Test(ActorSystem system) {
        super(system);
    }

    public void test(ActorRef userRef) {
        IUser user = ActorAgent.getAgent(IUser.class, this, userRef);
        user.addUser(100, "老王头儿", 10);
        CompletableFuture<String> userNameFuture = user.getUserName(100);
        userNameFuture.whenComplete((r, e) -> {
            if (e != null) {
                System.out.println("Error...");
                e.printStackTrace();
            } else {
                System.out.println("Name is " + r);
            }
        });
    }

    public static void main(String[] args) throws IOException {
        ActorSystem system = new ActorSystem("ActorTestSystem");
        Test test = new Test(system);
        ActorRef testRef = system.actor("test", test);

        UserImpl user = new UserImpl(system);
        ActorRef userRef = system.actor("user", user);

        test.test(userRef);

        System.in.read();
    }
}

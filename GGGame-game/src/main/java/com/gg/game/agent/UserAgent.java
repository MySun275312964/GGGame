package com.gg.game.agent;

import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;
import com.gg.core.net.IMsgDispatch;
import com.gg.core.net.codec.Net;
import com.google.protobuf.RpcCallback;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by hunter on 2016/8/21.
 */
public class UserAgent extends ActorBase implements IMsgDispatch {

    private static List<Consumer<UserAgent>> registryRunners = new ArrayList<>();

    public UserAgent(ActorSystem system) {
        super(system);
        init();
    }

    private void init() {
        for (Consumer<UserAgent> runner : registryRunners) {
            runner.accept(this);
        }
    }

    // 業務類註冊自己的初始化進來
    public static void addRegistryRunners (Consumer<UserAgent> runner) {
        registryRunners.add(runner);
    }

    @Override
    public void process(ChannelHandlerContext ctx, Net.Request request, RpcCallback<?> callback) {

    }
}

package com.gg.game;

import com.gg.common.GGLogger;
import com.gg.core.actor.ActorSystem;
import com.gg.core.net.GameNetConfig;
import com.gg.core.net.GameNetServer;
import com.gg.core.net.IMsgDispatch;
import com.gg.game.agent.UserAgent;
import com.gg.game.agent.impl.RoomImpl;
import com.gg.game.agent.impl.TestImpl;
import com.gg.game.session.SessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Created by hunter on 8/20/16.
 */
public class GGGameApp {
    private static final GGLogger logger = GGLogger.getLogger(GGGameApp.class);

    private static ActorSystem actorSystem;

    public static ActorSystem getActorSystem() {
        return actorSystem;
    }

    /**
     * 注册函数
     */
    private static void registMethods() {
        UserAgent.addRegistryRunners(agent -> agent.addFunc("ITest", new TestImpl()));
        UserAgent.addRegistryRunners(agent -> agent.addFunc("IRoom", new RoomImpl(agent.getRoleId())));
    }

    public static void main(String[] args) throws InterruptedException {
        int coreNum = Runtime.getRuntime().availableProcessors();

        GameNetConfig netConfig = GameNetConfig.getInstance();
        netConfig.init(1, 1, false);

        actorSystem = new ActorSystem("GGGame", Math.max(1, coreNum - 2));

        IMsgDispatch sessionMsgDispatch = SessionManager.getInstance();

        registMethods();

        GameNetServer server = new GameNetServer(netConfig, sessionMsgDispatch);

        ChannelFuture channelFuture = server.bind("0.0.0.0", 29999);

        Channel channel = channelFuture.sync().channel();

        logger.info("server start ...");

        channel.closeFuture().awaitUninterruptibly();
    }
}

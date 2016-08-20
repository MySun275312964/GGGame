package com.gg.game;

import com.gg.common.GGLogger;
import com.gg.core.actor.ActorAgent;
import com.gg.core.actor.ActorRef;
import com.gg.core.actor.ActorSystem;
import com.gg.core.net.GameNetConfig;
import com.gg.core.net.GameNetServer;
import com.gg.core.net.IMsgDispatch;
import com.gg.game.session.SessionManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Created by hunter on 8/20/16.
 */
public class GGGameApp {
    private static final GGLogger logger = GGLogger.getLogger(GGGameApp.class);

    public static void main(String[] args) throws InterruptedException {
        int coreNum = Runtime.getRuntime().availableProcessors();

        GameNetConfig netConfig = GameNetConfig.getInstance();
        netConfig.init(1, 1, false);

        ActorSystem actorSystem = new ActorSystem("GGGame", Math.max(1, coreNum - 2));

        SessionManager sessionManager = new SessionManager(actorSystem);
        ActorRef sessionManagerRef = actorSystem.actor("SessionManager", sessionManager);
        
        IMsgDispatch sessionMsgDispatch = ActorAgent.getAgent(IMsgDispatch.class, actorSystem.getSystemActor(), sessionManagerRef);

        GameNetServer server = new GameNetServer(netConfig, sessionMsgDispatch);

        ChannelFuture channelFuture = server.bind("0.0.0.0", 19999);

        Channel channel = channelFuture.sync().channel();

        logger.info("server start ...");

        channel.closeFuture().awaitUninterruptibly();
    }
}

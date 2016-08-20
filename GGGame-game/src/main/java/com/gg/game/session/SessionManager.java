package com.gg.game.session;

import com.gg.common.GGLogger;
import com.gg.common.JsonHelper;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;
import com.gg.core.net.IMsgDispatch;
import com.gg.core.net.codec.Net;
import com.gg.game.proto.GameProto;
import com.gg.game.proto.GameProto.ISessionManager;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by hunter on 8/20/16.
 */
public class SessionManager extends ActorBase implements IMsgDispatch {
    private static final GGLogger logger = GGLogger.getLogger(SessionManager.class);

    private InnerSessionManager sessionManager;

    public SessionManager(ActorSystem system) {
        super(system);
        sessionManager = new InnerSessionManager();
    }

    @Override
    public void process(ChannelHandlerContext ctx, Net.Request request) {
        logger.info("SessionManager process: {}.", JsonHelper.toJson(request));

    }

    private static final class InnerSessionManager extends ISessionManager {
        @Override
        public void connect(RpcController controller, GameProto.ConnectRequest request, RpcCallback<GameProto.ConnectResponse> done) {

        }
    }
}

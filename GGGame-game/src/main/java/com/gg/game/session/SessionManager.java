package com.gg.game.session;

import com.gg.common.GGLogger;
import com.gg.common.JsonHelper;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;
import com.gg.core.net.IMsgDispatch;
import com.gg.core.net.NetPBCallback;
import com.gg.core.net.NetPBHelper;
import com.gg.core.net.codec.Net;
import com.gg.game.proto.PSessionManager;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by hunter on 8/20/16.
 */
public class SessionManager extends ActorBase implements IMsgDispatch {
    private static final GGLogger logger = GGLogger.getLogger(SessionManager.class);

    private static final String InstanceName = "ISessionManager";
    private static final String MethodName = "connect";

    private InnerSessionManager sessionManager;

    public SessionManager(ActorSystem system) {
        super(system);
        sessionManager = new InnerSessionManager();
    }

    @Override
    public void process(ChannelHandlerContext ctx, Net.Request request, RpcCallback callback) {
        logger.info("SessionManager process: {}.", JsonHelper.toJson(request));
        if (!InstanceName.equals(request.getInstance()) || !MethodName.equals(request.getMethod())) {
            logger.warn("wrong request, must send {}.{} first.", InstanceName, MethodName);
            NetPBCallback.Helper.disconnect(ctx.channel());
            return;
        }

        // parse ConnectRequest
        PSessionManager.ConnectRequest.Builder builder = PSessionManager.ConnectRequest.newBuilder();
        NetPBHelper.parseJson(request.getPayload().toStringUtf8(), builder);
        PSessionManager.ConnectRequest connectRequest = builder.build();

        sessionManager.connect(null, connectRequest, callback);
    }

    private static final class InnerSessionManager extends PSessionManager.ISessionManager {
        @Override
        public void connect(RpcController controller, PSessionManager.ConnectRequest request,
                RpcCallback<PSessionManager.ConnectResponse> done) {
            PSessionManager.ConnectResponse resp =
                    PSessionManager.ConnectResponse.newBuilder().setCode(0).setMsg("success").build();
            done.run(resp);
        }
    }
}

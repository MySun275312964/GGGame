package com.gg.core.actor.harbor;

import com.gg.common.GGLogger;
import com.gg.common.KryoHelper;
import com.gg.core.actor.ActorSystem;
import com.gg.core.actor.codec.ActorMessage;
import com.gg.core.actor.harbor.protocol.ActorHarborProtocol;
import com.gg.core.actor.harbor.protocol.IActorHarborGrpc;
import com.gg.core.master.protocol.MasterGrpc;
import com.gg.core.master.protocol.MasterOuterClass;
import com.google.protobuf.ByteString;
import io.grpc.internal.ManagedChannelImpl;
import io.grpc.internal.ServerImpl;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by guofeng.qin on 2016/7/16.
 */
public class ActorHarbor implements IActorHarborGrpc.IActorHarbor {
    private static final GGLogger logger = GGLogger.getLogger(ActorHarbor.class);

    private ActorSystem system;

    private String selfName;
    private String selfHost;
    private int selfPort;
    private String masterHost;
    private int masterPort;

    private Map<String, RemoteHarbor> remoteStreamMap = new ConcurrentHashMapV8<>();

    public ActorHarbor(ActorSystem system, String selfHost, int selfPort, String masterHost, int masterPort) {
        this.system = system;
        this.selfName = system.getName();
        this.selfHost = selfHost;
        this.selfPort = selfPort;
        this.masterHost = masterHost;
        this.masterPort = masterPort;
        if (StringUtil.isNullOrEmpty(masterHost) || masterPort <= 0) {
            throw new RuntimeException("master must be set.");
        }
        if (StringUtil.isNullOrEmpty(selfName) || StringUtil.isNullOrEmpty(selfHost) || selfPort <= 0) {
            throw new RuntimeException("self must be set.");
        }
        start();
        connectMaster();
    }

    private void start() {
        ServerImpl server = NettyServerBuilder.forPort(selfPort).addService(IActorHarborGrpc.bindService(this)).build();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void addRemoteHarbor(String key, RemoteHarbor harbor) {
        remoteStreamMap.put(key, harbor);
    }

    public void send(ActorMessage msg) {
        msg.setSenderSystem(selfName);
        String remoteSystem = msg.getReceiverSystem();
        RemoteHarbor remote = remoteStreamMap.get(remoteSystem);
        if (remote == null) {
            throw new RuntimeException("Remote Not Exist.");
        }
        remote.send(msg);
    }

    private void connectMaster() {
        ManagedChannelImpl channel = NettyChannelBuilder.forAddress(masterHost, masterPort)
                .negotiationType(NegotiationType.PLAINTEXT).build();
        MasterGrpc.MasterStub stub = MasterGrpc.newStub(channel);
        MasterOuterClass.MasterRegisterMessage msg = MasterOuterClass.MasterRegisterMessage.newBuilder()
                .setService(selfName).setHost(selfHost).setPort(selfPort).build();
        CompletableFuture<Boolean> waitForMaster = new CompletableFuture<>();
        StreamObserver<MasterOuterClass.MasterRegisterMessage> request =
                stub.register(new StreamObserver<MasterOuterClass.MasterRegisterResult>() {
                    private void complateFuture(CompletableFuture<Boolean> future, Boolean result,
                            Throwable throwable) {
                        if (future.isDone()) {
                            return;
                        }
                        if (throwable != null) {
                            future.completeExceptionally(throwable);
                        } else {
                            future.complete(result);
                        }
                    }

                    @Override
                    public void onNext(MasterOuterClass.MasterRegisterResult masterRegisterResult) {
                        try {
                            if (masterRegisterResult != null && masterRegisterResult.getPreviousList() != null) {
                                for (MasterOuterClass.MasterRegisterMessage previous : masterRegisterResult
                                        .getPreviousList()) {
                                    handshake(selfName, selfHost, selfPort, previous);
                                }
                            }
                        } finally {
                            complateFuture(waitForMaster, true, null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        // Status status = Status.fromThrowable(throwable);
                        // Verify.verify(status.getCode() == Status.Code.INTERNAL);
                        // Verify.verify(status.getDescription().contains("Overbite"));
                        complateFuture(waitForMaster, false, throwable);
                        // TODO ... reconnect...
                    }

                    @Override
                    public void onCompleted() {
                        complateFuture(waitForMaster, false, null);
                    }
                });
        request.onNext(msg);

        while (!waitForMaster.isDone()) {
            try {
                Boolean success = waitForMaster.get();
                if (!success) {
                    throw new RuntimeException("Connect Master failure.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private void handshake(String name, String host, int port, MasterOuterClass.MasterRegisterMessage msg) {
        ManagedChannelImpl channel = NettyChannelBuilder.forAddress(msg.getHost(), msg.getPort())
                .negotiationType(NegotiationType.PLAINTEXT).build();
        IActorHarborGrpc.IActorHarborStub harbor = IActorHarborGrpc.newStub(channel);
        ActorHarborProtocol.Service service =
                ActorHarborProtocol.Service.newBuilder().setName(name).setHost(host).setPort(port).build();
        ActorHarborProtocol.HandshakeMessage handshakeMsg =
                ActorHarborProtocol.HandshakeMessage.newBuilder().setSource(service).build();
        RemoteHarbor remoteHarbor = new RemoteHarbor(this);
        StreamObserver<ActorHarborProtocol.RemoteActorMessage> remoteStream = harbor.post(remoteHarbor);
        remoteHarbor.setRemoteStream(this, remoteStream);
        remoteHarbor.sendHandshake(handshakeMsg);
        remoteHarbor.setConnected();
        addRemoteHarbor(msg.getService(), remoteHarbor);
    }

    @Override
    public StreamObserver<ActorHarborProtocol.RemoteActorMessage> post(
            StreamObserver<ActorHarborProtocol.RemoteActorMessage> responseObserver) {
        return new RemoteHarbor(this, responseObserver);
    }

    private void forwardMessage(ActorHarborProtocol.RemoteActorMessage netActorMsg) {
        ActorMessage actorMsg = KryoHelper.readClassAndObject(netActorMsg.getPayload().toByteArray());
        system.forward(actorMsg);
    }

    private static final class RemoteHarbor implements StreamObserver<ActorHarborProtocol.RemoteActorMessage> {

        private Object Lock = new Object();

        private ActorHarbor harbor;
        private StreamObserver<ActorHarborProtocol.RemoteActorMessage> remoteStream;

        private boolean connStatus = false;

        private RemoteHarbor(ActorHarbor harbor) {
            this.harbor = harbor;
        }

        private RemoteHarbor(ActorHarbor harbor, StreamObserver<ActorHarborProtocol.RemoteActorMessage> stream) {
            this.harbor = harbor;
            this.remoteStream = stream;
        }

        private void setRemoteStream(ActorHarbor harbor,
                StreamObserver<ActorHarborProtocol.RemoteActorMessage> stream) {
            this.harbor = harbor;
            remoteStream = stream;
        }

        private void setConnected() {
            connStatus = true;
        }

        @Override
        public void onNext(ActorHarborProtocol.RemoteActorMessage remoteActorMessage) {
            if (remoteActorMessage.getType() == ActorHarborProtocol.MessageType.Handshake) {
                ActorHarborProtocol.HandshakeMessage handshakeMessage =
                        KryoHelper.readClassAndObject(remoteActorMessage.getPayload().toByteArray());
                if (handshakeMessage == null) {
                    logger.error("handshake error.");
                    // TODO ... handshake error.
                    return;
                }
                harbor.addRemoteHarbor(handshakeMessage.getSource().getName(), this);
                setConnected();
            } else {
                if (connStatus) {
                    harbor.forwardMessage(remoteActorMessage);
                } else {
                    logger.error("get message but without handshake.");
                }
            }
        }

        @Override
        public void onError(Throwable throwable) {
            // TODO ... reconnect...
            throwable.printStackTrace();
            logger.error("harbor error...");
        }

        @Override
        public void onCompleted() {
            logger.error("harbor completed...");
        }

        public void send(ActorMessage atrMsg) {
            byte[] payload = KryoHelper.writeClassAndObject(atrMsg);
            ActorHarborProtocol.RemoteActorMessage remoteMsg = ActorHarborProtocol.RemoteActorMessage.newBuilder()
                    .setType(ActorHarborProtocol.MessageType.Post).setPayload(ByteString.copyFrom(payload)).build();
            send(remoteMsg);
        }

        private void send(ActorHarborProtocol.RemoteActorMessage msg) {
            synchronized (Lock) {
                remoteStream.onNext(msg);
            }
        }

        public void sendHandshake(ActorHarborProtocol.HandshakeMessage msg) {
            byte[] payload = KryoHelper.writeClassAndObject(msg);
            ActorHarborProtocol.RemoteActorMessage remoteAtrMsg = ActorHarborProtocol.RemoteActorMessage.newBuilder()
                    .setType(ActorHarborProtocol.MessageType.Handshake).setPayload(ByteString.copyFrom(payload))
                    .build();
            send(remoteAtrMsg);
        }
    }
}

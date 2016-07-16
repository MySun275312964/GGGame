package com.gg.core.actor.harbor.protocol;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;

@javax.annotation.Generated("by gRPC proto compiler")
public class IActorHarborGrpc {

  private IActorHarborGrpc() {}

  public static final String SERVICE_NAME = "IActorHarbor";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage,
      com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage> METHOD_POST =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "IActorHarbor", "post"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage.getDefaultInstance()));

  public static IActorHarborStub newStub(io.grpc.Channel channel) {
    return new IActorHarborStub(channel);
  }

  public static IActorHarborBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new IActorHarborBlockingStub(channel);
  }

  public static IActorHarborFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new IActorHarborFutureStub(channel);
  }

  public static interface IActorHarbor {

    public io.grpc.stub.StreamObserver<com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage> post(
        io.grpc.stub.StreamObserver<com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage> responseObserver);
  }

  public static interface IActorHarborBlockingClient {
  }

  public static interface IActorHarborFutureClient {
  }

  public static class IActorHarborStub extends io.grpc.stub.AbstractStub<IActorHarborStub>
      implements IActorHarbor {
    private IActorHarborStub(io.grpc.Channel channel) {
      super(channel);
    }

    private IActorHarborStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IActorHarborStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new IActorHarborStub(channel, callOptions);
    }

    @java.lang.Override
    public io.grpc.stub.StreamObserver<com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage> post(
        io.grpc.stub.StreamObserver<com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_POST, getCallOptions()), responseObserver);
    }
  }

  public static class IActorHarborBlockingStub extends io.grpc.stub.AbstractStub<IActorHarborBlockingStub>
      implements IActorHarborBlockingClient {
    private IActorHarborBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private IActorHarborBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IActorHarborBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new IActorHarborBlockingStub(channel, callOptions);
    }
  }

  public static class IActorHarborFutureStub extends io.grpc.stub.AbstractStub<IActorHarborFutureStub>
      implements IActorHarborFutureClient {
    private IActorHarborFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private IActorHarborFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected IActorHarborFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new IActorHarborFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_POST = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final IActorHarbor serviceImpl;
    private final int methodId;

    public MethodHandlers(IActorHarbor serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_POST:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.post(
              (io.grpc.stub.StreamObserver<com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final IActorHarbor serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(SERVICE_NAME)
        .addMethod(
          METHOD_POST,
          asyncBidiStreamingCall(
            new MethodHandlers<
              com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage,
              com.gg.core.actor.harbor.protocol.ActorHarborProtocol.RemoteActorMessage>(
                serviceImpl, METHODID_POST)))
        .build();
  }
}

package com.gg.protocol.gate;

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
public class GateTunnelGrpc {

  private GateTunnelGrpc() {}

  public static final String SERVICE_NAME = "GateTunnel";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<com.gg.protocol.gate.GateRpc.Request,
      com.gg.protocol.gate.GateRpc.Response> METHOD_TUNNEL =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "GateTunnel", "tunnel"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.protocol.gate.GateRpc.Request.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.protocol.gate.GateRpc.Response.getDefaultInstance()));

  public static GateTunnelStub newStub(io.grpc.Channel channel) {
    return new GateTunnelStub(channel);
  }

  public static GateTunnelBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new GateTunnelBlockingStub(channel);
  }

  public static GateTunnelFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new GateTunnelFutureStub(channel);
  }

  public static interface GateTunnel {

    public io.grpc.stub.StreamObserver<com.gg.protocol.gate.GateRpc.Request> tunnel(
        io.grpc.stub.StreamObserver<com.gg.protocol.gate.GateRpc.Response> responseObserver);
  }

  public static interface GateTunnelBlockingClient {
  }

  public static interface GateTunnelFutureClient {
  }

  public static class GateTunnelStub extends io.grpc.stub.AbstractStub<GateTunnelStub>
      implements GateTunnel {
    private GateTunnelStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GateTunnelStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GateTunnelStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GateTunnelStub(channel, callOptions);
    }

    @java.lang.Override
    public io.grpc.stub.StreamObserver<com.gg.protocol.gate.GateRpc.Request> tunnel(
        io.grpc.stub.StreamObserver<com.gg.protocol.gate.GateRpc.Response> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_TUNNEL, getCallOptions()), responseObserver);
    }
  }

  public static class GateTunnelBlockingStub extends io.grpc.stub.AbstractStub<GateTunnelBlockingStub>
      implements GateTunnelBlockingClient {
    private GateTunnelBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GateTunnelBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GateTunnelBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GateTunnelBlockingStub(channel, callOptions);
    }
  }

  public static class GateTunnelFutureStub extends io.grpc.stub.AbstractStub<GateTunnelFutureStub>
      implements GateTunnelFutureClient {
    private GateTunnelFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GateTunnelFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GateTunnelFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GateTunnelFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_TUNNEL = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GateTunnel serviceImpl;
    private final int methodId;

    public MethodHandlers(GateTunnel serviceImpl, int methodId) {
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
        case METHODID_TUNNEL:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.tunnel(
              (io.grpc.stub.StreamObserver<com.gg.protocol.gate.GateRpc.Response>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final GateTunnel serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(SERVICE_NAME)
        .addMethod(
          METHOD_TUNNEL,
          asyncBidiStreamingCall(
            new MethodHandlers<
              com.gg.protocol.gate.GateRpc.Request,
              com.gg.protocol.gate.GateRpc.Response>(
                serviceImpl, METHODID_TUNNEL)))
        .build();
  }
}

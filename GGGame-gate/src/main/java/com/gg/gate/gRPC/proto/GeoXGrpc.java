package com.gg.gate.gRPC.proto;

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
public class GeoXGrpc {

  private GeoXGrpc() {}

  public static final String SERVICE_NAME = "GeoX";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<com.gg.gate.gRPC.proto.GeoXOuterClass.Position,
      com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash> METHOD_CALC_GEOHASH =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "GeoX", "calcGeohash"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.gate.gRPC.proto.GeoXOuterClass.Position.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash.getDefaultInstance()));

  public static GeoXStub newStub(io.grpc.Channel channel) {
    return new GeoXStub(channel);
  }

  public static GeoXBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new GeoXBlockingStub(channel);
  }

  public static GeoXFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new GeoXFutureStub(channel);
  }

  public static interface GeoX {

    public void calcGeohash(com.gg.gate.gRPC.proto.GeoXOuterClass.Position request,
        io.grpc.stub.StreamObserver<com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash> responseObserver);
  }

  public static interface GeoXBlockingClient {

    public com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash calcGeohash(com.gg.gate.gRPC.proto.GeoXOuterClass.Position request);
  }

  public static interface GeoXFutureClient {

    public com.google.common.util.concurrent.ListenableFuture<com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash> calcGeohash(
        com.gg.gate.gRPC.proto.GeoXOuterClass.Position request);
  }

  public static class GeoXStub extends io.grpc.stub.AbstractStub<GeoXStub>
      implements GeoX {
    private GeoXStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GeoXStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GeoXStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GeoXStub(channel, callOptions);
    }

    @java.lang.Override
    public void calcGeohash(com.gg.gate.gRPC.proto.GeoXOuterClass.Position request,
        io.grpc.stub.StreamObserver<com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CALC_GEOHASH, getCallOptions()), request, responseObserver);
    }
  }

  public static class GeoXBlockingStub extends io.grpc.stub.AbstractStub<GeoXBlockingStub>
      implements GeoXBlockingClient {
    private GeoXBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GeoXBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GeoXBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GeoXBlockingStub(channel, callOptions);
    }

    @java.lang.Override
    public com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash calcGeohash(com.gg.gate.gRPC.proto.GeoXOuterClass.Position request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CALC_GEOHASH, getCallOptions(), request);
    }
  }

  public static class GeoXFutureStub extends io.grpc.stub.AbstractStub<GeoXFutureStub>
      implements GeoXFutureClient {
    private GeoXFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GeoXFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GeoXFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GeoXFutureStub(channel, callOptions);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash> calcGeohash(
        com.gg.gate.gRPC.proto.GeoXOuterClass.Position request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CALC_GEOHASH, getCallOptions()), request);
    }
  }

  private static final int METHODID_CALC_GEOHASH = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GeoX serviceImpl;
    private final int methodId;

    public MethodHandlers(GeoX serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CALC_GEOHASH:
          serviceImpl.calcGeohash((com.gg.gate.gRPC.proto.GeoXOuterClass.Position) request,
              (io.grpc.stub.StreamObserver<com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final GeoX serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(SERVICE_NAME)
        .addMethod(
          METHOD_CALC_GEOHASH,
          asyncUnaryCall(
            new MethodHandlers<
              com.gg.gate.gRPC.proto.GeoXOuterClass.Position,
              com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash>(
                serviceImpl, METHODID_CALC_GEOHASH)))
        .build();
  }
}

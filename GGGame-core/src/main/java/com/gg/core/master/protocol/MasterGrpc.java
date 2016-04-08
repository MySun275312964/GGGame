package com.gg.core.master.protocol;

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
public class MasterGrpc {

  private MasterGrpc() {}

  public static final String SERVICE_NAME = "Master";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<com.gg.core.master.protocol.MasterOuterClass.MasterRegisterMessage,
      com.gg.core.master.protocol.MasterOuterClass.MasterRegisterResult> METHOD_REGISTER =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "Master", "register"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.core.master.protocol.MasterOuterClass.MasterRegisterMessage.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.core.master.protocol.MasterOuterClass.MasterRegisterResult.getDefaultInstance()));

  public static MasterStub newStub(io.grpc.Channel channel) {
    return new MasterStub(channel);
  }

  public static MasterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MasterBlockingStub(channel);
  }

  public static MasterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MasterFutureStub(channel);
  }

  public static interface Master {

    public io.grpc.stub.StreamObserver<com.gg.core.master.protocol.MasterOuterClass.MasterRegisterMessage> register(
        io.grpc.stub.StreamObserver<com.gg.core.master.protocol.MasterOuterClass.MasterRegisterResult> responseObserver);
  }

  public static interface MasterBlockingClient {
  }

  public static interface MasterFutureClient {
  }

  public static class MasterStub extends io.grpc.stub.AbstractStub<MasterStub>
      implements Master {
    private MasterStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MasterStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MasterStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MasterStub(channel, callOptions);
    }

    @java.lang.Override
    public io.grpc.stub.StreamObserver<com.gg.core.master.protocol.MasterOuterClass.MasterRegisterMessage> register(
        io.grpc.stub.StreamObserver<com.gg.core.master.protocol.MasterOuterClass.MasterRegisterResult> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_REGISTER, getCallOptions()), responseObserver);
    }
  }

  public static class MasterBlockingStub extends io.grpc.stub.AbstractStub<MasterBlockingStub>
      implements MasterBlockingClient {
    private MasterBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MasterBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MasterBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MasterBlockingStub(channel, callOptions);
    }
  }

  public static class MasterFutureStub extends io.grpc.stub.AbstractStub<MasterFutureStub>
      implements MasterFutureClient {
    private MasterFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MasterFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MasterFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MasterFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_REGISTER = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final Master serviceImpl;
    private final int methodId;

    public MethodHandlers(Master serviceImpl, int methodId) {
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
        case METHODID_REGISTER:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.register(
              (io.grpc.stub.StreamObserver<com.gg.core.master.protocol.MasterOuterClass.MasterRegisterResult>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final Master serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(SERVICE_NAME)
        .addMethod(
          METHOD_REGISTER,
          asyncBidiStreamingCall(
            new MethodHandlers<
              com.gg.core.master.protocol.MasterOuterClass.MasterRegisterMessage,
              com.gg.core.master.protocol.MasterOuterClass.MasterRegisterResult>(
                serviceImpl, METHODID_REGISTER)))
        .build();
  }
}

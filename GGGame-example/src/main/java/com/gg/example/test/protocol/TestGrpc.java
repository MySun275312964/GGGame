package com.gg.example.test.protocol;

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
public class TestGrpc {

  private TestGrpc() {}

  public static final String SERVICE_NAME = "Test";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<com.gg.example.test.protocol.Example.TestRequest,
      com.gg.example.test.protocol.Example.TestResponse> METHOD_POST =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "Test", "post"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.example.test.protocol.Example.TestRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(com.gg.example.test.protocol.Example.TestResponse.getDefaultInstance()));

  public static TestStub newStub(io.grpc.Channel channel) {
    return new TestStub(channel);
  }

  public static TestBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TestBlockingStub(channel);
  }

  public static TestFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TestFutureStub(channel);
  }

  public static interface Test {

    public io.grpc.stub.StreamObserver<com.gg.example.test.protocol.Example.TestRequest> post(
        io.grpc.stub.StreamObserver<com.gg.example.test.protocol.Example.TestResponse> responseObserver);
  }

  public static interface TestBlockingClient {
  }

  public static interface TestFutureClient {
  }

  public static class TestStub extends io.grpc.stub.AbstractStub<TestStub>
      implements Test {
    private TestStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TestStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TestStub(channel, callOptions);
    }

    @java.lang.Override
    public io.grpc.stub.StreamObserver<com.gg.example.test.protocol.Example.TestRequest> post(
        io.grpc.stub.StreamObserver<com.gg.example.test.protocol.Example.TestResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_POST, getCallOptions()), responseObserver);
    }
  }

  public static class TestBlockingStub extends io.grpc.stub.AbstractStub<TestBlockingStub>
      implements TestBlockingClient {
    private TestBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TestBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TestBlockingStub(channel, callOptions);
    }
  }

  public static class TestFutureStub extends io.grpc.stub.AbstractStub<TestFutureStub>
      implements TestFutureClient {
    private TestFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TestFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TestFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TestFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_POST = 0;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final Test serviceImpl;
    private final int methodId;

    public MethodHandlers(Test serviceImpl, int methodId) {
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
              (io.grpc.stub.StreamObserver<com.gg.example.test.protocol.Example.TestResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final Test serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(SERVICE_NAME)
        .addMethod(
          METHOD_POST,
          asyncBidiStreamingCall(
            new MethodHandlers<
              com.gg.example.test.protocol.Example.TestRequest,
              com.gg.example.test.protocol.Example.TestResponse>(
                serviceImpl, METHODID_POST)))
        .build();
  }
}

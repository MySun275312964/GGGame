package com.gg.core.harbor.protocol;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;

@javax.annotation.Generated("by gRPC proto compiler")
public class HarborGrpc {

    private HarborGrpc() {}

    public static final String SERVICE_NAME = "Harbor";

    // Static method descriptors that strictly reflect the proto.
    @io.grpc.ExperimentalApi
    public static final io.grpc.MethodDescriptor<com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage, com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage> METHOD_POST =
            io.grpc.MethodDescriptor.create(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
                    generateFullMethodName("Harbor", "post"),
                    io.grpc.protobuf.ProtoUtils.marshaller(
                            com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage.getDefaultInstance()),
                    io.grpc.protobuf.ProtoUtils.marshaller(
                            com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage.getDefaultInstance()));

    public static HarborStub newStub(io.grpc.Channel channel) {
        return new HarborStub(channel);
    }

    public static HarborBlockingStub newBlockingStub(io.grpc.Channel channel) {
        return new HarborBlockingStub(channel);
    }

    public static HarborFutureStub newFutureStub(io.grpc.Channel channel) {
        return new HarborFutureStub(channel);
    }

    public static interface Harbor {

        public io.grpc.stub.StreamObserver<com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage> post(
                io.grpc.stub.StreamObserver<com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage> responseObserver);
    }

    public static interface HarborBlockingClient {
    }

    public static interface HarborFutureClient {
    }

    public static class HarborStub extends io.grpc.stub.AbstractStub<HarborStub> implements Harbor {
        private HarborStub(io.grpc.Channel channel) {
            super(channel);
        }

        private HarborStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected HarborStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new HarborStub(channel, callOptions);
        }

        @java.lang.Override
        public io.grpc.stub.StreamObserver<com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage> post(
                io.grpc.stub.StreamObserver<com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage> responseObserver) {
            return asyncBidiStreamingCall(getChannel().newCall(METHOD_POST, getCallOptions()), responseObserver);
        }
    }

    public static class HarborBlockingStub extends io.grpc.stub.AbstractStub<HarborBlockingStub>
            implements HarborBlockingClient {
        private HarborBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private HarborBlockingStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected HarborBlockingStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new HarborBlockingStub(channel, callOptions);
        }
    }

    public static class HarborFutureStub extends io.grpc.stub.AbstractStub<HarborFutureStub>
            implements HarborFutureClient {
        private HarborFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private HarborFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected HarborFutureStub build(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new HarborFutureStub(channel, callOptions);
        }
    }

    private static final int METHODID_POST = 0;

    private static class MethodHandlers<Req, Resp> implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final Harbor serviceImpl;
        private final int methodId;

        public MethodHandlers(Harbor serviceImpl, int methodId) {
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
        public io.grpc.stub.StreamObserver<Req> invoke(io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_POST:
                    return (io.grpc.stub.StreamObserver<Req>) serviceImpl.post(
                            (io.grpc.stub.StreamObserver<com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage>) responseObserver);
                default:
                    throw new AssertionError();
            }
        }
    }

    public static io.grpc.ServerServiceDefinition bindService(final Harbor serviceImpl) {
        return io.grpc.ServerServiceDefinition.builder(SERVICE_NAME)
                .addMethod(METHOD_POST,
                        asyncBidiStreamingCall(
                                new MethodHandlers<com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage, com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage>(
                                        serviceImpl, METHODID_POST)))
                .build();
    }
}

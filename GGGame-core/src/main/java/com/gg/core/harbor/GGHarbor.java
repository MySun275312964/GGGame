package com.gg.core.harbor;

import java.io.IOException;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.gg.common.Constants;
import com.gg.core.harbor.protocol.HarborGrpc;
import com.gg.core.harbor.protocol.HarborGrpc.Harbor;
import com.gg.core.harbor.protocol.HarborGrpc.HarborStub;
import com.gg.core.harbor.protocol.HarborOuterClass.HandshakeMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;
import com.gg.core.harbor.protocol.HarborOuterClass.Service;
import com.gg.core.master.protocol.MasterGrpc;
import com.gg.core.master.protocol.MasterGrpc.MasterBlockingStub;
import com.gg.core.master.protocol.MasterOuterClass.MasterRegisterMessage;
import com.gg.core.master.protocol.MasterOuterClass.MasterRegisterResult;

import io.grpc.internal.ManagedChannelImpl;
import io.grpc.internal.ServerImpl;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;

/**
 * @author guofeng.qin
 */
public class GGHarbor {
	private static final Logger logger = LoggerFactory.getLogger(GGHarbor.class);

	private static HarborDispatch dispatch;
	private static Service self;
	private static String service;
	private static ApplicationContext ctx;
	
	public static ServerImpl start(ApplicationContext ctx, String service, String host, int port, Executor exepool) throws IOException {
		GGHarbor.ctx = ctx;
		GGHarbor.service = service;
		self = Service.newBuilder().setName(service).setHost(host).setPort(port).build();
		dispatch = new HarborDispatch(exepool);
		ServerImpl server = NettyServerBuilder.forPort(port)
				.addService(HarborGrpc.bindService(new HarborService(dispatch))).build().start();
		handshake(host, port);
		return server;
	}
	
	public static ApplicationContext getCtx() {
		return ctx;
	}

//	public static ServerImpl start(String service, String host, int port, IHarborHandler handler) throws IOException {
//		GGHarbor.service = service;
//		self = Service.newBuilder().setHost(host).setPort(port).build();
//		dispatch = new HarborDispatch(handler);
//		ServerImpl server = NettyServerBuilder.forPort(port)
//				.addService(HarborGrpc.bindService(new HarborService(dispatch))).build().start();
//		handshake(host, port);
//		return server;
//	}

	public static Service getSelf() {
		return self;
	}
	
	public static HarborDispatch getDispatch() {
		return dispatch;
	}

	private static void handshake(String host, int port) {
		ManagedChannelImpl channel = NettyChannelBuilder.forAddress(Constants.Localhost, Constants.MasterPort)
				.negotiationType(NegotiationType.PLAINTEXT).build();
		MasterBlockingStub stub = MasterGrpc.newBlockingStub(channel);
		MasterRegisterMessage msg = MasterRegisterMessage.newBuilder().setService(service).setHost(host).setPort(port).build();
		MasterRegisterResult result = stub.register(msg);
		if (result != null && result.getPreviousList() != null) {
			for (MasterRegisterMessage previous : result.getPreviousList()) {
				handshake(service, host, port, previous);
			}
		}
	}

	private static void handshake(String name, String host, int port, MasterRegisterMessage msg) {
		ManagedChannelImpl channel = NettyChannelBuilder.forAddress(msg.getHost(), msg.getPort())
				.negotiationType(NegotiationType.PLAINTEXT).build();
		HarborStub harbor = HarborGrpc.newStub(channel);
		Service service = Service.newBuilder().setName(name).setHost(host).setPort(port).build();
		HandshakeMessage handshakeMsg = HandshakeMessage.newBuilder().setSource(service).build();
		HarborStreamTunnel tunnel = new HarborStreamTunnel(dispatch, null);
		StreamObserver<HarborMessage> remoteStream = harbor.post(tunnel);
		tunnel.setRemoteStream(msg.getService(), handshakeMsg, remoteStream);
	}

	public static class HarborService implements Harbor {

		private HarborDispatch dispatch;

		public HarborService(HarborDispatch dispatch) {
			assert (dispatch != null);
			this.dispatch = dispatch;
		}

		@Override
		public StreamObserver<HarborMessage> post(StreamObserver<HarborMessage> responseObserver) {
			return new HarborStreamTunnel(dispatch, responseObserver);
		}

	}
}

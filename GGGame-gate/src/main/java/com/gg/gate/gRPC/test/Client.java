package com.gg.gate.gRPC.test;

import com.gg.gate.gRPC.proto.GeoXGrpc;
import com.gg.gate.gRPC.proto.GeoXOuterClass;

import io.grpc.internal.ManagedChannelImpl;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

/**
 * @author guofeng.qin
 */
public class Client {
	public static void main(String[] args) {
		ManagedChannelImpl channel = NettyChannelBuilder.forAddress("127.0.0.1", 9876)
				.negotiationType(NegotiationType.PLAINTEXT).build();

		GeoXGrpc.GeoXBlockingStub blockingStub = GeoXGrpc.newBlockingStub(channel);

		GeoXOuterClass.Position position = GeoXOuterClass.Position.newBuilder().setLat(12.12f).setLng(45.45f)
				.setLevel(4).build();
		GeoXOuterClass.Geohash geohash = blockingStub.calcGeohash(position);
		System.out.println("Geohash: " + geohash.getBase32Str());
		
		while (true) {
			try {
				channel.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

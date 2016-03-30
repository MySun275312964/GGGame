package com.gg.gate.gRPC.test;

import com.gg.gate.gRPC.proto.GeoXGrpc;
import com.gg.gate.gRPC.proto.GeoXOuterClass;
import com.gg.gate.gRPC.proto.GeoXOuterClass.Geohash;
import com.gg.gate.gRPC.proto.GeoXOuterClass.Position;

import io.grpc.stub.StreamObserver;

/**
 * @author guofeng.qin
 */
public class GeoXService implements GeoXGrpc.GeoX {

	@Override
	public void calcGeohash(Position request, StreamObserver<Geohash> responseObserver) {
		float lat = request.getLat();
		float lng = request.getLng();
		int level = request.getLevel();
		System.out.println(lat + ":" + lng + ":" + level);
		GeoXOuterClass.Geohash geo = GeoXOuterClass.Geohash.newBuilder().setBase32Str("wf3gfd3").build();

		responseObserver.onNext(geo);
		responseObserver.onCompleted();
	}

}

package com.gg.gate.gRPC.test;

import com.gg.gate.gRPC.proto.GeoXOuterClass;

import io.grpc.internal.ManagedChannelImpl;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

/**
 * @author guofeng.qin
 */
public class Client {
    public static void main(String[] args) {
        ManagedChannelImpl channel =
                NettyChannelBuilder.forAddress("127.0.0.1", 9876).negotiationType(NegotiationType.PLAINTEXT).build();
        GeoXOuterClass.Position position =
                GeoXOuterClass.Position.newBuilder().setLat(12.12f).setLng(45.45f).setLevel(4).build();

        // GeoXGrpc.GeoXBlockingStub blockingStub =
        // GeoXGrpc.newBlockingStub(channel);
        // GeoXOuterClass.Geohash geohash = blockingStub.calcGeohash(position);
        // System.out.println("Geohash: " + geohash.getBase32Str());

        // GeoXGrpc.GeoXStub stub = GeoXGrpc.newStub(channel);
        // stub.calcGeohash(position, new StreamObserver<GeoXOuterClass.Geohash>(){
        // @Override
        // public void onCompleted() {
        //
        // }
        //
        // @Override
        // public void onError(Throwable arg0) {
        //
        // }
        //
        // @Override
        // public void onNext(Geohash arg0) {
        //
        // }
        // });

        // StreamObserver<Position> stream = stub.calcGeohash(new
        // StreamObserver<GeoXOuterClass.Geohash>() {
        //
        // @Override
        // public void onError(Throwable arg0) {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // @Override
        // public void onCompleted() {
        // System.out.println("client completed...");
        // }
        //
        // @Override
        // public void onNext(GeoXOuterClass.Geohash arg0) {
        // System.out.println("ClientRecvBack: " + arg0.toString());
        // }
        // });
        //
        // for (int i = 0; i < 10; i++) {
        // stream.onNext(position);
        // }

        while (true) {
            try {
                channel.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

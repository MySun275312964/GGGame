package com.gg.core.master;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gg.common.JsonHelper;
import com.gg.core.master.protocol.MasterGrpc;
import com.gg.core.master.protocol.MasterOuterClass;
import com.gg.core.master.protocol.MasterOuterClass.MasterRegisterMessage;
import com.gg.core.master.protocol.MasterOuterClass.MasterRegisterResult;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import io.grpc.stub.StreamObserver;

/**
 * @author guofeng.qin
 */
public class MasterService implements MasterGrpc.Master {

	private static final Logger logger = LoggerFactory.getLogger(MasterService.class);

	private Object Lock = new Object();

	private List<MasterRegisterMessage> registerList = new ArrayList<MasterRegisterMessage>();

	@Override
	public StreamObserver<MasterRegisterMessage> register(StreamObserver<MasterRegisterResult> responseObserver) {
		return new StreamObserver<MasterOuterClass.MasterRegisterMessage>() {
			private MasterRegisterMessage msg;

			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable arg0) {
				if (msg != null) {
					unregist(msg);
				}
			}

			@Override
			public void onNext(MasterRegisterMessage request) {
				this.msg = request;
				List<MasterRegisterMessage> previous = doregist(request);
				if (previous != null) {
					MasterRegisterResult result = MasterRegisterResult.newBuilder().addAllPrevious(previous).build();
					responseObserver.onNext(result);
				}
			}
		};
	}

	private void removeIfExist(MasterRegisterMessage msg) {
		Collection<MasterRegisterMessage> old = Collections2.filter(registerList,
				new Predicate<MasterRegisterMessage>() {
					@Override
					public boolean apply(MasterRegisterMessage input) {
						return msg.getHost().equals(input.getHost()) && (msg.getPort() == input.getPort());
					}
				});
		if (old != null && old.size() > 0) {
			logger.warn(JsonHelper.toJson(old) + " Removed...");
			registerList.removeAll(old);
		}
	}

	private List<MasterRegisterMessage> doregist(MasterRegisterMessage msg) {
		List<MasterRegisterMessage> previous = new ArrayList<MasterRegisterMessage>();
		synchronized (Lock) {
			removeIfExist(msg);
			previous.addAll(registerList.subList(0, registerList.size()));
			registerList.add(msg);
			logger.info(msg.getService() + ":" + msg.getHost() + ":" + msg.getPort() + " registed...");
		}
		return previous;
	}

	private void unregist(MasterRegisterMessage msg) {
		synchronized (Lock) {
			removeIfExist(msg);
		}
	}
}

package com.gg.core.master;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.gg.common.Constants;
import com.gg.core.master.protocol.MasterGrpc;

import io.grpc.internal.ServerImpl;
import io.grpc.netty.NettyServerBuilder;

/**
 * @author guofeng.qin
 */
@SpringBootApplication
public class GGMasterApp implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(GGMasterApp.class);

	@Override
	public void run(String... args) throws Exception {

	}

	public static void main(String[] args) throws IOException {
		try (ConfigurableApplicationContext ctx = SpringApplication.run(GGMasterApp.class, args)) {
			ServerImpl server = startMaster();
			server.start();
			logger.info("ggmaster started...");
			while (!server.isShutdown()) {
				try {
					server.awaitTermination();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static ServerImpl startMaster() {
		return NettyServerBuilder.forPort(Constants.MasterPort).addService(MasterGrpc.bindService(new MasterService()))
				.build();
	}
}

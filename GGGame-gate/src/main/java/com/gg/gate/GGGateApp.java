package com.gg.gate;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import com.gg.common.Constants;
import com.gg.core.harbor.GGHarbor;

import io.grpc.internal.ServerImpl;

/**
 * @author guofeng.qin
 */
@SpringBootApplication
public class GGGateApp implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(GGGateApp.class);

	public static void init(ApplicationContext ctx) throws BeansException, IOException {
		ServerImpl server = GGHarbor.start(ctx, Constants.Gate, Constants.Localhost, Constants.GatePort, Executors.newSingleThreadExecutor());

		while (!server.isShutdown()) {
			try {
				server.awaitTermination();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws BeansException, IOException {
		try (ConfigurableApplicationContext ctx = SpringApplication.run(GGGateApp.class, args)) {
			ctx.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
				@Override
				public void onApplicationEvent(ApplicationEvent event) {
					logger.info("EVENT: " + event.toString());
				}
			});

			init(ctx);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		// do some init here
	}
}

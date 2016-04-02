package com.gg.example.game;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import com.gg.common.Constants;
import com.gg.core.harbor.GGHarbor;
import com.gg.example.common.ExampleConst;

import io.grpc.internal.ServerImpl;

@SpringBootApplication
public class GameServiceApp {
	private static final Logger logger = LoggerFactory.getLogger(GameServiceApp.class);

	public static void init(ApplicationContext ctx) throws BeansException, IOException {
		ServerImpl server = GGHarbor.start(ctx, ExampleConst.GameService, Constants.Localhost, ExampleConst.GameSerivcePort, Executors.newSingleThreadExecutor());
		
		Game g = ctx.getBean(Game.class);
		g.usertest();

		while (!server.isShutdown()) {
			try {
				server.awaitTermination();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws BeansException, IOException {
		try (ConfigurableApplicationContext ctx = SpringApplication.run(GameServiceApp.class, args)) {
			ctx.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
				@Override
				public void onApplicationEvent(ApplicationEvent event) {
					logger.info("Shutdown...");
				}
			});

			init(ctx);
		}
	}
}
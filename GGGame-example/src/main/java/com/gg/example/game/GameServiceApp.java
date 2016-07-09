package com.gg.example.game;

import com.gg.common.Constants;
import com.gg.core.harbor.GGHarbor;
import com.gg.example.common.ExampleConst;
import io.grpc.internal.ServerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
public class GameServiceApp {
    private static final Logger logger = LoggerFactory.getLogger(GameServiceApp.class);
    final static long COUNT = 50000L;
    static AtomicLong AC = new AtomicLong(COUNT);

    public static void init(ApplicationContext ctx) throws BeansException, IOException {
        ServerImpl server = GGHarbor.start(ctx, ExampleConst.GameService, Constants.Localhost,
                ExampleConst.GameSerivcePort, Executors.newFixedThreadPool(1));

        Game g = ctx.getBean(Game.class);
        /*
         * g.usertestasync((s)->{}); try { Thread.sleep(10L); } catch (InterruptedException e1) {
         * e1.printStackTrace(); } long start = System.currentTimeMillis(); for (int i = 0; i <
         * COUNT; i++) { // g.usertest(); g.usertestasync((u)->{ long v = AC.decrementAndGet(); if
         * (v <= 0) { long end = System.currentTimeMillis(); logger.info("TotalTime:" +
         * (end-start)); } }); } long end = System.currentTimeMillis(); logger.info("TotalTime1:" +
         * (end-start));
         */
        g.utest1();
        g.utest2();

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

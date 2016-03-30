package com.gg.gate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author guofeng.qin
 */
@SpringBootApplication
public class GGGateApp implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(GGGateApp.class);

	public static void main(String[] args) {
		try (ConfigurableApplicationContext ctx = SpringApplication.run(GGGateApp.class, args)) {
			ctx.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
				@Override
				public void onApplicationEvent(ApplicationEvent event) {
					logger.info("EVENT: " + event.toString());
				}
			});

			
		}
	}

	@Override
	public void run(String... args) throws Exception {
		// do some init here
	}
}

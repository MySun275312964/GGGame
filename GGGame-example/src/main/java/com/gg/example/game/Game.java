package com.gg.example.game;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gg.core.harbor.HarborFutureTask;
import com.gg.core.harbor.HarborRPC;
import com.gg.example.common.ExampleConst;
import com.gg.example.protocol.user.IUserService;
import com.gg.example.protocol.user.User;

@Component
public class Game {

	private static final Logger logger = LoggerFactory.getLogger(Game.class);
	
	public void usertest() {
		IUserService us = HarborRPC.getHarbor(ExampleConst.UserService, IUserService.class);
		User u = us.getUserById("gametestid");
//		u.toString();
//		logger.info(">>>>>>>>>>>>>>>>>>>: " + u.toString());
	}
	
	public <T>  void usertestasync() {
		IUserService us = HarborRPC.getHarbor(ExampleConst.UserService, IUserService.class);
		HarborFutureTask future = us.getUserByAge(30);
		future.addCallback((u) -> {
			logger.info("<<<<<<<<<<<<<<<<<: " + u.toString());
//			User uu = (User) u;
			
//			for (Task t:uu.getTaskList()) {
//				logger.info(t.getName());
//			}
		});
	}
	
	public void usertestasync(Consumer<Object> consumer) {
		IUserService us = HarborRPC.getHarbor(ExampleConst.UserService, IUserService.class);
		HarborFutureTask future = us.getUserByAge(30);
		future.addCallback(consumer);
	}
}

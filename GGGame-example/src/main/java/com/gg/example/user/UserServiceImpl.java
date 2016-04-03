package com.gg.example.user;

import org.springframework.stereotype.Service;

import com.gg.core.harbor.HarborFutureTask;
import com.gg.example.protocol.user.IUserService;
import com.gg.example.protocol.user.User;

@Service
public class UserServiceImpl implements IUserService {
	@Override
	public User getUserById(String id) {
		return new User(id, "testname", "testicon", 20);
	}

	@Override
	public HarborFutureTask getUserByAge(int age) {
		HarborFutureTask task = HarborFutureTask.buildTask(User.class);
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(15*1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				User u = new User("testid", "testname", "testicon", age);
				task.finish(u);
			}
		}.start();
		return task;
	}
}

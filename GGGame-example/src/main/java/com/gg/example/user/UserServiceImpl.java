package com.gg.example.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gg.core.harbor.HarborFutureTask;
import com.gg.core.harbor.HarborRPC;
import com.gg.example.common.ExampleConst;
import com.gg.example.protocol.task.ITaskService;
import com.gg.example.protocol.task.Task;
import com.gg.example.protocol.user.IUserService;
import com.gg.example.protocol.user.User;

@Service
public class UserServiceImpl implements IUserService {
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public User getUserById(String id) {
//		return new User(id, "testname", "testicon", 20, null);
		return null;
	}

	@Override
	public HarborFutureTask getUserByAge(int age) {
		HarborFutureTask task = HarborFutureTask.buildTask();
		new Thread() {
			@Override
			public void run() {
//				try {
//					Thread.sleep(15*1000L);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				ITaskService taskService = HarborRPC.getHarbor(ExampleConst.TaskService, ITaskService.class);
				List<Task> tl = taskService.getTaskList();
				User u = new User("testid", "testname", "testicon", age, tl);
				if (tl != null) {
					for (Task t:tl) {
						logger.info(t.getName());
					}
				}
				task.finish(u);
			}
		}.start();
		return task;
	}
}

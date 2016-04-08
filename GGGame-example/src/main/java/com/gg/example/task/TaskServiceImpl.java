package com.gg.example.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gg.example.protocol.task.ITaskService;
import com.gg.example.protocol.task.Task;

/**
 * @author guofeng.qin
 */
@Service
public class TaskServiceImpl implements ITaskService {
	private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	
	private static AtomicLong C = new AtomicLong(0L);
	
	@Override
	public List<Task> getTaskList() {
		logger.info("Count:" + C.incrementAndGet());
		List<Task> list = new ArrayList<Task>();
		for (int i = 0; i < 100; i++) {
			list.add(new Task("task:" + i, i));
		}
		return list;
	}

}

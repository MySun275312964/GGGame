package com.gg.example.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gg.example.protocol.task.ITaskService;
import com.gg.example.protocol.task.Task;

/**
 * @author guofeng.qin
 */
@Service
public class TaskServiceImpl implements ITaskService {

	@Override
	public List<Task> getTaskList() {
		List<Task> list = new ArrayList<Task>();
		for (int i = 0; i < 100; i++) {
			list.add(new Task("task:" + i, i));
		}
		return list;
	}

}

package com.gg.example.protocol.user;

import java.util.List;

import com.gg.common.JsonHelper;
import com.gg.example.protocol.task.Task;

public class User implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String icon;
	private int age;

	private List<Task> taskList;
	
	public User() {
		
	}

	public User(String id, String name, String icon, int age, List<Task> taskList) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.age = age;
		this.taskList = taskList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the taskList
	 */
	public List<Task> getTaskList() {
		return taskList;
	}

	/**
	 * @param taskList
	 *            the taskList to set
	 */
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	@Override
	public String toString() {
		return JsonHelper.toJson(this);
	}

}

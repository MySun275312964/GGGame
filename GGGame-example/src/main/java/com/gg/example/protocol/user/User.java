package com.gg.example.protocol.user;

import com.gg.common.JsonHelper;

public class User implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String icon;
	private int age;

	public User(String id, String name, String icon, int age) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.age = age;
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

	@Override
	public String toString() {
		return JsonHelper.toJson(this);
	}

}

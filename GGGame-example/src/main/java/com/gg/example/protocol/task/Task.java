package com.gg.example.protocol.task;

/**
 * @author guofeng.qin
 */
public class Task implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private int level;

	public Task(String name, int level) {
		this.name = name;
		this.level = level;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

}

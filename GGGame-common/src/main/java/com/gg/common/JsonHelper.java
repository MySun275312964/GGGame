package com.gg.common;

import com.google.gson.Gson;

/**
 * @author guofeng.qin
 */
public class JsonHelper {
	private static Gson GSON = new Gson();

	public static String toJson(Object obj) {
		return GSON.toJson(obj);
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return GSON.fromJson(json, clazz);
	}
}

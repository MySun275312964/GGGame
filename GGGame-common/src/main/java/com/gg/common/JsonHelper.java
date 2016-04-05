package com.gg.common;

import java.lang.reflect.Type;

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

	public static <T> T fromJson(String json, Type type) {
		return GSON.fromJson(json, type);
	}

	/**
	 * 用于纠正泛型反序列化错误
	 */
	public static <T> T reparse(Object orig, Type type) {
		if (orig == null || type == null) {
			return null;
		}
		String json = toJson(orig);
		return GSON.fromJson(json, type);
	}
}

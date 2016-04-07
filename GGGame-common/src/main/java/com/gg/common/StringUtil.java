package com.gg.common;

import java.util.StringJoiner;

public class StringUtil {

	public static String join(String delimiter, String... strs) {
		if (strs == null || strs.length <= 0) {
			return null;
		}
		StringJoiner sj = new StringJoiner(delimiter);
		for (String str : strs) {
			sj.add(str);
		}
		return sj.toString();
	}

	public static void printHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X ", b));
		}
		System.out.println(sb.toString());
	}
}

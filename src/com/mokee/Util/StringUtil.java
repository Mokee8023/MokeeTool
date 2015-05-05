package com.mokee.Util;

public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * @param str 需要判定的字符串
	 * @return 为空则返回true，否则为false
	 */
	public static boolean isNullOrEmpty(String str) {
		if ((str == null) || (str.trim().length() < 1)||str.trim().equalsIgnoreCase("null")) {
			return true;
		}
		return false;
	}
}

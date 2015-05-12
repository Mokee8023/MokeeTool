package com.mokee.Util;

import java.util.Arrays;
import java.util.List;

public class StringUtil {
	
	public static String empty = "";

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
	
	/**
	 * 判断两个字符串是否相等
	 * @param argStr1
	 * @param argStr2
	 * @return 相等返回true，不相等返回true
	 */
	public static boolean equalsString(String str1, String str2) {
		if ((str1 == null) && (str2 == null)) { return true; }
		if ((str1 == null) || (str2 == null)) { return false; }
		
		return str1.equals(str2);
	}
	
	/**
	 * 判断strChar字符串在srcString字符串中的个数
	 * @param srcString 源字符串
	 * @param strChar	需要查询数量的字符串
	 * @return			字符串的数量
	 */
	public static int getCount(String srcString, String strChar) {
		int result = 0;

		int beginIndex = 0;
		int endIndex = srcString.indexOf(strChar, beginIndex);

		while (endIndex >= 0) {
			result++;
			beginIndex = endIndex + strChar.length();
			endIndex = srcString.indexOf(strChar, beginIndex);
		}

		return result;
	}
	
	/**
	 * 字符串比较：判断str1是大于、等于还是小于str2字符串
	 * @param str2 	字符串1
	 * @param str2 	字符串2
	 * @return 		
	 * 				全部为空，返回0，表示相等
	 * 				str1为空，返回-1，表示str2大于str1
	 * 				str2为空，返回1，表示str1大于str2
	 * 				其他:按字典顺序返回str1与str2每个字符是否相等，相等返回0，不相等返回不想等的那个字符的AscII差值
	 */
	public static int compare(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return 0;
		}
		if (str1 == null) {
			return -1;
		}
		if (str2 == null) {
			return 1;
		}

		return str1.compareTo(str2);
	}

	/**
	 * 获取字符串长度
	 * @param str:需要计算长度的字符串
	 * @return 字符串的长度，字符串为空也返回0
	 */
	public static int getStrLength(String str) {
		if (str == null)
			return 0;
		return str.length();
	}
	
	/**
	 * 判断某个字符串是否存在于字符串数组中
	 * 
	 * @param stringArray 	原数组
	 * @param source 		查找的字符串
	 * @return 
	 * 			true：在数组中找到字符串
	 * 			                在数组中没有找到字符串
	 */
	public static boolean contains(String[] stringArray, String source) {
		// 转换为list
		List<String> tempList = Arrays.asList(stringArray);

		// 利用list的包含方法,进行判断
		if (tempList.contains(source)) {
			return true;
		} else {
			return false;
		}
	}
}

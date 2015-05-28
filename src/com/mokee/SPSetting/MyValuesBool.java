package com.mokee.SPSetting;


public class MyValuesBool {

	//测试不打印纸张
	private static final String notPrintPaperKey = "notPrintPaper" ;
	/** @return 默认值是 false */
	public static boolean isNotPrintPaper() {
		return MySharedPreferences.getValue(notPrintPaperKey, false);
	}
	public static void setNotPrintPaper(boolean value) {
		MySharedPreferences.SetValue(notPrintPaperKey, value);
	}
}

package com.mokee.SPSetting;

public class MyValuesString {
	
	private static final String Temperature24 = "";
	private static final String Temperature24Key = "Temperature24" ;
	public static String getTemperature24() {
		return MySharedPreferences.getValue(Temperature24Key, Temperature24);
	}
	public static void setTemperature24(String value) {
		MySharedPreferences.SetValue(Temperature24Key, value);
	}
}

package com.mokee.SPSetting;

public class MyValuesInt {

	private static final int Flag=1;
	private static final String FlagKey = "Flag" ;
	public static int getFlag() {
		return MySharedPreferences.getValue(FlagKey, Flag);
	}
	public static void setFlag(int value) {
		MySharedPreferences.SetValue(FlagKey, value);
	}
}

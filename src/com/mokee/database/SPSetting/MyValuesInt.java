package com.mokee.database.SPSetting;

public class MyValuesInt {

	private static final int SocketPort = 7890;
	private static final String SocketPortKey = "socketport" ;
	public static int getSocketPort() {
		return MySharedPreferences.getValue(SocketPortKey, SocketPort);
	}
	public static void setSocketPort(int value) {
		MySharedPreferences.SetValue(SocketPortKey, value);
	}
}

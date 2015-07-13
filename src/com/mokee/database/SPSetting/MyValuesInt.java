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
	
	private static final int smsNum = 200;
	private static final String smsNumKey = "sms_num" ;
	public static int getSmsNum() {
		return MySharedPreferences.getValue(smsNumKey, smsNum);
	}
	public static void setSmsNum(int smsNum) {
		MySharedPreferences.SetValue(smsNumKey, smsNum);
	}
}

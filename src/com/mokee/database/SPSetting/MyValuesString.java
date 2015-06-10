package com.mokee.database.SPSetting;

public class MyValuesString {
	
	private static final String socketIp = "192.168.1.1";
	private static final String socketIpKey = "SocketIp" ;
	public static String getSocketIP() {
		return MySharedPreferences.getValue(socketIpKey, socketIp);
	}
	public static void setSocketIP(String value) {
		MySharedPreferences.SetValue(socketIpKey, value);
	}
}

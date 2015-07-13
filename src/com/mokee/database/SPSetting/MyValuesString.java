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
	
	private static final String bomberTime = "2015-07-13";
	private static final String bomberTimeKey = "bomber_time";
	public static String getBomberTime() {
		return MySharedPreferences.getValue(bomberTimeKey, bomberTime);
	}
	public static void setBomberTime(String time) {
		MySharedPreferences.SetValue(bomberTimeKey, time);
	}
}

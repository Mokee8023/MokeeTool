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
	
	/** 0表示基于百度APIStore, 1表示基于WebService */
	private static final int getPhoneStyle = 0;
	private static final String getPhoneStyleKey = "phone_style";
	public static int getPhoneStyle() {
		return MySharedPreferences.getValue(getPhoneStyleKey, getPhoneStyle);
	}
	public static void setPhoneStyle(int phoneStyle) {
		MySharedPreferences.SetValue(getPhoneStyleKey, phoneStyle);
	}
	
	/** 0表示基于百度APIStore, 1表示基于GPSSo */
	private static final int getIDCardStyle = 0;
	private static final String getIDCardStyleKey = "phone_style";
	public static int getIDCardStyle() {
		return MySharedPreferences.getValue(getIDCardStyleKey, getIDCardStyle);
	}
	public static void setIDCardStyle(int idCardStyle) {
		MySharedPreferences.SetValue(getIDCardStyleKey, idCardStyle);
	}
}

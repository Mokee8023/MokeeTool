package com.mokee.database.SPSetting;


public class MyValuesBool {

	private static final String sweepKey = "is_sweep" ;
	public static boolean isSweep() {
		return MySharedPreferences.getValue(sweepKey, true);
	}
	public static void setIsSweep(boolean value) {
		MySharedPreferences.SetValue(sweepKey, value);
	}
	
	private static final String generateQrCodeKey = "is_generateQrCode" ;
	public static boolean isGenerateQrCode() {
		return MySharedPreferences.getValue(generateQrCodeKey, true);
	}
	public static void setIsGenerateQrCode(boolean value) {
		MySharedPreferences.SetValue(generateQrCodeKey, value);
	}
	
	private static final String phoneNumberKey = "is_phoneNumber" ;
	public static boolean isPhoneNumber() {
		return MySharedPreferences.getValue(phoneNumberKey, true);
	}
	public static void setIsPhoneNumber(boolean value) {
		MySharedPreferences.SetValue(phoneNumberKey, value);
	}
	
	private static final String idCardKey = "is_idCard" ;
	public static boolean isIdCard() {
		return MySharedPreferences.getValue(idCardKey, true);
	}
	public static void setIsIdCard(boolean value) {
		MySharedPreferences.SetValue(idCardKey, value);
	}
	
	private static final String goodsPriceKey = "is_goodsPrice" ;
	public static boolean isGoodsPrice() {
		return MySharedPreferences.getValue(goodsPriceKey, true);
	}
	public static void setIsGoodsPrice(boolean value) {
		MySharedPreferences.SetValue(goodsPriceKey, value);
	}
	
	private static final String translationKey = "is_translation" ;
	public static boolean isTranslation() {
		return MySharedPreferences.getValue(translationKey, true);
	}
	public static void setIsTranslation(boolean value) {
		MySharedPreferences.SetValue(translationKey, value);
	}
	
	private static final String expressKey = "is_express" ;
	public static boolean isExpress() {
		return MySharedPreferences.getValue(expressKey, true);
	}
	public static void setIsExpress(boolean value) {
		MySharedPreferences.SetValue(expressKey, value);
	}
	
	private static final String robotChatKey = "is_robotChat" ;
	public static boolean isRobotChat() {
		return MySharedPreferences.getValue(robotChatKey, true);
	}
	public static void setIsRobotChat(boolean value) {
		MySharedPreferences.SetValue(robotChatKey, value);
	}
	
	private static final String socketDebugKey = "is_socketDebug" ;
	public static boolean isSocketDebug() {
		return MySharedPreferences.getValue(socketDebugKey, true);
	}
	public static void setIsSocketDebug(boolean value) {
		MySharedPreferences.SetValue(socketDebugKey, value);
	}
	
	private static final String hackerBomberKey = "is_hackerBomber" ;
	public static boolean isHackerBomber() {
		return MySharedPreferences.getValue(hackerBomberKey, true);
	}
	public static void setIsHackerBomber(boolean value) {
		MySharedPreferences.SetValue(hackerBomberKey, value);
	}
	
	private static final String quitWindowKey = "is_quitWindow" ;
	public static boolean isQuitWindow() {
		return MySharedPreferences.getValue(quitWindowKey, true);
	}
	public static void setIsQuitWindow(boolean value) {
		MySharedPreferences.SetValue(quitWindowKey, value);
	}
}

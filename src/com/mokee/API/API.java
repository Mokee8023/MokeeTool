package com.mokee.API;

public class API {
	
	//翻译API使用频率为每个IP 1000次/小时,支持免费扩容 
	public static String BaiDuTranslate_API_KEY = "qcRZbAEg4UCESop90Bpgzoe7";
	public static String BaiDuTranslate_SECRET_KEY = "GaHbRUQKWCGmkdtC5PZef0le4web0fjz";
	
	public static final int GET_PHONE_INFORMATION = 0;// WebService获取Phone信息
	public static final int TIMESERVICE = 1;// 系统时间获取
	public static final int GET_PHONE = 2;// 获取联系人
	public static final int GET_TRANSLATE_TEXT = 0;// 翻译文字
	
	
	//Error code
	
	public static final String URL_ERROR = "URL Error";
	public static final String URLENCODER_ERROR = "URLEncoder Error";
	public static final String URLCONNECTION_ERROR = "URLConnection Error";
	public static final String JSONEXCEPTION_ERROR = "JSONException Error";

}

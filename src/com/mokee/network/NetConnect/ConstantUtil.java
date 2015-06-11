package com.mokee.network.NetConnect;

public class ConstantUtil {
	
	/** 标记  */
	public static final int HTTPGET_THREAD_APACHE = 0;
	public static final int HTTPPOST_THREAD_APACHE = 1;
	public static final int HTTPGET_THREAD_JAVA = 3;
	public static final int HTTPPOST_THREAD_JAVA = 4;
	public static final int SOCKET_CLIENT_THREAD_INIT = 5;
	public static final int SOCKET_CLIENT_THREAD_SEND = 6;
	public static final int SOCKET_CLIENT_THREAD_ACCEPT = 7;
	public static final int SOCKET_SERVER_THREAD = 8;
	
	public static final String NETWORK_STATUS_SUCCESS = "Success";
	public static final String NETWORK_STATUS_ERROR_FAILED = "Failed";
	public static final String NETWORK_STATUS_ERROR_NULL = "Result is Null";
	public static final String NETWORK_STATUS_ERROR_Exception = "Exception";
	public static final String NETWORK_STATUS_ERROR_ClientProtocol = "ClientProtocolException";
	public static final String NETWORK_STATUS_ERROR_UnsupportedEncoding = "UnsupportedEncodingException";
	public static final String NETWORK_STATUS_ERROR_IOException = "IOException";
	public static final String NETWORK_STATUS_ERROR_MalformedURL = "MalformedURLException";
	public static final String NETWORK_STATUS_ERROR_UnknownHostException = "UnknownHostException";

}

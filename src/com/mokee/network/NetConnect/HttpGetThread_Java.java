package com.mokee.network.NetConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpStatus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpGetThread_Java extends Thread {
	private final String TAG = getClass().getName();
	
	private Handler mHandler;
	private String mUrl;
	
	private InputStreamReader in;
	private BufferedReader buffer;
	
	/**
	 * HttpGetThread_Java (URL方法默认使用的get方法)
	 * 
	 * @param handler
	 *            用于传递信息的Handler
	 * @param url
	 *            需要Get的url地址
	 */
	public HttpGetThread_Java(Handler handler,String url){
		this.mHandler = handler;
		this.mUrl = url;
	}

	@Override
	public void run() {
		super.run();
		
		Message message = new Message();
		message.what = ConstantUtil.HTTPGET_THREAD_JAVA;
		
		Bundle result = new Bundle();
		
		try {
			/** 创建一个URL对象  */
			URL pathUrl = new URL(mUrl);
			/** 打开一个HttpsURLConnection连接 */
			HttpURLConnection urlConnect = (HttpURLConnection) pathUrl.openConnection();
			/** 设置连接超时时间 */
			urlConnect.setConnectTimeout(3 * 1000);
			/** 开始连接 */
			urlConnect.connect();
			
			if(urlConnect.getResponseCode() == HttpStatus.SC_OK){
				/** 得到读取的内容  */
				in = new InputStreamReader(urlConnect.getInputStream());
				/** 为输出创建BufferedReader */
				buffer = new BufferedReader(in);
				
				String inputLine = null;
				StringBuilder sb = new StringBuilder();
				while((inputLine = buffer.readLine()) != null){
					sb.append(inputLine);
					sb.append("\n");
				}
				String data = sb.toString();
				if(data == null || data.equals("")){
					result.putBoolean("result", false);
					result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_NULL);
					
					Log.e(TAG, "HttpGetThread_Java request is failed, return data is null!");
				} else {
					result.putBoolean("result", true);
					result.putString("data", data);
					
					Log.e(TAG, "HttpGetThread_Java request is success.");
				}
			} else {
				result.putBoolean("result", false);
				result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_FAILED);
				
				Log.e(TAG, "HttpGetThread_Java request is failed,the response is not SC_OK!");
			}
		} catch (MalformedURLException e) {
			result.putBoolean("result", false);
			result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_MalformedURL);
			
			Log.e(TAG, "HttpGetThread_Java request is failed. MalformedURLException:" + e.toString());
		} catch (IOException e) {
			result.putBoolean("result", false);
			result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_IOException);
			
			Log.e(TAG, "HttpGetThread_Java request is failed. IOException:" + e.toString());
		} finally {
			try {
				in.close();
				buffer.close();
			} catch (IOException e) {
				Log.e(TAG,"HttpGetThread_Java--> in.close() or buffer.close() if failed!" + e.toString());
			}
			
			message.obj = result;
			mHandler.sendMessage(message);
		}
	}
}

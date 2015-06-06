package com.mokee.NetConnect;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpGetThread_Apache extends Thread {
	private final String TAG = getClass().getName();

	private Handler mHandler;
	private String mUrl;
	private int mRrequestCode;

	/**
	 * HttpGetThread_Apache 构造函数，启动一个线程
	 * 
	 * @param handler
	 *            用于传递信息的Handler
	 * @param url
	 *            需要Get的url地址（完整的url）
	 */
	public HttpGetThread_Apache(Handler handler, String url, int requestCode) {
		this.mHandler = handler;
		this.mUrl = url;
		this.mRrequestCode = requestCode;
	}

	@Override
	public void run() {
		super.run();
		
		Message message = new Message();
		message.what = mRrequestCode;
		
		Bundle result = new Bundle();

		/** HttpClient 对象  */
		HttpClient mClient = new DefaultHttpClient();

		/** 设置HttpGet请求参数  */
		HttpConnectionParams.setConnectionTimeout(mClient.getParams(), 3 * 1000);/* 设置连接超时 */
		HttpConnectionParams.setSoTimeout(mClient.getParams(), 3 * 1000);/* 设置读取数据时间超时 */
		ConnManagerParams.setTimeout(mClient.getParams(), 3 * 1000);/* 设置从连接池中取连接超时 */

		HttpGet mHttpGet = new HttpGet(mUrl);/* 获取请求 */
		try {
			HttpResponse response = mClient.execute(mHttpGet);/* 执行请求，获取响应结果 */
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
				String data = EntityUtils.toString(response.getEntity(), "utf-8");
				result.putBoolean("result", true);
				result.putString("data", data);
				
				Log.i(TAG, "HttpGetThread_Apache request is sueecss,the response is SC_OK!");
			} else { // 响应未通过
				result.putBoolean("result", false);
				result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_FAILED);
				
				Log.e(TAG, "HttpGetThread_Apache request is failed,the response is not SC_OK!");
			}
		} catch (ClientProtocolException e) {
			result.putBoolean("result", false);
			result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_ClientProtocol);
			
			Log.e(TAG, "HttpGetThread_Apache request is failed. ClientProtocolException:" + e.toString());
		} catch (IOException e) {
			result.putBoolean("result", false);
			result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_IOException);
			
			Log.e(TAG, "HttpGetThread_Apache request is failed. IOException:" + e.toString());
		} finally {
			message.obj = result;
			mHandler.sendMessage(message);
		}
	}
}

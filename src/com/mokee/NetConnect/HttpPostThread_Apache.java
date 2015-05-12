package com.mokee.NetConnect;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpPostThread_Apache extends Thread {
	private final String TAG = getClass().getName();

	private Handler mHandler;
	private String mUrl;
	/** 使用NameValuePair来保存Post要传递的参数  */
	private List<NameValuePair> params;
	
	/**
	 * HttpPostThread_Apache 构造函数，启动一个线程
	 * 
	 * @param handler
	 *            用于传递信息的Handler
	 * @param url
	 *            需要Get的url地址
	 * @param param
	 * 			  Post所需的参数(如：param.add(new BasicNameValuePair("username","hello"));
	 * 						     param.add(new BasicNameValuePair("password","zxq"));)
	 */
	public HttpPostThread_Apache(Handler handler, String url, ArrayList<NameValuePair> param) {
		this.mHandler = handler;
		this.mUrl = url;
		this.params = (ArrayList<NameValuePair>) param.clone();
		// 或者如下复制
		// this.params = new ArrayList<NameValuePair>(param);
	}
	
	
	@Override
	public void run() {
		super.run();
		
		Message message = new Message();
		message.what = ConstantUtil.HTTPPOST_THREAD_APACHE;
		
		Bundle result = new Bundle();

		/** HttpClient 对象  */
		HttpClient mClient = new DefaultHttpClient();

		/** 设置HttpPost 请求参数  */
		HttpConnectionParams.setConnectionTimeout(mClient.getParams(), 3 * 1000);/* 设置连接超时 */
		HttpConnectionParams.setSoTimeout(mClient.getParams(), 3 * 1000);/* 设置读取数据时间超时 */
		ConnManagerParams.setTimeout(mClient.getParams(), 3 * 1000);/* 设置从连接池中取连接超时 */

		HttpPost mHttpPost = new HttpPost(mUrl);/* 获取请求 */
		try {
			/** 设置字符集  */
			HttpEntity entity = new UrlEncodedFormEntity(params,HTTP.UTF_8);
			/** 设置参数实体 */
			mHttpPost.setEntity(entity);
			
			HttpResponse response = mClient.execute(mHttpPost);/* 执行请求，获取响应结果 */
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 响应通过
				String data = EntityUtils.toString(response.getEntity(), "utf-8");
				result.putBoolean("result", true);
				result.putString("data", data);
				
				Log.i(TAG, "HttpPostThread_Apache request is sueecss,the response is SC_OK!");
			} else { // 响应未通过
				result.putBoolean("result", false);
				result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_FAILED);
				
				Log.e(TAG, "HttpPostThread_Apache request is failed,the response is not SC_OK!");
			}
		} catch (UnsupportedEncodingException e){
			result.putBoolean("result", false);
			result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_UnsupportedEncoding);
			
			Log.e(TAG, "HttpPostThread_Apache request is failed. UnsupportedEncodingException:" + e.toString());
		} catch (ClientProtocolException e) {
			result.putBoolean("result", false);
			result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_ClientProtocol);
			
			Log.e(TAG, "HttpPostThread_Apache request is failed. ClientProtocolException:" + e.toString());
		} catch (IOException e) {
			result.putBoolean("result", false);
			result.putString("data", ConstantUtil.NETWORK_STATUS_ERROR_IOException);
			
			Log.e(TAG, "HttpPostThread_Apache request is failed. IOException:" + e.toString());
		} finally {
			message.obj = result;
			mHandler.sendMessage(message);
		}
	}
}

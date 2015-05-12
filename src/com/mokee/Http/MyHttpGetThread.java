package com.mokee.Http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyHttpGetThread extends Thread {
	private static final String TAG = "MyHttpGetThread";

	private Handler handler;
	private String url;
	private int getCode;

	/**
	 * HttpGet请求线程，当请求结果返回为null时，返回的给handler主线程的内容为空字符串:""
	 * 
	 * @param handler
	 *            用于接受线程返回的消息的handler
	 * @param url
	 *            http://openapi.baidu.com/public/2.0/bmt/translate?client_id=YourApiKey&q=today&from=auto&to=auto
	 * @param getCode
	 *            用于区别请求线程，请求HttpGet标记
	 */
	public MyHttpGetThread(Handler handler, String url,int getCode) {
		this.handler = handler;
		this.url = url;
		this.getCode = getCode;
	}

	@Override
	public void run() {
		Message msg = new Message();
		msg.what = getCode;
		String result = HttpUtil.httpGet(url);
		if (result != null) {
			msg.obj = result;
		} else {
			msg.obj = "";
		}
		Log.i(TAG, "MyHttpGetThread.run-->msg.obj:" + msg.obj);
		handler.sendMessage(msg);
	}
}

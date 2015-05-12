package com.mokee.Http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyHttpPostThread extends Thread {
	private static final String TAG = "MyHttpPostThread";

	private Handler handler;
	private String url;
	private String content;
	private int postCode;

	/**
	 * HttpPost请求线程，当请求结果返回为null时，返回的给handler主线程的内容为空字符串:""
	 * 
	 * 完整URL:	http://openapi.baidu.com/public/2.0/bmt/translate?client_id=YourApiKey&q=today&from=auto&to=auto
	 * 
	 * @param handler
	 *            用于接受线程返回的消息的handler
	 * @param url
	 *            请求post的url：http://openapi.baidu.com/public/2.0/bmt/translate
	 * @param content
	 *            除了url外的参数的传递
	 *            请求参数内容为：client_id=YourApiKey&q=today&from=auto&to=auto
	 * @param postCode
	 *            用于区别请求线程，请求HttpPost标记
	 */
	public MyHttpPostThread(Handler handler, String url, String content,int postCode) {
		this.handler = handler;
		this.url = url;
		this.content = content;
		this.postCode= postCode;
	}

	@Override
	public void run() {
		Message msg = new Message();
		msg.what = postCode;
		String result = HttpUtil.post(url, content);
		if (result != null) {
			msg.obj = result;
		} else {
			msg.obj = "";
		}
		Log.i(TAG, "MyHttpPostThread.run-->msg.obj:" + msg.obj);
		handler.sendMessage(msg);
	}
}

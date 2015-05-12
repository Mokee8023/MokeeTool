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

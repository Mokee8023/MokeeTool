package com.mokee.Http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MyHttpGetThread extends Thread {
	private static final String TAG = "MyHttpGetThread";

	private Handler handler;
	private String url;
	private int getCode;

	public MyHttpGetThread(Handler handler, String url,int getCode) {
		this.handler = handler;
		this.url = url;
		this.getCode = getCode;
	}

	@Override
	public void run() {
		Message msg = new Message();
		msg.what = getCode;
		String result = HttpUtil.get(url);
		if (result != null) {
			msg.obj = result;
		} else {
			msg.obj = "";
		}
		Log.i(TAG, "MyHttpGetThread.run-->msg.obj:" + msg.obj);
		handler.sendMessage(msg);
	}
}

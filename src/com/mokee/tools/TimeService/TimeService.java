package com.mokee.tools.TimeService;

import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;

public class TimeService extends Thread {
	private static final String TAG = "TimeService";
	private Handler handler;
	private static final int TIMESERVICE = 1;

	public TimeService(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		super.run();
		while (true) {
			Message message = new Message();
			message.what = TIMESERVICE;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
//				e.printStackTrace();
				Log.i(TAG, e.toString());
			}
			message.obj = DateFormat.format("yyyy-MM-dd HH:mm:ss",
					System.currentTimeMillis());
			handler.sendMessage(message);
		}
	}
}

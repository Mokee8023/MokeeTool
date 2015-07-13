package com.mokee.application.Bomber;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.mokee.application.API.API;
import com.mokee.database.SPSetting.MyValuesInt;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class BomberService extends Thread {
	private static final String tag = "BomberService";
	
	private Handler mHandler;
	private Context mContext;
	private int mNum;// 次数
	private String mPhone;
	
	public BomberService(Handler handler, Context context, String phone, int num){
		this.mHandler = handler;
		this.mContext = context;
		this.mPhone = phone;
		this.mNum = num;
	}
	@Override
	public void run() {
		super.run();
		Log.i(tag, "Phone:" + mPhone + ",    Num:" + mNum);
		
		Message message = new Message();
		message.what = API.MOB_BOMBER_SERVICE;
		
		try{
			while(mNum > 0){
				SMSSDK.initSDK(mContext, API.MOB_APPKEY, API.MOB_APPSECRET);
				Log.i(tag, "initSDK");
				
				EventHandler eventHandler = new EventHandler() {
					public void afterEvent(int event, int result, Object data) {
//						Message msg = new Message();
//						msg.arg1 = event;
//						msg.arg2 = result;
//						msg.obj = data;
//						handler.sendMessage(msg);
						
			                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
			                //获取验证码成功
			                	Log.i(tag, "SMSSDK.EVENT_GET_VERIFICATION_CODE");
			                }
			                
					}
				};
				// 注册回调监听接口
				SMSSDK.registerEventHandler(eventHandler);
				
				SMSSDK.getVerificationCode(String.valueOf(mNum), mPhone);
				Log.i(tag, "getVerificationCode");
				
//				mNum--;
//				MyValuesInt.setSmsNum(MyValuesInt.getSmsNum() - 1);
				
				Toast.makeText(mContext, "剩余" + mNum + "次", Toast.LENGTH_SHORT).show();
				Thread.sleep(60 * 1000);
			}
			
			message.obj = "Bomber success.";
			Log.i(tag, "BomberService Success");
		}catch(Exception ex){
			Log.e(tag, "BomberService Exception:" + ex.toString());
			message.obj = "BomberService Exception:" + ex.toString();
		} finally {
			mHandler.sendMessage(message);
		}
	}
}

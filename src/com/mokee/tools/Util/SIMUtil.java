package com.mokee.tools.Util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SIMUtil {
	private static SIMUtil mInstance = null;
	private static Context mContext;
	private TelephonyManager telManager;

	public SIMUtil(Context context) {
		this.mContext = context;
		telManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public static SIMUtil getInstance() {
		return mInstance;
	}
	
	public static SIMUtil getInstance(Context context){
		if(mInstance == null){
			synchronized (SIMUtil.class) {
				if(mInstance == null){
					mInstance = new SIMUtil(context);
				}
			}
		}
		return mInstance;
	}

	public String getSimOperatorName() {
//		String imsi = telManager.getSubscriberId();
		String simName = "Null";
//
//		if (imsi != null) {
//			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
//				simName = "中国移动";
//			} else if (imsi.startsWith("46001")) {
//				simName = "中国联通";
//			} else if (imsi.startsWith("46003")) {
//				simName = "中国电信";
//			}
//		}
		simName = telManager.getSimOperatorName();
		return simName;
	}
	
	/**
	 * 获取SIM卡的序列号，需要权限：READ_PHONE_STATE 
	 * @return	sim卡序列号
	 */
	public String getSimSerialNumber(){
		return telManager.getSimSerialNumber();
	}
}

package com.mokee.Util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SIMUtil {
	private static SIMUtil mInstance = null;
	private static Context mContext;

	public SIMUtil(Context context) {
		this.mContext = context;
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

	public String getSimName() {
		TelephonyManager telManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telManager.getSubscriberId();
		String SimName = "Null";

		if (imsi != null) {
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				SimName = "中国移动";
			} else if (imsi.startsWith("46001")) {
				SimName = "中国联通";
			} else if (imsi.startsWith("46003")) {
				SimName = "中国电信";
			}
		}
		return SimName;
	}
}

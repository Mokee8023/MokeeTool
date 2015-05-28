package com.mokee.NetConnect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkConnectionUtil {
	private static final String TAG = "NetworkConnection";
	
	private static NetworkConnectionUtil mInstance = null;
	private Context mContext;

	private NetworkConnectionUtil(Context context) {
		this.mContext = context;
	}
	
	public static NetworkConnectionUtil getInstance() {
		return mInstance;
	}
	
	public static NetworkConnectionUtil getInstance(Context context){
		if(mInstance == null){
			synchronized (NetworkConnectionUtil.class) {
				if(mInstance == null){
					mInstance = new NetworkConnectionUtil(context);
				}
			}
		}
		return mInstance;
	}

	/**
	 * Judge the type of the current network
	 * @return	int		
	 * 			0：ConnectivityManager.TYPE_MOBILE		
	 * 			1：ConnectivityManager.TYPE_WIFI
	 * 			2: Invalid NetWork
	 */
	public int judgeNetworkType(){
		ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(networkInfo.isConnected()){
			return ConnectivityManager.TYPE_WIFI;
		} else {
			networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if(networkInfo.isConnected()){
				return ConnectivityManager.TYPE_MOBILE;
			} else {
				return 2;
			}
		}
	}
	
	/**
	 * 得到当前的手机网络类型
	 * 
	 * @param context
	 * @return
	 */ 
	public String getCurrentNetType() { 
	    String type = ""; 
	    ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE); 
	    NetworkInfo info = cm.getActiveNetworkInfo(); 
	    if (info == null) { 
	        type = "null"; 
	    } else if (info.getType() == ConnectivityManager.TYPE_WIFI) { 
	        type = "Wifi"; 
	    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) { 
	        int subType = info.getSubtype(); 
	        if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS 
	                || subType == TelephonyManager.NETWORK_TYPE_EDGE) { 
	            type = "2G"; 
	        } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA 
	                || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0 
	                || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) { 
	            type = "3G"; 
	        } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准 
	            type = "4G"; 
	        } 
	    } 
	    return type; 
	}
}

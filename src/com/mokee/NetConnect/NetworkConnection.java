package com.mokee.NetConnect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkConnection {
	private static final String TAG = "NetworkConnection";
	
	private static NetworkConnection mInstance = null;
	private Context mContext;

	private NetworkConnection(Context context) {
		this.mContext = context;
		isMobileWifiConn();
	}
	
	public static NetworkConnection getInstance() {
		return mInstance;
	}
	
	public static NetworkConnection getInstance(Context context){
		if(mInstance == null){
			synchronized (NetworkConnection.class) {
				if(mInstance == null){
					mInstance = new NetworkConnection(context);
				}
			}
		}
		return mInstance;
	}

	public boolean isMobileWifiConn(){
		ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = networkInfo.isConnected();
		Log.i(TAG, "networkInfo_TYPE_WIFI:" + networkInfo.toString());
		Log.i(TAG, "networkInfo.isConnected():" + isWifiConn);
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = networkInfo.isConnected();
		Log.i(TAG, "networkInfo_TYPE_MOBILE:" + networkInfo.toString());
		Log.i(TAG, "networkInfo.isConnected():" + isMobileConn);
		
		return false;
	}
}

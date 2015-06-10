package com.mokee.network.NetConnect;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

/**
 * Wifi Manager
 * @author Mokee_Work
 * Manager permission:
 * 			1.android.permission.ACCESS_WIFI_STATE
			2.adnroid.permission.ACCESS_CHECKIN_PROPERTTES
			3.android.permission.WAKE_LOCK
			4.android.permission.INTERNET
			5.adnroid.permission.CHANGE_WIFI_STATE
			6.android.permission.MODIFY_PHONE_STATE
 */
public class WifiManagerUtil {
	private static final String TAG = "WifiAdmin";

	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private List<ScanResult> mWifiList;
	private List<WifiConfiguration> mWifiConfigurations;
	private WifiLock mWifiLock = null;

	public WifiManagerUtil(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	/**
	 * Open Wifi
	 * @return Return string type	
	 * 			1."true" if Open Wifi successfully	
	 * 			2."opened" if Wifi has opened
	 * 			3."open_fail" if Open Wifi Failed
	 */
	public String openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			boolean openResult = mWifiManager.setWifiEnabled(true);
			if(openResult){
				return "true";
			} else {
				return "open_fail";
			}
		} else {
			return "opened";
		}
	}

	/**
	 * Close Wifi
	 * @return  Return string type	
	 * 			1."true" if Close Wifi successfully	
	 * 			2."closed" if Wifi has closed
	 * 			3."close_fail" if Close Wifi Failed
	 */
	public String closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			boolean closeResult = mWifiManager.setWifiEnabled(false);
			if(closeResult){
				return "true";
			} else {
				return "close_fail";
			}
		} else {
			return "closed";
		}
	}

	/**
	 * Check Wifi State
	 * @return	Wifi State
	 */
	public int checkState() {
		return mWifiManager.getWifiState();
	}

	/** Scan Wifi List */
	public void startScan() {
		mWifiManager.startScan();
		mWifiList = mWifiManager.getScanResults();
		mWifiConfigurations = mWifiManager.getConfiguredNetworks();
	}
	
	/**
	 * Specify the configuration of the network to connect
	 * @param index		Network index
	 * @return	String Type	：	1."index_error"		2."true"	3."open_wifi_fail"
	 */
	public String connetionConfiguration(int index) {
		if (index > mWifiConfigurations.size()) {
			return "index_error";
		}
		boolean openNerworkResult = mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
		if(openNerworkResult){
			return "true";
		} else {
			return "open_wifi_fail";
		}
	}
	
	/**
	 * Disconnect the specified network ID
	 * @param networkId		Network ID
	 */
	public void disConnectionWifi(int networkId) {
		mWifiManager.disableNetwork(networkId);
		mWifiManager.disconnect();
	}

	/**
	 * Get MAC address
	 * @return	String	:	1.mWifiInfo == null return:null		2.MAC address
	 */
	public String getMacAddress() {
		return (mWifiInfo == null) ? "null" : mWifiInfo.getMacAddress();
	}

	/**
	 * Get BSSID
	 * @return	String	:	1.mWifiInfo == null return:null		2.BSSID
	 */
	public String getBSSID() {
		return (mWifiInfo == null) ? "null" : mWifiInfo.getBSSID();
	}

	/**
	 * Get Ip Address
	 * @return	int	:	1.mWifiInfo == null return:null		2.Ip Address
	 */
	public int getIpAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	/**
	 * Get connected network ID
	 * @return	int	:	1.mWifiInfo == null return:0		2.network ID 
	 */
	public int getNetWordId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	/**
	 * Get WifiInfo All Information
	 * @return	String	:	1.mWifiInfo == null return:null		2.WifiInfo String 
	 */
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "null" : mWifiInfo.toString();
	}

	/**
	 * Add a network and connect
	 * @param configuration		network configuration
	 * @return 	boolean		true: Connect success	false: Connect failed
	 */
	public boolean addNetWork(WifiConfiguration configuration) {
		int wcgId = mWifiManager.addNetwork(configuration);
		boolean enableResult = mWifiManager.enableNetwork(wcgId, true);
		return enableResult;
	}
	
	/**
	 * WifiLock, prevent WiFi from entering the sleep state, so that WiFi has been active in the state.
	 */
	public void acquireWifiLock() {
		if(mWifiLock == null){
			createWifiLock();
		}
		mWifiLock.acquire();
		
	}

	/**
	 * Release Wifi Lock
	 */
	public void releaseWifiLock() {
		if (mWifiLock.isHeld()) {
			mWifiLock.release();
		}
	}
	
	/**
	 * Create Wifi Lock
	 */
	public void createWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("wifi_lock");
	}

	/**
	 * Get well configured network
	 * @return	List<WifiConfiguration>
	 */
	public List<WifiConfiguration> getConfiguration() {
		if(mWifiConfigurations == null){
			startScan();
		}
		return mWifiConfigurations;
	}
	
	/**
	 * Get network list
	 * @return	List<ScanResult>
	 */
	public List<ScanResult> getWifiList() {
		if(mWifiList == null){
			startScan();
		}
		return mWifiList;
	}
	
	/**
	 * View scan results
	 * @return	StringBuffer
	 */
	@SuppressLint("UseValueOf") 
	public StringBuffer lookUpScan() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mWifiList.size(); i++) {
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			// 包括：BSSID、SSID、capabilities、frequency、level
			sb.append((mWifiList.get(i)).toString()).append("\n");
		}
		return sb;
	}
	
	public static String intIPToStringIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
}

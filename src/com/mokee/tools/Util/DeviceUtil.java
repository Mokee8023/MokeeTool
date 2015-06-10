package com.mokee.tools.Util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class DeviceUtil {
	private static final String TAG = "DeviceUtil";
	
	/**
	 * 获取设备的宽度和宽度
	 * @param context	Context，可以传getApplicationContext
	 * @param code	0:获取宽度	1：获取高度		其他数值：获取宽度
	 * @return	高度或宽度
	 * @author Mokee
	 */
	public static int getDeviceSize(Context context, int code){
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		int size;
		int width = manager.getDefaultDisplay().getWidth();
		int height = manager.getDefaultDisplay().getHeight();
		switch (code) {
		case 0: size = width; break;
		case 1: size = height; break;
		default: size = width; break;
		}
		
		Log.i(TAG, "Device size：Width:" + width + ",Height:" + height);
		return size;
	}
	
	
	/**
	 * 获取设备的高度和宽度：Metrics方法
	 * @param context	Context，可以传getApplicationContext
	 * @param code	0:获取宽度	1：获取高度		其他数值：获取宽度
	 * @return	高度或宽度
	 * @author Mokee
	 */
	public static int getDeviceSize_Metrics(Context context, int code){
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		int size;
		int width = outMetrics.widthPixels;
		int height = outMetrics.heightPixels;
		
		switch (code) {
		case 0: size = width; break;
		case 1: size = height; break;
		default: size = width; break;
		}
		
		Log.i(TAG, "Device size：Width:" + width + ",Height:" + height);
		return size;
	}
	
	/**
	 * 获取设备的高度和宽度：Resources方法
	 * @param context	Context，可以传getApplicationContext
	 * @param code	0:获取宽度	1：获取高度		其他数值：获取宽度
	 * @return	高度或宽度
	 * @author Mokee
	 */
	public static int getDeviceSize_Resources(Context context, int code){
		Resources resources = context.getResources();
		DisplayMetrics outMetrics = resources.getDisplayMetrics();
		int size;
		
		float density = outMetrics.density;
		int width = outMetrics.widthPixels;
		int height = outMetrics.heightPixels;
		
		switch (code) {
		case 0: size = width; break;
		case 1: size = height; break;
		default: size = width; break;
		}
		
		Log.i(TAG, "Device size：Width:" + width + ",Height:" + height +",Density:" + density);
		return size;
	}
	
	/**
	 * 获取设备的Wifi IP地址
	 * 		使用此函数需要添加权限：android.permission.ACCESS_WIFI_STATE和android.permission.ACCESS_WIFI_STATE
	 * @param context	Context，可以传getApplicationContext
	 * @return	获取成功：返回ip地址		无wifi：0.0.0.0
	 * @author Mokee
	 */
	public static String getWifiIpAddress(Context context) {
		String ipAdress = null;
		
		WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		Log.i(TAG, "ipAddress:" + ipAddress);
		int[] ipAddr = new int[4];
		ipAddr[0] = ipAddress & 0xFF;
		ipAddr[1] = (ipAddress >> 8) & 0xFF;
		ipAddr[2] = (ipAddress >> 16) & 0xFF;
		ipAddr[3] = (ipAddress >> 24) & 0xFF;
		
		ipAdress = new StringBuilder().append(ipAddr[0]).append(".")
				.append(ipAddr[1]).append(".").append(ipAddr[2]).append(".")
				.append(ipAddr[3]).append(".").toString();
		Log.i(TAG, "DeviceUtil.getWifiIpAddress.ipAdress:" + ipAdress);
		
		return ipAdress;

	}

	/**
	 * 获取设备的IP地址		(包括wifi时的地址和GSM网络时的地址)	使用此函数要附加权限：android.permission.INTERNET
	 * @return	获取成功：返回ip地址(IPV4)	获取失败：null
	 * @author Mokee
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				Log.i(TAG, "Enumeration<NetworkInterface>："+en);
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					Log.i(TAG, "Enumeration<InetAddress>："+enumIpAddr);
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						String ip = inetAddress.getHostAddress().toString();
						Log.i(TAG, "DeviceUtil.getDeviceLocalIpAddress.IP:" + ip);
						return ip;
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, "DeviceUtil.getDeviceLocalIpAddress.SocketException:" + ex.toString());
		}
		return null;
	}
	
	/**
	 * 获取设备的MAC地址	如：2c:8a:72:09:33
	 * @param context	Context，可以传getApplicationContext
	 * @return	获取成功：返回 mac
	 * @author Mokee
	 */
	public static String getLocalMacAddress(Context context) {  
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wifi.getConnectionInfo();  
        return info.getMacAddress();  
    } 
}

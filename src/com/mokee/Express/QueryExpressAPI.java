package com.mokee.Express;

import android.util.Log;

import com.mokee.API.API;

public class QueryExpressAPI {
	private static final String TAG = "QueryExpressAPI";

	/**
	 * 根據需要查詢的訂單號和快遞公司編號，生成url
	 * 
	 * @param number
	 *            訂單號
	 * @param name
	 *            快遞公司編碼
	 * @return 生成的url
	 */
	public static String GetQueryExpressURL(String number, String name) {
		// http://www.kuaidiapi.cn/rest/?uid=10002&key=xxxxxxx&order=6108241734&id=dtwl
		StringBuilder sb = new StringBuilder(
				"http://www.kuaidiapi.cn/rest/?uid=");
		sb.append(API.KUAIDI_UID);
		sb.append("&key=");
		sb.append(API.KUAIDI_API_KEY);
		sb.append("&order=");
		sb.append(number);
		sb.append("&id=");
		sb.append(name);

		Log.i(TAG, "StringBuilder-->GetQueryExpressURL:" + sb.toString());
		return sb.toString();
	}
}

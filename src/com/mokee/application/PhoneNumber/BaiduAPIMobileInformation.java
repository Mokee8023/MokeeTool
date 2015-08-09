package com.mokee.application.PhoneNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.mokee.application.API.API;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * HttpGet
 * Url：http://apis.baidu.com/apistore/mobilephoneservice/mobilephone
 * @author Mokee_2015
 *
 */
public class BaiduAPIMobileInformation extends Thread {
	private static final String TAG = "BaiduAPIMobileInformation";
	private Handler handler;
	private String phoneNumbers;

	public BaiduAPIMobileInformation(Handler handler, String phoneNumbers) {
		this.handler = handler;
		this.phoneNumbers = phoneNumbers;
	}
	
	@Override
	public void run() {
		Message message = new Message();
		message.what = API.GET_PHONE_INFORMATION;
		message.obj = GetPhoneInformation(phoneNumbers);
		handler.sendMessage(message);
	}

	private String GetPhoneInformation(String phoneNumber) {
		String result = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(API.BaiDuPhoneInfoUrl + "?tel=" + phoneNumber);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(8 * 1000);
			connection.setRequestProperty("apikey", API.BaidDuApiStoreApiKey);
			connection.connect();
			
			InputStream in = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String strRead = null;
			while((strRead = reader.readLine()) != null){
				sb.append(strRead).append("\r\n");
			}
			reader.close();
			connection.disconnect();
			result = analyzePhoneJSON(sb.toString());
			return result;
		} catch (Exception e) {
			Log.e(TAG, "GetPhoneInformation.Exception:" + e.toString());
			// result = "Exception" + e.toString();
			return null;
		} 
	}
	
	private String analyzePhoneJSON(String phoneResult){
		Log.i(TAG, "Data:" + phoneResult);
		StringBuilder sb = new StringBuilder();
		try {
			JSONObject jsonData = new JSONObject(phoneResult);
			if (jsonData.getInt("errNum") == 0) {
				JSONObject data = jsonData.getJSONObject("retData");
				sb.append(data.getString("telString")).append("：");
				sb.append(data.getString("province")).append(" ");
				sb.append(data.getString("carrier"));
			} else {
				sb.append(jsonData.getString("errMsg"));
			}
			return sb.toString();
		} catch (JSONException e) {
			Log.e(TAG, "analyzePhoneJSON.JSONException:" + e.toString());
			// sb.append("JSONException:" + e.toString());
			return null;
		}
	}
}

package com.mokee.application.IdCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.mokee.application.API.API;
import com.mokee.network.Http.HttpUtil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BaiduIDCardService extends Thread{
	private static final String tag = "BaiduIDCardService";

	private Handler handler;
	private String idCardNumber;

	public BaiduIDCardService(Handler handler, String idCardNumber) {
		this.handler = handler;
		this.idCardNumber = idCardNumber;
	}
	
	@Override
	public void run() {
		Message message = new Message();
		message.what = API.BAIDU_QUERY_IDCARD_INFO;
		message.obj = GetIdCardInfomation();
		handler.sendMessage(message);
	}
	
	private Handler getQueryResultHandler = new Handler(){
		
	};
	
	public String queryIdCard(){
		return idCardNumber;
	}

	private String GetIdCardInfomation() {
		String result = null;
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		StringBuffer buffer = new StringBuffer();
		String httpUrl = BaiduIDCardUtil.idCardHttpUrl + "?id=" + idCardNumber;
		try {
			URL url = new URL(httpUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("apikey", BaiduIDCardUtil.apikey);
			connection.connect();
			
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),HTTP.UTF_8));
			String strReader = null;
			while((strReader = reader.readLine()) != null){
				buffer.append(strReader).append("\n");
			}
			result = GetIDCardInformation(buffer.toString());
		} catch (MalformedURLException e) {
			result = "MalformedURLException:"+e.toString();
			Log.e(tag, "MalformedURLException:" + e.toString());
		} catch (IOException e) {
			result = "IOException:"+e.toString();
			Log.e(tag, "IOException:" + e.toString());
		} finally {
			try {
				reader.close();
				connection.disconnect();
			} catch (IOException e) {
				result = "IOException:"+e.toString();
				Log.e(tag, "IOException:" + e.toString());
			}
		}
		return result;
	}

	private String GetIDCardInformation(String string) {
		StringBuffer buffer = new StringBuffer();
		try {
			JSONObject data = new JSONObject(string);
			if(data.getInt("errNum") == 0){
				JSONObject idInfo = data.getJSONObject("retData");
				buffer.append("Address: ").append(idInfo.getString("address")).append("\n\n");
				buffer.append("Sex: ").append(BaiduIDCardUtil.getSexChina(idInfo.getString("sex"))).append("\n\n");
				buffer.append("Birthday: ").append(idInfo.getString("birthday")).append("\n");
			} else if(data.getInt("errNum") == -1){
				buffer.append(data.getString("retMsg"));
			} else {
				buffer.append(data.getString("errMsg"));
			}
		} catch (JSONException e) {
			Log.e(tag, "JSONException：" + e.toString());
			buffer.append("JSONException：").append(e.toString());
		}
		
		return buffer.toString();
	}
}

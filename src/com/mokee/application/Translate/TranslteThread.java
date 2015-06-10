package com.mokee.application.Translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mokee.application.API.API;

public class TranslteThread extends Thread {

	private static final String TAG = "Translte";
	private String translateText;
	private String sourceLang = "auto";
	private String targetLang = "auto";

	private Handler handler;

	public TranslteThread(Handler handler, String translate, String sourceLang,
			String targetLang) {
		this.handler = handler;
		this.translateText = translate;
		this.sourceLang = sourceLang;
		this.targetLang = targetLang;
	}

	@Override
	public void run() {
		Message message = new Message();
		message.what = API.GET_TRANSLATE_TEXT;
		message.obj = TranslateText(translateText, sourceLang, targetLang);
		handler.sendMessage(message);
	}

	private String TranslateText(String translateText2, String sourceLang2,
			String targetLang2) {

		String originURL = "http://openapi.baidu.com/public/2.0/bmt/translate";
		String requestURL = GetRequestURL(originURL);
		if (requestURL == API.URLENCODER_ERROR) {
			return API.URLENCODER_ERROR;
		} else {
			URL url;
			HttpURLConnection connection = null;
			try {
				url = new URL(requestURL);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
				String text = GetTextString(reader);
				return text;
			} catch (MalformedURLException e) {
				Log.e(TAG, "URL Create Error:" + e.toString());
				return API.URL_ERROR;
			} catch (IOException e) {
				Log.e(TAG, "HttpURLConnection Error:" + e.toString());
				return API.URLCONNECTION_ERROR;
			} finally {
				connection.disconnect();
			}
		}
	}

	private String GetTextString(BufferedReader reader) throws IOException {
		String text = null;
		StringBuilder builder = new StringBuilder();
		for (String s = reader.readLine(); s != null; s = reader.readLine()) {
			builder.append(s);
		}
		try {
			JSONObject jsonObject = new JSONObject(builder.toString());
			if(jsonObject.has("error_msg")){
				String resultError = jsonObject.getString("error_msg");
				if(resultError.equals("TIMEOUT")){
					return API.TIMEOUT_ERROR;
				}else if(resultError.equals("SYSTEM ERROR")){
					return API.SYSTEM_ERROR;
				}else if(resultError.equals("UNAUTHORIZED USER")){
					return API.UNAUTHORIZED_USER_ERROR;
				}else if(resultError.equals("PARAM_FROM_TO_OR_Q_EMPTY")){
					return API.PARAM_FROM_TO_OR_Q_EMPTY_ERROR;
				}else{
					return API.UNKNOW_ERROR;
				}
			}else{
				StringBuilder textArray = new StringBuilder();
				JSONArray jsonArray = jsonObject.getJSONArray("trans_result");
				for(int i=0;i<jsonArray.length();i++){
					String str = jsonArray.getJSONObject(i).getString("dst");
					textArray.append(str);
					textArray.append('\n');
				}
				text = textArray.toString();
				return text;
			}
		} catch (JSONException e) {
			return API.JSONEXCEPTION_ERROR;
		} 
	}

	private String GetRequestURL(String originURL) {
		// http://openapi.baidu.com/public/2.0/bmt/translate?client_id=YourApiKey&q=today&from=auto&to=auto

		String url = originURL;
		String encoder = urlEncoded(translateText);
		if (encoder == API.URLENCODER_ERROR) {
			return API.URLENCODER_ERROR;
		} else {
			url += "?client_id=" + API.BaiDuTranslate_API_KEY + "&q="
					+ urlEncoded(translateText) + "&from=" + sourceLang
					+ "&to=" + targetLang;
			Log.i(TAG, "请求的Url：" + url);
		}
		return url;
	}

	public static String urlEncoded(String text) {
		String str;
		try {
			str = new String(text.getBytes(), "UTF-8");
			str = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			Log.e(TAG, "URLEncoder Error:" + e.toString());
			return API.URLENCODER_ERROR;
		}
		return str;
	}
}

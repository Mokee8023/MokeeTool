package com.mokee.Http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpStatus;

import com.mokee.Util.StringUtil;

import android.util.Log;

public class HttpUtil {
	private static final String TAG = "HttpUtil";

	/**
	 * HttpGet请求，获取失败返回Null，成功则返回获取内容
	 * 
	 * @param strURL
	 *            请求的URL
	 * 
	 * @return 返回获取的字符串
	 */
	public static String httpGet(String requestURL) {
		BufferedReader bufferedReader = null;
		HttpURLConnection httpURLConnection = null;
		
		try {
			Log.i(TAG, "HttpUtil.httpGet.requestURL = " + requestURL);
			if (StringUtil.isNullOrEmpty(requestURL)) {
				Log.e(TAG, "requestURL isNullOrEmpty:" + requestURL);
				return null;
			}
			URL url = new URL(requestURL);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(20000);
			httpURLConnection.setReadTimeout(20000);
			if (httpURLConnection.getResponseCode() == HttpStatus.SC_OK) {
				bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line;
				StringBuffer stringBuffer = new StringBuffer();
				int num = 0;
				while ((line = bufferedReader.readLine()) != null) {
					num++;
					if (num >= 2) { stringBuffer.append("\n"); }
					stringBuffer.append(line);
				}
				String result = stringBuffer.toString();
				Log.i(TAG, "HttpUtil.httpGet.Http Result:" + result);
				if (StringUtil.isNullOrEmpty(result) || "empty".equalsIgnoreCase(result)) {
					return StringUtil.empty;
				} else { return result; }
			} else {
				Log.e(TAG, "HttpUtil.httpGet.getResponseCode:" + httpURLConnection.getResponseCode());
				return null;
			}
		} catch (Exception e) {
			Log.e(TAG, "HttpUtil.httpGet.catch-->Exception:" + e.toString());
			return null;
		} finally {
			try {
				if (httpURLConnection != null)
					httpURLConnection.disconnect();
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e) {
				Log.e(TAG, "HttpUtil.httpGet.finally.catch-->Exception:" + e.toString());
			}
		}
	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param urlStr
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String httpPost(String urlStr, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		HttpURLConnection connection = null;
		String result = "";
		try {
			URL url = new URL(urlStr);
			// 打开和URL之间的连接
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			out = new PrintWriter(connection.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpStatus.SC_OK) {
				in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				String line;
				while ((line = in.readLine()) != null) {result += line; }
				
				return result;
			} else {
				Log.e(TAG, "HttpUtil.httpPost.responseCode:" + responseCode);
				
				return null;
			}
		} catch (Exception e) {
			Log.e(TAG, "HttpUtil.httpPost.catch-->Exception:" + e.toString());
			e.printStackTrace();
			
			return null;
		} finally {
			try {
				
				if(connection != null){ connection.disconnect(); }
				if (out != null) { out.close(); }
				if (in != null) { in.close(); }
				
			} catch (Exception ex) {
				Log.e(TAG, "HttpUtil.httpPost.finally-->Exception:" + ex.toString());
				ex.printStackTrace();
			}
		}
	}
}

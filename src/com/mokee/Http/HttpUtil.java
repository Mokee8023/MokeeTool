package com.mokee.Http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
	public static String get(String qequestURL) {
		BufferedReader bufferedReader = null;
		HttpURLConnection httpURLConnection = null;
		try {
			Log.v(TAG, "strURL = " + qequestURL);
			if (StringUtil.isNullOrEmpty(qequestURL)) {
				Log.e(TAG, "strURL isNullOrEmpty:" + qequestURL);
				return null;
			}
			URL url = new URL(qequestURL);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(20000);
			httpURLConnection.setReadTimeout(20000);
			if (httpURLConnection.getResponseCode() == HttpStatus.SC_OK) {
				bufferedReader = new BufferedReader(new InputStreamReader(
						httpURLConnection.getInputStream(), "utf-8"));
				String line;
				StringBuffer stringBuffer = new StringBuffer();
				int num = 0;
				while ((line = bufferedReader.readLine()) != null) {
					num++;
					if (num >= 2) {
						stringBuffer.append("\n");
					}
					stringBuffer.append(line);
				}
				String result = stringBuffer.toString();
				Log.i(TAG, "result:" + result);
				if (StringUtil.isNullOrEmpty(result)
						|| "empty".equalsIgnoreCase(result)) {
					return StringUtil.empty;
				} else {
					return result;
				}
			} else {
				Log.e(TAG,
						"getResponseCode:"
								+ httpURLConnection.getResponseCode());
				return null;
			}
		} catch (Exception e) {
			Log.e(TAG, "HttpUtil.get-->Exception:" + e.toString());
			return null;
		} finally {
			try {
				if (httpURLConnection != null)
					httpURLConnection.disconnect();
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e) {
				Log.e(TAG, "HttpUtil.get-->Exception:" + e.toString());
				return null;
			}
		}
	}
	
	public static String post(String urlStr, HashMap<String,String> hashMap) {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, String>> iter = hashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String,String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			if(sb.length()>=1){
				sb.append('&');
			}
			sb.append(key);
			sb.append('=');
			sb.append(val);
		}
		return post(urlStr,sb.toString());
	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String post(String urlStr, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL url = new URL(urlStr);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			int responseCode = conn.getResponseCode();
			if(responseCode==200){
				in = new BufferedReader( new InputStreamReader(conn.getInputStream(),"UTF-8"));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				return result;
			}else{
				System.out.println("responseCode:"+responseCode);
				return null;
			}
		} catch (Exception e) {
			Log.e(TAG, "HttpUtil.post-->Exception:" + e.toString());
			System.out.println("Exception:"+e);
			e.printStackTrace();
			return null;
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(Exception ex){
				Log.e(TAG, "HttpUtil.post-->Exception:" + ex.toString());
				ex.printStackTrace();
			}
		}
	}
}

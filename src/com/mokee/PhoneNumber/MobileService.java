package com.mokee.PhoneNumber;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.mokee.API.API;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MobileService extends Thread {

	private static final String TAG = "MobileService";
	private Handler handler;
	private String phoneNumbers;

	public MobileService(Handler handler, String phoneNumbers) {
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

	private String GetPhoneInformation(String phoneNumbers) {
		// 定义待请求的URL
		String repuestURL = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo";
		// 定义HttpClient
		HttpClient client = new DefaultHttpClient();
		// 根据URL创建HttpPost实例
		HttpPost post = new HttpPost(repuestURL);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 设置需要传递的参数
		params.add(new BasicNameValuePair("mobileCode", phoneNumbers));
		params.add(new BasicNameValuePair("userId", ""));

		// 设置URL编码
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 发送请求并获取反馈
			HttpResponse response = client.execute(post);

			// 判断请求是否成功处理
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 解析返回的内容
				String result = EntityUtils.toString(response.getEntity());
				return API.filterHtml(result);
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			return null;
		}
	}
}

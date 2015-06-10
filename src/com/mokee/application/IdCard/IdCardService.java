package com.mokee.application.IdCard;

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

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mokee.application.API.API;

public class IdCardService extends Thread {
	private static final String TAG = "IdCardService";

	private Handler handler;
	private String idCardNumber;

	public IdCardService(Handler handler, String idCardNumber) {
		this.handler = handler;
		this.idCardNumber = idCardNumber;
	}

	@Override
	public void run() {
		Message message = new Message();
		message.what = API.QUERY_IDCARD_INFO;
		message.obj = GetIdCardInfomation();
		handler.sendMessage(message);
	}

	private Object GetIdCardInfomation() {
		String requestURL = "http://www.gpsso.com/webservice/idcard/idcard.asmx/SearchIdCard";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(requestURL);

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("IdCard", idCardNumber));

		try {
			post.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
				return GetIDCardInformation(API.filterHtml(result));
			} else {
				return null;
			}
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			return null;
		}
	}

	/**
	 * 将查询身份证的接口返回的结果中的“0”和“查询结果成功”去掉，保留余下内容
	 * 
	 * @param idCardInfo
	 *            查询的结果
	 * @return 去除“0”和“查询结果成功”后的字符串
	 */
	private String GetIDCardInformation(String idCardInfo) {

		Log.i(TAG, "原始查詢結果：" + idCardInfo);
		String[] info = idCardInfo.split("\n");
		Log.i(TAG, "長度：" + info.length);
		String result = "";
		for (int i = 2; i < info.length; i++) {
			result += info[i] + "\n";
		}
		Log.i(TAG, "處理后的結果：" + result);
		return result;

	}
}

package com.mokee.Express;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.tools.R;

public class QueryExpressInfo extends Thread {
	private static final String TAG = "QueryExpressInfo";

	private Handler handler;
	private String expressNumber;
	private String expressName;

	public QueryExpressInfo(Handler handler, String expressNumber,
			String expressName) {
		this.handler = handler;
		this.expressNumber = expressNumber;
		this.expressName = expressName;
	}

	@Override
	public void run() {
		Message msg = new Message();
		msg.what = API.QUERY_EXPRESS_INFO;
		// 定义待请求的URL
		String repuestURL = QueryExpressAPI.GetQueryExpressURL(expressNumber,
				expressName);
		Log.i(TAG, "請求的Url：" + repuestURL);

		HttpGet request = new HttpGet(repuestURL);
		HttpParams params = new BasicHttpParams();
		HttpClient httpClient = new DefaultHttpClient(params);
		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
				msg.obj = result;
			} else {
				msg.obj = "111";
			}

		} catch (Exception e) {
			msg.obj = "222";
		} finally {
			httpClient.getConnectionManager().shutdown();
			handler.sendMessage(msg);
		}
	}
}

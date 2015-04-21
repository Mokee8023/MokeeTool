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
import com.mokee.API.API;

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

		String repuestURL = QueryExpressAPI.GetQueryExpressURL(expressNumber,
				expressName);
		Log.i(TAG, "Request Url：" + repuestURL);

		HttpGet request = new HttpGet(repuestURL);
		HttpParams params = new BasicHttpParams();
		HttpClient httpClient = new DefaultHttpClient(params);
		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
				String resultString = QueryExpressAPI
						.AnalyzeExpressJSON(result);
				msg.obj = resultString;
			} else {
				msg.obj = "response.getStatusLine().getStatusCode() != HttpStatus.SC_OK:"
						+ response.getStatusLine().getStatusCode();
			}

		} catch (Exception e) {
			msg.obj = "httpClient.execute(request):" + e.toString();
		} finally {
			Log.i(TAG, msg.obj.toString());
			httpClient.getConnectionManager().shutdown();
			handler.sendMessage(msg);
		}
	}
}

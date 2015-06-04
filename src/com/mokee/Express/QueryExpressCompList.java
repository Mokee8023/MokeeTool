package com.mokee.Express;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.mokee.API.API;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class QueryExpressCompList extends Thread {
	private static final String TAG = "QueryExpressCompList";

	private Handler handler;

	public QueryExpressCompList(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void run() {
		Message msg = new Message();
		msg.what = API.QUERY_EXPRESS_LIST_INFO;
		
		String repuestURL = QueryExpressAPI.GetShowAPIQueryExpressListURL();
		Log.i(TAG, "Request Url：" + repuestURL);

		HttpGet request = new HttpGet(repuestURL);
		HttpParams params = new BasicHttpParams();
		HttpClient httpClient = new DefaultHttpClient(params);
		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity());
//				String resultString = QueryExpressAPI
//						.AnalyzeExpressJSON(result);
				msg.obj = result;
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

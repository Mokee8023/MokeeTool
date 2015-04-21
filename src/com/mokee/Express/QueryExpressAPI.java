package com.mokee.Express;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;
import android.util.Log;

import com.mokee.API.API;

public class QueryExpressAPI {
	private static final String TAG = "QueryExpressAPI";

	/**
	 * 根據需要查詢的訂單號和快遞公司編號，生成url
	 * 
	 * @param number
	 *            訂單號
	 * @param name
	 *            快遞公司編碼
	 * @return 生成的url
	 */
	public static String GetQueryExpressURL(String number, String name) {
		// http://www.kuaidiapi.cn/rest/?uid=10002&key=xxxxxxx&order=6108241734&id=dtwl
		StringBuilder sb = new StringBuilder(
				"http://www.kuaidiapi.cn/rest/?uid=");
		sb.append(API.KUAIDI_UID);
		sb.append("&key=");
		sb.append(API.KUAIDI_API_KEY);
		sb.append("&order=");
		sb.append(number);
		sb.append("&id=");
		sb.append(name);
		sb.append("&time=");
		sb.append(DateFormat.format("yyyy-MM-dd HH:mm:ss",System.currentTimeMillis()));
		sb.append("&issign=");
		sb.append("true");
		sb.append("&ord=");
		sb.append("desc");
		sb.append("&show=");
		sb.append("json");
		sb.append("&last=");
		sb.append("false");
		String url = sb.toString();
		// url = url.replaceAll("&", "%26");
		url = url.replaceAll(" ", "%20");

		Log.i(TAG, "StringBuilder-->GetQueryExpressURL:" + url);
		return url;
	}

	public static String AnalyzeExpressJSON(String data) {
		StringBuilder sb = new StringBuilder();
		if (data == null || data.equals("")) {
			return "Query data is empty!";
		} else {
			try {
				JSONObject originJSON = new JSONObject(data);
				sb.append(originJSON.getString("name"));
				sb.append(": ");
				sb.append(originJSON.getString("order"));
				sb.append("\n");
				String errorCode = originJSON.getString("errcode");
				if (errorCode.equals("0000")) {
					sb.append("Express Status: ");
					sb.append(GetExpressStatus(originJSON.getInt("status")));
					sb.append("\n");
					JSONArray dataArray = originJSON.getJSONArray("data");
					for (int i = 0; i < dataArray.length(); i++) {
						JSONObject item = dataArray.getJSONObject(i);
						// sb.append(i);
						// sb.append(".");
						sb.append(item.getString("time"));
						sb.append(":");
						sb.append(item.getString("content"));
						sb.append("\n");
					}
				} else {
					sb.append(originJSON.getString("message"));
				}
				Log.i(TAG, "JSONData:" + sb.toString());
				return sb.toString();
			} catch (Exception e) {
				Log.e(TAG, "Data processing error:" + e.toString());
				return "Data processing error:" + e.toString();
			}
		}
	}

	private static String GetExpressStatus(int status) {
		String statusString = null;
		switch (status) {
		case -1:
			statusString = "待查询、在批量查询中才会出现的状态,指提交后还没有进行任何更新的单号";
			break;
		case 0:
			statusString = "查询异常";
			break;
		case 1:
			statusString = "暂无记录、单号没有任何跟踪记录";
			break;
		case 2:
			statusString = "在途中";
			break;
		case 3:
			statusString = "派送中";
			break;
		case 4:
			statusString = "已签收";
			break;
		case 5:
			statusString = "拒收、用户拒签";
			break;
		case 6:
			statusString = "疑难件、以为某些原因无法进行派送";
			break;
		case 7:
			statusString = "无效单";
			break;
		case 8:
			statusString = "超时单";
			break;
		case 9:
			statusString = "签收失败";
			break;
		default:
			statusString = "未知错误";
			break;
		}
		Log.i(TAG, "Express Status:" + status + "Status String:" + statusString);
		return statusString;
	}

	private static String GetResult(String errcode) {
		int errcodeInt = Integer.parseInt(errcode);
		String returnString = null;
		Log.i(TAG, "errcodeString:" + errcode + "errcodeInt:" + errcodeInt);
		switch (errcodeInt) {
		case 0:
			returnString = "接口调用正常,无任何错误";
			break;
		case 1:
			returnString = "传输参数格式有误";
			break;
		case 2:
			returnString = "用户编号(UID)无效";
			break;
		case 3:
			returnString = "用户被禁用";
			break;
		case 4:
			returnString = "Key无效";
			break;
		case 5:
			returnString = "快递代号(id)无效";
			break;
		case 6:
			returnString = "访问次数达到最大额度";
			break;
		case 7:
			returnString = "查询服务器返回错误";
			break;
		default:
			returnString = "未知错误";
			break;
		}
		return returnString;
	}
}

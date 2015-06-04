package com.mokee.API;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class API {

	// 翻译API使用频率为每个IP 1000次/小时,支持免费扩容
	public static String BaiDuTranslate_API_KEY = "qcRZbAEg4UCESop90Bpgzoe7";
	public static String BaiDuTranslate_SECRET_KEY = "GaHbRUQKWCGmkdtC5PZef0le4web0fjz";
	
	//百度定位CK
	public static String BaiDuLocation_API_CK = "C8d9423757173cb90286e924b15ac47f";
	
	// http://www.kuaidiapi.cn	调用次数200次每天
	public static String KUAIDI_API_KEY = "2b0fd0a6d69742c3a6812739e8f1b9c0";
	public static String KUAIDI_UID = "28980";
	
	//https://www.showapi.com/app/editApp     ShowAPI的接口
	public static String ShOWAPI_KUAIDI_secret = "8f4e447a431e4fe8bbbe28ff1ad29eb4";
	public static String ShOWAPI_KUAIDI_appid = "106";

	public static final int GET_PHONE_INFORMATION = 0;// WebService获取Phone信息
	public static final int TIMESERVICE = 1;// 系统时间获取
	public static final int GET_PHONE = 2;// 获取联系人
	public static final int GET_TRANSLATE_TEXT = 3;// 翻译文字

	public static final int SWEEP = 4;// 扫码
	public static final int SAVE_QRCODE_FLAG = 5;// 保存二維碼
	public static final int QUERY_IDCARD_INFO = 6;// 查詢號碼信息
	public static final int QUERY_EXPRESS_INFO = 7;// 查詢快递
	public static final int QUERY_EXPRESS_LIST_INFO = 8;// 查詢快递名称列表

	// Error code
	public static final String URL_ERROR = "URL Error";
	public static final String URLENCODER_ERROR = "URLEncoder Error";
	public static final String URLCONNECTION_ERROR = "URLConnection Error";
	public static final String JSONEXCEPTION_ERROR = "JSONException Error";
	public static final String TIMEOUT_ERROR = "TimeOut Error";
	public static final String SYSTEM_ERROR = "System Error";
	public static final String UNAUTHORIZED_USER_ERROR = "Unauthorized User Error";
	public static final String PARAM_FROM_TO_OR_Q_EMPTY_ERROR = "Param From To OrQuery is Empty Error";
	public static final String UNKNOW_ERROR = "Unknow Error";

	/**
	 * 使用正则表达式过滤HTML标记
	 * 
	 * @param source
	 *            待过滤内容
	 * @return
	 */
	public static String filterHtml(String source) {
		if (null == source) {
			return "";
		}
		return source.replaceAll("</?[^>]+>", "").trim();
	}

	/**
	 * 将Bitmap转换为Byte[]形式
	 * 
	 * @param bitmap
	 *            需要变换成Byte数组的Bitmap
	 * @return 返回变换后的Byte[]
	 */
	public static byte[] Bitmap2Byte(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 将Byte[]转换为Bitmap形式
	 * 
	 * @param mbyte
	 *            需要变换成Bitmap的Byte数组
	 * @return 返回变换后的Bitmap
	 */
	public static Bitmap Byte2Bitmap(byte[] mbyte) {
		if (mbyte.length != 0) {
			return BitmapFactory.decodeByteArray(mbyte, 0, mbyte.length);
		} else {
			return null;
		}
	}

	/**
	 * 将路径一层一层的创建
	 * 
	 * @param filePath
	 *            需要创建目录的路径
	 */
	public void makeRootDirectory(String filePath) {
		File file = null;
		String newPath = null;
		String[] path = filePath.split("/");
		for (int i = 0; i < path.length; i++) {
			if (newPath == null) {
				newPath = path[i];
			} else {
				newPath = newPath + "/" + path[i];
			}
			file = new File(newPath);
			if (!file.exists()) {
				file.mkdir();
			}
		}
	}

	/**
	 * 判断字符串是否全为数字
	 * 
	 * @param str
	 *            需要判断的字符串
	 * @return 全为数字：true 不全为数字:false
	 */
	public static boolean StringISNum(String str) {
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				if (!Character.isDigit(str.charAt(i))) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
}

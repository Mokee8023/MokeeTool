package com.mokee.API;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class API {

	// 翻译API使用频率为每个IP 1000次/小时,支持免费扩容
	public static String BaiDuTranslate_API_KEY = "qcRZbAEg4UCESop90Bpgzoe7";
	public static String BaiDuTranslate_SECRET_KEY = "GaHbRUQKWCGmkdtC5PZef0le4web0fjz";

	public static final int GET_PHONE_INFORMATION = 0;// WebService获取Phone信息
	public static final int TIMESERVICE = 1;// 系统时间获取
	public static final int GET_PHONE = 2;// 获取联系人
	public static final int GET_TRANSLATE_TEXT = 3;// 翻译文字

	public static final int SWEEP = 4;// 扫码
	public static final int SAVE_QRCODE_FLAG = 5;// 扫码

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
}

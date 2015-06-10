package com.mokee.application.Robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.mokee.tools.FilePath.FilePath;

public class ShowAPIChatFunction {
	private static final String TAG = "ShowAPIChatFunction";
	
	public static String getAnalyzeShowAPIChat(String chatData){
		Log.i(TAG, "ShowAPIChatFunction.getAnalyzeShowAPIChat:" + chatData);
		String mChatData = "";
		try {
			JSONObject oriChat = new JSONObject(chatData);
			if(oriChat.getInt("showapi_res_code") == 0){
				mChatData = oriChat.getJSONObject("showapi_res_body").getString("text");
			} else {
				mChatData = "调用接口失败，" + oriChat.getString("showapi_res_error");
			}
		} catch (JSONException e) {
			Log.e(TAG, "ShowAPIChatFunction.getAnalyzeShowAPIChat.JSONException:" + e.toString());
			e.printStackTrace();
		}
		
		return mChatData;
	}
	
	public static String SaveRobotChatText(Context context, String content) {
		content = "Create Time：" + (String) DateFormat.format("yyyy-MM-dd HH:mm:ss",
						System.currentTimeMillis()) + "\n\n\nCreate Content：\n\n" + content;
		String txtSavePath = FilePath.GetFilePath();
		if (txtSavePath == null) {
			txtSavePath = "/sdcard/MokeeRobotChat";
		} else {
			/////////////////////Android 4.4之后的系统///////////////////////////////////////
			
			/*
			 * Android 4.4之后的系统，由于有内置存储卡，所有如若再外置存储卡上保存文件，创建目录
			 * 必须先执行该句话，创建/Android/data/包名/的目录，然后才能在外置存储卡上创建目录，
			 * 否则创建目录会失败
			 */
			context.getExternalFilesDir(null);
			
			////////////////////////////////////////////////////////////////////////////////
			txtSavePath = txtSavePath + "/Android/data/" + context.getApplicationContext().getPackageName() + "/MokeeRobotChatText";
		}

		File file = new File(txtSavePath);
		if (!file.exists()) {
			Log.e(TAG, "" + file.mkdirs());
		}
		
		String txtFilePath = txtSavePath + File.separator + "Robot_Chat_" + (String) DateFormat.format("yyyy_MM_dd_HH_mm_ss", System.currentTimeMillis()) + ".txt";
		Log.i(TAG, "txtFilePath:" +txtFilePath);
		
		FileOutputStream fos = null;
		file = new File(txtFilePath);
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return txtFilePath;
	}
}

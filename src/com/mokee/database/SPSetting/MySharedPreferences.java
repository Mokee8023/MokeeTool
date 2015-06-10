package com.mokee.database.SPSetting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class MySharedPreferences {
	public static SharedPreferences sharedPreferences;
	public static Editor editor ;
	
	public static void init(Context context) {
		MySharedPreferences.sharedPreferences=context.getSharedPreferences("toole_sp", Context.MODE_PRIVATE);
		MySharedPreferences.editor = MySharedPreferences.sharedPreferences.edit();
	}
	
	public synchronized static boolean getValue(String key, boolean defValue){
		try {
			return MySharedPreferences.sharedPreferences.getBoolean(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defValue;
	}
	
	public synchronized static int getValue(String key, int defValue){
		try {
			return MySharedPreferences.sharedPreferences.getInt(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defValue;
	}
	
	public synchronized static String getValue(String key, String defValue){
		try {
			return MySharedPreferences.sharedPreferences.getString(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defValue;
	}

	public synchronized static void SetValue(String key, boolean value){
		try {
			MySharedPreferences.editor.putBoolean(key, value);
			if (MySharedPreferences.editor.commit()) {
				Runtime.getRuntime().exec("sync");
			} else {
				Log.e("保存失败", "保存失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("保存异常", "保存异常"+e.toString());
		}
	}
	
	public synchronized static void SetValue(String key, int value){
		try {
			//如果值相同无需重复保存
			if(value == getValue(key,-987654321)){
				return ;
			}
			MySharedPreferences.editor.putInt(key, value);
			if (MySharedPreferences.editor.commit()) {
				Runtime.getRuntime().exec("sync");
			} else {
				Log.e("保存失败", "保存失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("保存异常", "保存异常"+e.toString());
		}
	}
	
	public synchronized static void SetValue(String key, String value){
		try {
			//如果值相同无需重复保存
			if(getValue(key, "9D1A994B-CDD2-4EC8-91B7-AA172D8322FB").equals(value)){
				return ;
			}
			MySharedPreferences.editor.putString(key, value);
			if (MySharedPreferences.editor.commit()) {
				Runtime.getRuntime().exec("sync");
			} else {
				Log.e("保存失败", "保存失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("保存异常", "保存异常"+e.toString());
		}
	}
	
}

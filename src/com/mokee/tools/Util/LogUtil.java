package com.mokee.tools.Util;

import java.util.Locale;

import android.util.Log;

public class LogUtil {
	private static boolean LOGV = true;
	private static boolean LOGD = true;
	private static boolean LOGI = true;
	private static boolean LOGW = true;
	private static boolean LOGE = true;

	/** Log.v */
	public static void v(String tag, String message) {
		if (LOGV) {
			Log.v(tag, message);
		}
	}
	
	/** Log.v (自动获取TAG--调用者的类名) */
	public static void v(String message) {
		if (LOGV) {
			Log.v(getTag(), message);
		}
	}
	
	/** Log.v (自动获取TAG--调用者的类名)		(格式化Message--包括行号、方法名) */
	public static void v_build(String message) {
		if (LOGV) {
			Log.v(getTag(), buildMessage(message));
		}
	}

	/** Log.d */
	public static void d(String tag, String message) {
		if (LOGD) {
			Log.d(tag, message);
		}
	}
	
	/** Log.d (自动获取TAG--调用者的类名) */
	public static void d(String message) {
		if (LOGD) {
			Log.d(getTag(), message);
		}
	}
	
	/** Log.d (自动获取TAG--调用者的类名)		(格式化Message--包括行号、方法名) */
	public static void d_build(String message) {
		if (LOGD) {
			Log.d(getTag(), buildMessage(message));
		}
	}

	/** Log.i */
	public static void i(String tag, String message) {
		if (LOGI) {
			Log.i(tag, message);
		}
	}
	
	/** Log.i (自动获取TAG--调用者的类名) */
	public static void i(String message) {
		if (LOGI) {
			Log.i(getTag(), message);
		}
	}
	
	/** Log.i (自动获取TAG--调用者的类名)		(格式化Message--包括行号、方法名) */
	public static void i_build(String message) {
		if (LOGI) {
			Log.i(getTag(), buildMessage(message));
		}
	}

	/** Log.w */
	public static void w(String tag, String message) {
		if (LOGW) {
			Log.w(tag, message);
		}
	}
	
	/** Log.w (自动获取TAG--调用者的类名) */
	public static void w(String message) {
		if (LOGW) {
			Log.w(getTag(), message);
		}
	}
	
	/** Log.w (自动获取TAG--调用者的类名)		(格式化Message--包括行号、方法名) */
	public static void w_build(String message) {
		if (LOGW) {
			Log.w(getTag(), buildMessage(message));
		}
	}

	/** Log.e */
	public static void e(String tag, String message) {
		if (LOGE) {
			Log.e(tag, message);
		}
	}
	
	/** Log.e (自动获取TAG--调用者的类名) */
	public static void e(String message) {
		if (LOGE) {
			Log.e(getTag(), message);
		}
	}
	
	/** Log.e (自动获取TAG--调用者的类名)		(格式化Message--包括行号、方法名) */
	public static void e_build(String message) {
		if (LOGE) {
			Log.e(getTag(), buildMessage(message));
		}
	}
	
	/** 获取到调用者的类名 */
	private static String getTag() {
		StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
		String callingClass = "";
		for (int i = 2; i < trace.length; i++) {
			Class clazz = trace[i].getClass();
			if (!clazz.equals(LogUtil.class)) {
				callingClass = trace[i].getClassName();
				callingClass = callingClass.substring(callingClass .lastIndexOf('.') + 1);
				break;
			}
		}
		return callingClass;
	}

	/** 格式化打印信息（行号、方法名） */
	private static String buildMessage(String msg) {
	     StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();
	     String caller = "" ;
	     for ( int i = 2 ; i < trace.length; i++) {
	          Class clazz = trace[i].getClass();
	          if (!clazz.equals(LogUtil.class )) {
	               caller = trace[i].getMethodName();
	               break ;
	          }
	     }
	     return String.format(Locale.US, "[%d] %s: %s" , Thread.currentThread().getId(), caller, msg);

	}

}

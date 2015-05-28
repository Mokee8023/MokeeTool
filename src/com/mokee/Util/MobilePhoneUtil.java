package com.mokee.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

public class MobilePhoneUtil {
	
	private static MobilePhoneUtil mInstance = null;
	private static Context mContext;
	private TelephonyManager telephony = null;

	public MobilePhoneUtil(Context context) {
		this.mContext = context;
		telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public static MobilePhoneUtil getInstance() {
		return mInstance;
	}
	
	public static MobilePhoneUtil getInstance(Context context){
		if(mInstance == null){
			synchronized (SIMUtil.class) {
				if(mInstance == null){
					mInstance = new MobilePhoneUtil(context);
				}
			}
		}
		return mInstance;
	}

	/**
	 * 唯一的设备ID
	 * @return	GSM手机的 IMEI 和 CDMA手机的 MEID.  
	 */
	public String getImeiOrMeid(){
		return telephony.getDeviceId();
	}
	
	/**  
     * 手机号：  
     * @return	GSM手机的 MSISDN,电信手机暂获取不到手机号码.  
     */   
	public String getPhoneNumber(){
		String phoneNumber = telephony.getLine1Number();
		if(phoneNumber == null || phoneNumber.equals("")){
			return "电信手机暂时无法获取";
		}
		return telephony.getLine1Number();
	}
	
	/**
	 * 获取CPU序列号
	 * 
	 * @return CPU序列号(16位) 读取失败为"您的手机暂时无法获取CPU序列号"
	 */
	public String getCPUSerial() {
		String str = "", strCPU = "", cpuAddress = "您的手机暂时无法获取CPU序列号";
		try {
			// 读取CPU信息
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader isr = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(isr);
			// 查找CPU序列号
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					// 查找到序列号所在行
					if (str.indexOf("Serial") > -1) {
						// 提取序列号
						strCPU = str.substring(str.indexOf(":") + 1, str.length());
						cpuAddress = strCPU.trim();
						break;
					}
				} else {
					// 文件结尾
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return cpuAddress;
	}
	
	public String getTotalMemory() {  
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");

			// 获得系统总内存，单位是KB，乘以1024转换为Byte
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();

		} catch (IOException e) {
		}
		
		return Formatter.formatFileSize(mContext, initial_memory);// Byte转换为KB或者MB，内存大小规格化 
    }
	
	public String getAvailMemory() {// 获取android当前可用内存大小 

		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);//mi.availMem; 当前系统的可用内存 

		return Formatter.formatFileSize(mContext, mi.availMem);
		}
	
	/**
	 * 获取Rom的大小(总大小和可用大小)
	 * @return	long[0]:总大小	long[1]:可用大小
	 */
	public long[] getRomMemroy() {  
		File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
        long[] romInfo = new long[2];  
        
        long blockSize = stat.getBlockSize();  
        long availableBlocks = stat.getAvailableBlocks(); 
        long totalBlocks = stat.getBlockCount(); 
        
        romInfo[0] = blockSize * totalBlocks;		//Total rom memory 
        romInfo[1] = blockSize * availableBlocks;   //Available rom memory
        return romInfo;  
    } 
	
	/**
	 * 获取SDCard的大小(总大小和可用大小)
	 * @return	long[0]:总大小	long[1]:可用大小
	 */
	public long[] getSDCardMemory() {  
		long[] sdCardInfo = new long[2];
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long bSize = sf.getBlockSize();
			long bCount = sf.getBlockCount();
			long availBlocks = sf.getAvailableBlocks();

			sdCardInfo[0] = bSize * bCount;// 总大小
			sdCardInfo[1] = bSize * availBlocks;// 可用大小
		}
		return sdCardInfo; 
    }
	
	/**
	 * 获取系统运行时间
	 * @return	int[0]:小时	int[1]:分钟
	 */
	public int[] getSystemRunTimes() {  
		int[] time = new int[2];  
	    long ut = SystemClock.elapsedRealtime() / 1000;  
	    if (ut == 0) {  
	        ut = 1;  
	    }  
	    time[0] = (int) ((ut / 3600));  
	    time[1] = (int) ((ut / 60) % 60);  
	    return time;  
	} 
	
	/**
	 * 获取系统版本
	 * @return
	 * 		version[0]：Kernel Version
	 * 		version[1]：firmware version
	 *		version[2]：model
	 *		version[3]：system version
	 */
	public String[] getVersion() {
		String[] version = { "null", "null", "null", "null" };
		String str1 = "/proc/version";
		String str2;
		String[] arrayOfString;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			version[0] = arrayOfString[2];// Kernel Version
			localBufferedReader.close();
		} catch (IOException e) {
		}
		version[1] = Build.VERSION.RELEASE;// firmware version
		version[2] = Build.MODEL;// model
		version[3] = Build.DISPLAY;//system version  
	    return version;  
	}
}

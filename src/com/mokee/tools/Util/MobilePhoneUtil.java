package com.mokee.tools.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
	private static final String TAG = "MobilePhoneUtil";
	
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
		String str = "", strCPU = "", cpuAddress = "Sorry,don't support query Serial.";
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
						if(cpuAddress.equals("0000000000000000")){
							cpuAddress = "Sorry,don't support query Serial.";
						}
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
	
	public String getRAMTotalMemory() {  
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
	
	public String getRAMAvailMemory() {// 获取android当前可用内存大小 

		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);//mi.availMem; 当前系统的可用内存 

		return Formatter.formatFileSize(mContext, mi.availMem);
		}
	
	/**
	 * 获取Rom的大小(总大小和可用大小)
	 * @return	long[0]:总大小	long[1]:可用大小
	 */
	@SuppressWarnings("deprecation")
	public long[] getRomMemroy() {  
		File path = Environment.getDataDirectory();  
		Log.i(TAG, "Environment.getDataDirectory().getPath:" + path.getPath());
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
	public long[] getInternalSDCardMemory() {  
		long[] sdCardInfo = new long[2];
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			Log.i(TAG, "Environment.getInternalSDCardMemory().getPath:" + sdcardDir.getPath());
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
	 * 获取External SDCard的大小(总大小和可用大小)
	 * @return	long[0]:总大小	long[1]:可用大小
	 */
	@SuppressWarnings("deprecation")
	public long[] getExternalSDCardMemory() {  
		long[] externalSdCardInfo = new long[2];
		try {
			String externalSDCardPath = getExtSDCardPath();
			StatFs stat = new StatFs(externalSDCardPath);  
			long bSize = stat.getBlockSize();
			long bCount = stat.getBlockCount();
			long availBlocks = stat.getAvailableBlocks();

			externalSdCardInfo[0] = bSize * bCount;// 总大小
			externalSdCardInfo[1] = bSize * availBlocks;// 可用大小
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "getExternalSDCardMemory.Exception:" + e.toString());
		}
		
		return externalSdCardInfo; 
    }
	
	/**
	 * 获取外置SD卡路径
	 * 
	 * @return 应该就一条记录或空
	 */
	public String getExtSDCardPath() {
		String extPath = new String();
		try {  
            Runtime runtime = Runtime.getRuntime();  
            Process proc = runtime.exec("mount");  
            InputStream is = proc.getInputStream();  
            InputStreamReader isr = new InputStreamReader(is);  
            String line;  
            BufferedReader br = new BufferedReader(isr);  
            while ((line = br.readLine()) != null) {  
                if (line.contains("fuse") && line.contains("storage") && line.contains("ext_sd")) {  
                    String columns[] = line.split(" ");  
                    if (columns != null && columns.length > 1) {  
                    	extPath = columns[1];
                    }  
                }  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
            Log.e(TAG, "getExternalSDCardMemory.FileNotFoundException:" + e.toString());
        } catch (IOException e) {  
            e.printStackTrace();  
            Log.e(TAG, "getExternalSDCardMemory.IOException:" + e.toString());
        }  
		
		Log.i(TAG, "lResult:" + extPath.toString());
		return extPath.toString();
	}
	
	
	/**
	 * 获取外置存储卡的根路径
	 * @return		根路径		没有外置存储卡：null
	 */
	public String getExternalSDCardPath() {
		String sdcard_path = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("ext_sd")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						sdcard_path = columns[1];
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "getExternalSDCardMemory.Exception:" + e.toString());
		}
		
		Log.i(TAG, "sdcard_path:"+sdcard_path);
		return sdcard_path;
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

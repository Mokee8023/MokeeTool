package com.mokee.FilePath;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;

public class FilaPath {
	private static final String TAG = "FilaPath";

	/**
	 * 获得系统的路径
	 *     1.如果系统有两个SDCard，则返回第二个
	 *     2.如果只有一个则返回第一个
	 *     3.没有SDCard，则返回null
	 * @return SDCard的路径，没有"/",需要时，可加:File.separator
	 */
	public static String GetFilePath() {
		String secondExterPath = getSecondExterPath();
		if (secondExterPath != null) {
			return secondExterPath;
		} else {
			String firstExterPath = getFirstExterPath();
			if (firstExterPath != null) {
				return firstExterPath;
			} else {
				return null;
			}
		}
	}

	/**
	 * 获取系统的第一个存储路径，如果无存储卡，则返回null
	 * @return 系统的第一个存储路径
	 */
	private static String getFirstExterPath() {
		String first_ExternalPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		Log.i(TAG, "First_ExternalPath:" + first_ExternalPath);
		return first_ExternalPath;
	}

	/**
	 * 获取系统的第二个存储路径，如果无第二个存储卡，则返回null
	 * @return 系统的第二个存储路径
	 */
	private static String getSecondExterPath() {
		List<String> paths = getAllExterSdcardPath();

		if (paths.size() == 2) {
			for (String path : paths) {
				if (path != null && !path.equals(getFirstExterPath())) {
					Log.i(TAG, "Second_ExternalPath:" + path);
					return path;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	/**
	 * 获取系统的所有存储路径
	 * @return 系统的存储路径的List
	 */
	private static List<String> getAllExterSdcardPath() {
		List<String> sdcarddList = new ArrayList<String>();
		String firstPath = getFirstExterPath();

		// 得到路径
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				// 将常见的linux分区过滤掉
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;
				if (line.contains("media"))
					continue;
				if (line.contains("system") || line.contains("cache")
						|| line.contains("sys") || line.contains("data")
						|| line.contains("tmpfs") || line.contains("shell")
						|| line.contains("root") || line.contains("acct")
						|| line.contains("proc") || line.contains("misc")
						|| line.contains("obb")) {
					continue;
				}

				if (line.contains("fat") || line.contains("fuse")
						|| (line.contains("ntfs"))) {

					String[] columns = line.split(" ");
					if (columns != null && columns.length > 1) {
						String path = columns[1];
						if (path != null && !sdcarddList.contains(path)
								&& path.contains("sd"))
							sdcarddList.add(columns[1]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!sdcarddList.contains(firstPath)) {
			sdcarddList.add(firstPath);
		}
		return sdcarddList;
	}

	/**
	 * 判断第一个SDCard是否卸载
	 * @return 卸载了：true,否则为false
	 */
	private static boolean isFirstSdcardMounted() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断第二个SDCard是否卸载
	 * @return 卸载了：true,否则为false
	 */
	private static boolean isSecondSDcardMounted() {
		String sdcard2 = getSecondExterPath();
		if (sdcard2 == null) {
			return false;
		}
		return checkFsWritable(sdcard2 + File.separator);
	}

	// 测试外置sd卡是否卸载，不能直接判断外置sd卡是否为null，因为当外置sd卡拔出时，仍然能得到外置sd卡路径。我这种方法是按照android谷歌测试DICM的方法，
	// 创建一个文件，然后立即删除，看是否卸载外置sd卡
	// 注意这里有一个小bug，即使外置sd卡没有卸载，但是存储空间不够大，或者文件数已至最大数，此时，也不能创建新文件。此时，统一提示用户清理sd卡吧
	private static boolean checkFsWritable(String dir) {

		if (dir == null)
			return false;

		File directory = new File(dir);

		if (!directory.isDirectory()) {
			if (!directory.mkdirs()) {
				return false;
			}
		}

		File f = new File(directory, ".keysharetestgzc");
		try {
			if (f.exists()) {
				f.delete();
			}
			if (!f.createNewFile()) {
				return false;
			}
			f.delete();
			return true;

		} catch (Exception e) {
		}
		return false;

	}
}

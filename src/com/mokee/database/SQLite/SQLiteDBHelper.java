package com.mokee.database.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "MokeeDB.db";
	private static final int DATABASE_VERSION = 1;

	public SQLiteDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public SQLiteDBHelper(Context context) {
		//  CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 *  数据库第一次被创建时onCreate会被调用  
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS Goods(" +
					"_id INTEGER PRIMARY KEY AUTOINCREMENT,GoodsName VARCHAR(50)," +
					" BarCode VARCHAR(20)," +
					" SupermarketName1 VARCHAR(20), Price1 REAL," +
					" SupermarketName2 VARCHAR(20), Price2 REAL," +
					" SupermarketName3 VARCHAR(20), Price3 REAL," +
					" info TEXT)");
	}

	/**
	 * 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}

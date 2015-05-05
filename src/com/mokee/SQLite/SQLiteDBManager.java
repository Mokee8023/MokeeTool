package com.mokee.SQLite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDBManager {
	private static final String TAG = "private DBHelper helper"; 
    private SQLiteDatabase db; 
	private SQLiteDBHelper helper;  
	
	public SQLiteDBManager(Context context) {  
        helper = new SQLiteDBHelper(context);  
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);  
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里  
        db = helper.getWritableDatabase();  
    } 
	
	/** 
     * Add Goods 
     * @param Goods 
     */  
    public void add(List<Goods> goods) {  
        db.beginTransaction();  //开始事务  
        try {  
            for (Goods good : goods) {  
//            	if(good.getGoodsName() != null && good.getBarCode() != null && good.getInfo() != null 
//            		&& good.getSuperMarketName1() != null && good.getPrice1() != null 
//            		&& good.getSuperMarketName2() != null && good.getPrice2() != null 
//            		&& good.getSuperMarketName3() != null && good.getPrice3() != null ){
//            		db.execSQL("INSERT INTO Goods VALUES(null, ?, ?, ?)", new Object[]{person.name, person.age, person.info}); 
//            	} else {
//            		continue;
//            	}
            	db.execSQL("INSERT INTO Goods VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
            			new Object[]{good.getGoodsName(), good.getBarCode(), 
            			good.getSuperMarketName1(), good.getPrice1(),
            			good.getSuperMarketName2(), good.getPrice2(),
            			good.getSuperMarketName3(), good.getPrice3(),
            			good.getInfo()}); 
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
    
    /** 
     * Update SuperMarket1
     * @param goods 
     */  
    public void updateSuperMarket1(Goods goods) {  
        ContentValues cv = new ContentValues();  
        cv.put("SupermarketName1", goods.getSuperMarketName1());  
        cv.put("Price1", goods.getPrice1());  
        db.update("Goods", cv, "name = ?", new String[]{goods.getGoodsName()});  
    }
    
    /** 
     * Update SuperMarket2
     * @param goods 
     */  
    public void updateSuperMarket2(Goods goods) {  
        ContentValues cv = new ContentValues();  
        cv.put("SupermarketName2", goods.getSuperMarketName3());  
        cv.put("Price2", goods.getPrice2());  
        db.update("Goods", cv, "name = ?", new String[]{goods.getGoodsName()});  
    }
    
    /** 
     * Update SuperMarket3
     * @param goods
     */  
    public void updateSuperMarket3(Goods goods) {  
        ContentValues cv = new ContentValues();  
        cv.put("SupermarketName3", goods.getSuperMarketName3());  
        cv.put("Price3", goods.getPrice3());  
        db.update("Goods", cv, "name = ?", new String[]{goods.getGoodsName()});  
    }
    
    /** 
     * Update Info
     * @param goods 
     */  
    public void updateInfo(Goods goods) {  
        ContentValues cv = new ContentValues();  
        cv.put("info", goods.getInfo());  
        db.update("Goods", cv, "name = ?", new String[]{goods.getGoodsName()});  
    }
    
    
    /** 
     * Delete goods 
     * @param goods 
     */  
    public void deleteOldGoods(Goods goods) {  
        db.delete("Goods", "name = ?", new String[]{goods.getGoodsName()});  
    } 
    
    /** 
     * Query all Goods, return list 
     * @return List<Goods> 
     */  
    public List<Goods> query() {  
        ArrayList<Goods> allGoods = new ArrayList<Goods>();  
        Cursor c = queryTheCursor();  
        while (c.moveToNext()) {  
        	Goods goods = new Goods();  
        	goods._id = c.getInt(c.getColumnIndex("_id"));  
        	goods.goodsName = c.getString(c.getColumnIndex("GoodsName"));  
        	goods.barCode = c.getString(c.getColumnIndex("BarCode"));  
        	goods.superMarketName1 = c.getString(c.getColumnIndex("SupermarketName1"));  
        	goods.price1 = c.getFloat(c.getColumnIndex("Price1"));  
        	goods.superMarketName2 = c.getString(c.getColumnIndex("SupermarketName2"));  
        	goods.price2 = c.getFloat(c.getColumnIndex("Price2")); 
        	goods.superMarketName3 = c.getString(c.getColumnIndex("SupermarketName3"));  
        	goods.price3 = c.getFloat(c.getColumnIndex("Price3"));
        	goods.info = c.getString(c.getColumnIndex("info"));
        	
        	allGoods.add(goods);  
        }  
        c.close();  
        return allGoods;  
    }  
    
    /** 
     * Query all Goods, return cursor 
     * @return  Cursor 
     */  
    public Cursor queryTheCursor() {  
        Cursor c = db.rawQuery("SELECT * FROM Goods", null);  
        return c;  
    }  
    
    /** 
     * Close database 
     */  
    public void closeDB() {  
        db.close();  
    } 
}

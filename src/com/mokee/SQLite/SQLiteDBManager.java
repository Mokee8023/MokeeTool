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
            	Cursor c = queryName(good.getGoodsName());
            	if(c.moveToNext()){
            		updateGoods(good);
            	} else {
            		db.execSQL("INSERT INTO Goods VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
                			new Object[]{good.getGoodsName(), good.getBarCode(), 
                			good.getSuperMarketName1(), good.getPrice1(),
                			good.getSuperMarketName2(), good.getPrice2(),
                			good.getSuperMarketName3(), good.getPrice3(),
                			good.getInfo()}); 
            	}
            	
            }  
            db.setTransactionSuccessful();  //设置事务成功完成  
        } finally {  
            db.endTransaction();    //结束事务  
        }  
    }  
    
    /** 
     * Update Goods (Except: GoodsName )
     * @param goods 
     */
    public void updateGoods(Goods goods) {  
        ContentValues cv = new ContentValues(); 
        if(goods.getBarCode() != null && !goods.getBarCode().equals("")){
        	cv.put("BarCode", goods.getBarCode());  
        }
        
        if(goods.getSuperMarketName1() != null && !goods.getSuperMarketName1().equals("")){
        	cv.put("SupermarketName1", goods.getSuperMarketName1()); 
        }
         
        if(goods.getSuperMarketName2() != null && !goods.getSuperMarketName2().equals("")){
        	cv.put("SupermarketName2", goods.getSuperMarketName2());  
        }
        
        if(goods.getSuperMarketName3() != null && !goods.getSuperMarketName3().equals("")){
        	cv.put("SupermarketName3", goods.getSuperMarketName3());  
        }
        
        if(goods.getInfo() != null && !goods.getInfo().equals("")){
        	cv.put("info", goods.getInfo());  
        }
        
        if(goods.getPrice1() != -1){
        	cv.put("Price1", goods.getPrice1());
        }
        
        if(goods.getPrice2() != -1){
        	cv.put("Price2", goods.getPrice2());
        }
        
        if(goods.getPrice3() != -1){
        	cv.put("Price3", goods.getPrice3());
        }
         
        db.update("Goods", cv, "GoodsName = ?", new String[]{goods.getGoodsName()});  
    }
    
    /** 
     * Delete goods 
     * @param Goods 
     */  
    public void deleteOldGoods(Goods goods) {  
        db.delete("Goods", "GoodsName = ?", new String[]{goods.getGoodsName()});  
    } 
    
    /** 
     * Delete goods 
     * @param goodsName 
     */  
    public void deleteOldGoods(String goodsName) {  
        db.delete("Goods", "GoodsName = ?", new String[]{goodsName});  
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
     * 根据名称查询
     * @param goodsName 商品名称
     * @return 查询到的Cursor
     */
    public Cursor queryName(String goodsName){
    	Cursor c = db.query("Goods", new String[]{"GoodsName"}, "GoodsName=?", new String[]{goodsName}, null, null, null);
    			return c;
    }
    /** 
     * Close database 
     */  
    public void closeDB() {  
        db.close();  
    } 
}

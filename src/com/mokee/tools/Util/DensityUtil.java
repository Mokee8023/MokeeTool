package com.mokee.tools.Util;

import android.content.Context;
import android.util.Log;

public class DensityUtil {
	private static final String TAG = "DensityUtil";

	/**
	 * 根据手机的分辨率从 dp的单位转成为 px(像素) 
	 * @param context	Context，可以传getApplicationContext
	 * @param dpValue	dp值得大小
	 * @return			px值得大小
	 * @author Mokee
	 */
    public static int dip2px(Context context, float dpValue) {  
        float scale = context.getResources().getDisplayMetrics().density;  
        float pxValue = dpValue * scale;
        
		Log.i(TAG, "dip2px:" + dpValue + "dp-->" + pxValue + "px");
		
        return (int) (pxValue + 0.5f);  
    }  
  
    /**
	 * 根据手机的分辨率从 px(像素)的单位转成为 dp 
	 * @param context	Context，可以传getApplicationContext
	 * @param pxValue	px值的大小
	 * @return			dp值的大小
	 * @author Mokee
	 */
    public static int px2dip(Context context, float pxValue) {  
        float scale = context.getResources().getDisplayMetrics().density; 
        float dpValue = pxValue * scale;
        
        Log.i(TAG, "px2dip:" + pxValue + "px-->" + dpValue + "dp");
        
        return (int) (dpValue + 0.5f);  
    } 
    
    /**
     * 将px值转换为sp值
     * @param context	Context，可以传getApplicationContext
     * @param pxValue	px值的大小
     * @return			sp值的大小
     * @author Mokee
     */ 
    public static int px2sp(Context context, float pxValue) { 
        final float scale = context.getResources().getDisplayMetrics().scaledDensity; 
        float spValue = pxValue * scale;
        
        Log.i(TAG, "px2sp:" + pxValue + "px-->" + spValue + "sp");
        
        return (int) (spValue + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值
     * @param context	Context，可以传getApplicationContext
     * @param spValue	sp值得大小
     * @return			px值的大小
     * @author Mokee
     */ 
    public static int sp2px(Context context, float spValue) { 
        float scale = context.getResources().getDisplayMetrics().scaledDensity; 
        float pxValue = spValue * scale;
        
        Log.i(TAG, "sp2px:" + spValue + "sp-->" + pxValue + "px");
        
        return (int) (pxValue + 0.5f); 
    } 
    
    /**
     * 将sp值转换为dp值
     * @param context	Context，可以传getApplicationContext
     * @param spValue	sp值得大小
     * @return			dp值的大小
     * @author Mokee
     */
    public static int sp2dp(Context context, float spValue){
    	int dipValue = px2dip(context,sp2px(context,spValue));
        
        Log.i(TAG, "sp2dp:" + spValue + "sp-->" + dipValue + "dp");
        
        return dipValue; 
    }
    
    /**
     * 将sp值转换为dp值
     * @param context	Context，可以传getApplicationContext
     * @param dipValue	dp值得大小
     * @return			sp值的大小
     * @author Mokee
     */
    public static int dp2sp(Context context, float dipValue){
    	int spValue = px2sp(context,dip2px(context,dipValue));
        
        Log.i(TAG, "dp2sp:" + dipValue + "dp-->" + spValue + "sp");
        
        return spValue; 
    }
}

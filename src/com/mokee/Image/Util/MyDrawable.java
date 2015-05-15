package com.mokee.Image.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 用于加载大图像，缩放于控件
 */
class MyDrawable extends Drawable{
	private static final String TAG = MyDrawable.class.getName();

	private String path;
	private long time;
	
	public MyDrawable(String path){
		this.path = path;  
	}
	
	public int calculateInSampleSize(BitmapFactory.Options options,  
			int reqWidth, int reqHeight) {  
		// 源图片的高度和宽度  
		final int height = options.outHeight;  
		final int width = options.outWidth;  
		int inSampleSize = 1;  
		if (height > reqHeight || width > reqWidth) {  
			// 计算出实际宽高和目标宽高的比率  
			final int heightRatio = Math.round((float) height / (float) reqHeight);  
			final int widthRatio = Math.round((float) width / (float) reqWidth);  
			inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;  
		}  
		return inSampleSize;  
	} 
	@Override
	public void draw(Canvas canvas) {
		time = System.currentTimeMillis();
		try {
			int width = canvas.getWidth();
			int height = canvas.getHeight();
			if(path == null){
				return;
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);

			options.inSampleSize = calculateInSampleSize(options, width, height);
			options.inJustDecodeBounds = false;				
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);

			int imgWidth = bitmap.getWidth();
			int imgHeight = bitmap.getHeight();

			Rect src = new Rect(0, 0, imgWidth, imgHeight);

			float k1 = (imgWidth*1f) / (imgHeight*1f);
			float k2 = (width*1f)/(height*1f);

			Rect dest;
			if(k2 >= k1){
				float ratio = (height*1f)/(imgHeight*1f);
				int w = (int) (imgWidth*ratio);
				int offset = (width - w)/2;
				dest = new Rect(offset,0,w+offset,height);	
			}else{
				float ratio = (width*1f)/(imgWidth*1f);
				int h = (int)(imgHeight*ratio);
				int offset = (height - h)/2;
				dest = new Rect(0,offset,width,h + offset);
			}
			canvas.drawBitmap(bitmap, src, dest, null);
			bitmap.recycle();
			onDrawFinish(true);
		} catch (Exception e) {
			onDrawFinish(false);
		}
	}
	
	/**
	 * 设置MyDrawable的Draw完成的监听器
	 * 
	 * @author mokee
	 */
	public interface loadImageFinishListener {
		/**
		 * 加载图像的结果
		 * 
		 * @param loadResult
		 *            加载成功：true 加载失败：false
		 */
		void loadImageResult(boolean loadResult);
	};

	/**
	 * 设置MyDrawable的Draw完成的时间监听器
	 * 
	 * @author mokee
	 */
	public interface drawImageTimeListener {
		/**
		 * MyDrawable的Draw完成的时间
		 * 
		 * @param time
		 *            Draw完成的时间
		 */
		void drawImageFinishTime(long time);
	};
	
	private loadImageFinishListener loadImageFinishListener = null;
	private drawImageTimeListener drawImageTimeListener = null;
	
	public loadImageFinishListener getLoadImageFinishListener() {
		return loadImageFinishListener;
	}

	public void setLoadImageFinishListener(loadImageFinishListener LoadImageFinishListener) {
		this.loadImageFinishListener = LoadImageFinishListener;
	}
	
	public drawImageTimeListener getDrawImageTimeListener() {
		return drawImageTimeListener;
	}

	public void setDrawImageTimeListener(drawImageTimeListener timeListener) {
		this.drawImageTimeListener = timeListener;
	}

	protected void onDrawFinish(boolean success){
		time = System.currentTimeMillis() - time;
		
		if(loadImageFinishListener != null){
			loadImageFinishListener.loadImageResult(success);
		}
		
		if(drawImageTimeListener != null){
			drawImageTimeListener.drawImageFinishTime(time);
		}
		
		if(success){
			Log.i(TAG, "Drawable draw is success!");
		} else {
			Log.e(TAG, "Drawable draw is failed!");
		}
		Log.i(TAG, "处理的时间：	" + time / 1000 + " 秒");
	}

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	public void setAlpha(int arg0) {

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}
}
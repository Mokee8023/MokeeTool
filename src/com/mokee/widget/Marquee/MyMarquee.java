package com.mokee.widget.Marquee;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyMarquee extends TextView {
	private float speed = 3f;
	/**	0:自右向左	other:自左向右	*/
	private int direction = 0;
	private boolean isStarting;
	/** 文字颜色 以0x开头 */
	private int color;
	private volatile float offset = 0f;
	private volatile float textWidth = 0f;
	
	private OnMarqueeShiftOutListener mShiftOutListener = null; 

	public MyMarquee(Context context) {
		super(context);
		init();
	}

	public MyMarquee(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MyMarquee(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		setSingleLine(true);
		isStarting = true;
		color = getCurrentTextColor();
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {	
		Paint paint = getPaint();
		paint.setColor(getCurrentTextColor());
		textWidth= paint.measureText(getText().toString());
		offset = 0f;
		if (textWidth > 0) {
			isStarting = true;
		} else {
			isStarting = false;
		}
		
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		float posx, posy;
		
		if(direction == 0){
			posx = getWidth() - offset;
		} else {
			posx = offset;
		}
		posy = getBaseline();
		canvas.drawText(getText().toString(), posx, posy, getPaint());
		if (!isStarting) { return; }
		offset += speed;
		
		if((textWidth < 1) || (offset >= getWidth() + textWidth)){
			offset = 0.0f;
			if(mShiftOutListener != null){
				mShiftOutListener.onShiftOut(this);
			}else{
				this.onShiftOut();
			}
		}
		
		invalidate();
	}
	
	/**
	 * 获取文字暂停或者滚动
	 * @return	true:滚动	false:暂停
	 */
	public boolean getIsStarting() {
		return isStarting;
	}

	/**
	 * 设置暂停或者滚动
	 * @param isStarting	true:滚动	false:暂停
	 */
	public void setIsStarting(boolean isStarting) {
		this.isStarting = isStarting;
		invalidate();
	}
	
	/**
	 * 获取文字的滚动速度 默认为1.0f
	 * @return	文字滚动速度
	 */
	public float getSpeed() {
		return speed;
	}
	
	/**
	 * 设置文字的滚动速度 默认为1.0f
	 * @param speed		speed
	 */
	public void setSpeed(float speed){
		if(speed < 1.0f){
			this.speed = 1.0f;
		}else{
			this.speed = speed;
		}
		
		invalidate();
	}
	
	/**
	 * 获取文字的滚动方向
	 * @return	0:自右向左	1:自左向右
	 */
	public int getDirection() {
		return direction;
	}
	
	/**
	 * 设置文字的滚动方向
	 * @param direction		0:自右向左	1:自左向右
	 */
	public void setDirection(int direction) {
		offset = 0f;
		if(direction != 1 && direction != 0){
			this.direction = 0;
		} else {
			this.direction = direction;
		}
		
		invalidate();
	}
	
	/**
	 * 获得当前文字的颜色
	 * @return
	 */
	public int getColor() {
		return color;
	}

	/**
	 * 设置滚动的文字的颜色
	 * @param color	颜色	: 已0x开头
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
	/** 文字滚动周期完成接口 */
	public interface OnMarqueeShiftOutListener {
		void onShiftOut(MyMarquee v);
	}
	
	/**
	 * 监听文字滚动一个周期完成
	 * @param listener
	 */
	public void setOnMarqueeShiftOutListener(OnMarqueeShiftOutListener listener){
		this.mShiftOutListener = listener;
	}
	
	protected void onShiftOut(){}
}

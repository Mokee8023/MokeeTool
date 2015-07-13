package com.mokee.application.Bomber;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.smssdk.SMSSDK;

import com.mokee.application.API.API;
import com.mokee.application.PhoneNumber.MobileService;
import com.mokee.database.SPSetting.MyValuesInt;
import com.mokee.database.SPSetting.MyValuesString;
import com.mokee.tools.R;
import com.mokee.widget.CircleProgress.CircleProgress;

public class BomberActivity extends Activity implements OnClickListener {
	private static final String tag = "BomberActivity";
	
	private ImageButton ib_Return;
	private Button btn_BomberStart;
	private TextView activity_Text;
	private EditText et_BomberNum, et_BomberPhone;
	
	private Dialog process;
	String phone;
	int num;
	
	private Handler bomberHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case API.GET_PHONE_INFORMATION:
				process.dismiss();
				Log.i(tag, "查询到的电话信息："+msg.obj.toString());
				if (msg.obj == null) {
					Toast.makeText(BomberActivity.this, "Query failed!", Toast.LENGTH_SHORT).show();
				} else {
					String phoneInfo = msg.obj.toString();
					if(phoneInfo.contains("移动") || phoneInfo.contains("电信") || phoneInfo.contains("联通")){
						Log.i(tag, "开始Bomber,号码为：" + phoneInfo.substring(0, 11));
						BomberService bomber = new BomberService(bomberHandler, getApplicationContext(), phoneInfo.substring(0, 11), num);
						bomber.start();
					} else {
						Toast.makeText(BomberActivity.this, "此号码非三大运营商号码", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case API.MOB_BOMBER_SERVICE:
				Log.i(tag, "Bomber Result:" + msg.obj.toString());
				Toast.makeText(BomberActivity.this, msg.toString(), Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bomber);
		
		initialSMSNumber();
		init();
	}

	private void init() {
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		btn_BomberStart = (Button) findViewById(R.id.btn_BomberStart);
		activity_Text = (TextView) findViewById(R.id.activity_Text);
		et_BomberNum = (EditText) findViewById(R.id.et_BomberNum);
		et_BomberPhone = (EditText) findViewById(R.id.et_BomberPhone);
		
		activity_Text.setText("Hacker Bomber");
		
		ib_Return.setOnClickListener(this);
		btn_BomberStart.setOnClickListener(this);
	}

	/** 初始化短信条数,每天200条 */
	private void initialSMSNumber() {
		String nowTime = (String) DateFormat.format("yyyy-MM-dd", System.currentTimeMillis());
		if(!MyValuesString.getBomberTime().equals(nowTime)){
			MyValuesInt.setSmsNum(200);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_Return:
			finish();
			onDestroy();
			break;
		case R.id.btn_BomberStart:
			phone = et_BomberPhone.getText().toString().trim();
			num = Integer.valueOf(et_BomberNum.getText().toString().trim());
			if(phone != null && !phone.equals("") && phone.length() == 11){
				if(num > 0 && num <= MyValuesInt.getSmsNum()){
					MobileService getPhoneInfo = new MobileService(bomberHandler, phone);
					getPhoneInfo.start();
					
					process = CircleProgress.createCircleProgressDialog(this, "Query");
					process.show();
				} else {
					Toast.makeText(getApplicationContext(), "请输入0~" + MyValuesInt.getSmsNum() + "之间的次数", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}
}

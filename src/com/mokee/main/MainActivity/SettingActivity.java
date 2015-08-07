package com.mokee.main.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.mokee.database.SPSetting.MyValuesBool;
import com.mokee.tools.R;

public class SettingActivity extends Activity implements OnCheckedChangeListener {
	private Switch sw_Sweep;
	private Switch sw_GenerateQrCode;
	private Switch sw_PhoneNumber;
	private Switch sw_IdCard;
	private Switch sw_GoodsPrice;
	private Switch sw_Translation;
	private Switch sw_Express;
	private Switch sw_RobotChat;
	private Switch sw_Socket;
	private Switch sw_Bomber;
	private Switch sw_QuitWindow;
	
	private ImageButton ib_Return;
	private TextView activity_Text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		initView();
	}

	private void initView() {
		
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		activity_Text = (TextView) findViewById(R.id.activity_Text);
		
		sw_Sweep = (Switch) findViewById(R.id.sw_Sweep);
		sw_GenerateQrCode = (Switch) findViewById(R.id.sw_GenerateQrCode);
		sw_PhoneNumber = (Switch) findViewById(R.id.sw_PhoneNumber);
		sw_IdCard = (Switch) findViewById(R.id.sw_IdCard);
		sw_GoodsPrice = (Switch) findViewById(R.id.sw_GoodsPrice);
		sw_Translation = (Switch) findViewById(R.id.sw_Translation);
		sw_Express = (Switch) findViewById(R.id.sw_Express);
		sw_RobotChat = (Switch) findViewById(R.id.sw_RobotChat);
		sw_Socket = (Switch) findViewById(R.id.sw_Socket);
		sw_Bomber = (Switch) findViewById(R.id.sw_Bomber);
		sw_QuitWindow = (Switch) findViewById(R.id.sw_QuitWindow);
		
		activity_Text.setText(R.string.action_settings);
		
		sw_Sweep.setChecked(MyValuesBool.isSweep());
		sw_GenerateQrCode.setChecked(MyValuesBool.isGenerateQrCode());
		sw_PhoneNumber.setChecked(MyValuesBool.isPhoneNumber());
		sw_IdCard.setChecked(MyValuesBool.isIdCard());
		sw_GoodsPrice.setChecked(MyValuesBool.isGoodsPrice());
		sw_Translation.setChecked(MyValuesBool.isTranslation());
		sw_Express.setChecked(MyValuesBool.isExpress());
		sw_RobotChat.setChecked(MyValuesBool.isRobotChat());
		sw_Socket.setChecked(MyValuesBool.isSocketDebug());
		sw_Bomber.setChecked(MyValuesBool.isHackerBomber());
		
		ib_Return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		sw_Sweep.setOnCheckedChangeListener(this);
		sw_GenerateQrCode.setOnCheckedChangeListener(this);
		sw_PhoneNumber.setOnCheckedChangeListener(this);
		sw_IdCard.setOnCheckedChangeListener(this);
		sw_GoodsPrice.setOnCheckedChangeListener(this);
		sw_Translation.setOnCheckedChangeListener(this);
		sw_RobotChat.setOnCheckedChangeListener(this);
		sw_Express.setOnCheckedChangeListener(this);
		sw_Socket.setOnCheckedChangeListener(this);
		sw_Bomber.setOnCheckedChangeListener(this);
		sw_QuitWindow.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.sw_Sweep:
			MyValuesBool.setIsSweep(isChecked);
			break;
		case R.id.sw_GenerateQrCode:
			MyValuesBool.setIsGenerateQrCode(isChecked);
			break;
		case R.id.sw_PhoneNumber:
			MyValuesBool.setIsPhoneNumber(isChecked);
			break;
		case R.id.sw_IdCard:
			MyValuesBool.setIsIdCard(isChecked);
			break;
		case R.id.sw_GoodsPrice:
			MyValuesBool.setIsGoodsPrice(isChecked);
			break;
		case R.id.sw_Translation:
			MyValuesBool.setIsTranslation(isChecked);
			break;
		case R.id.sw_RobotChat:
			MyValuesBool.setIsRobotChat(isChecked);
			break;
		case R.id.sw_Express:
			MyValuesBool.setIsExpress(isChecked);
			break;
		case R.id.sw_Socket:
			MyValuesBool.setIsSocketDebug(isChecked);
			break;
		case R.id.sw_Bomber:
			MyValuesBool.setIsHackerBomber(isChecked);
			break;
		case R.id.sw_QuitWindow:
			MyValuesBool.setIsQuitWindow(isChecked);
			break;

		default:
			break;
		}
	}
}

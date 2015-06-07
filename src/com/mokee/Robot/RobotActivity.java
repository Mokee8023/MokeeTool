package com.mokee.Robot;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.NetConnect.HttpGetThread_Apache;
import com.mokee.tools.R;

public class RobotActivity extends Activity implements OnClickListener, OnLongClickListener {
	private static final String TAG = "RobotActivity";
	private Button btn_SendChat;
	private ImageButton ib_Return, ib_Save, ib_ClearChat;
	private TextView activity_Text, tv_RobotData;
	private EditText et_ChatData;
	private ScrollView sv_ScrollView;

	StringBuilder sb_Robot = new StringBuilder("");
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case API.SHOWAPI_ROBOT_CHAT:
				
				Bundle result = (Bundle) msg.obj;
				String text;
				if(result.getBoolean("result")){
					text = ShowAPIChatFunction.getAnalyzeShowAPIChat(result.getString("data"));
				} else {
					text = result.getString("data");
				}
				// Html.fromHtml("<font color='blue'><b>" + text + "</b></font>");
				sb_Robot.append("Robot: ").append(text).append("\n\n");
				tv_RobotData.setText(sb_Robot.toString());
				scrollToBottom(sv_ScrollView, tv_RobotData);
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
		setContentView(R.layout.activity_robot);

		initView();
		initEvent();
	}

	private void initView() {
		btn_SendChat = (Button) findViewById(R.id.btn_SendChat);
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		ib_Save = (ImageButton) findViewById(R.id.ib_Save);
		ib_ClearChat = (ImageButton) findViewById(R.id.ib_ClearChat);
		activity_Text = (TextView) findViewById(R.id.activity_Text);
		tv_RobotData = (TextView) findViewById(R.id.tv_RobotData);
		et_ChatData = (EditText) findViewById(R.id.et_ChatData);
		sv_ScrollView = (ScrollView) findViewById(R.id.sv_ScrollView);
		
		activity_Text.setText("Robot Chat");
		ib_Save.setVisibility(View.VISIBLE);
	}

	private void initEvent() {
		btn_SendChat.setOnClickListener(this);
		ib_Return.setOnClickListener(this);
		ib_Save.setOnClickListener(this);
		ib_ClearChat.setOnClickListener(this);
		
		et_ChatData.setOnClickListener(this);
		
		tv_RobotData.setOnLongClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_SendChat:
			String chat = et_ChatData.getText().toString().trim();
			if(!chat.equals("")){
				et_ChatData.setText("");
				sb_Robot.append("Me: ").append(chat).append("\n\n");
				tv_RobotData.setText(sb_Robot.toString());
				scrollToBottom(sv_ScrollView, tv_RobotData);
				ib_ClearChat.setVisibility(View.VISIBLE);
				
				HttpGetThread_Apache chatThread = new HttpGetThread_Apache(mHandler, getChatURL(chat), API.SHOWAPI_ROBOT_CHAT);
				chatThread.start();
			} else {
				Toast.makeText(getApplicationContext(), "Chat content is empty.", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.ib_Save:
			if(!sb_Robot.toString().equals("")){
				String saveResult = ShowAPIChatFunction.SaveRobotChatText(this, sb_Robot.toString());
				if(saveResult != null){
					Toast.makeText(getApplicationContext(), "Chat save to:" + saveResult, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "Save Failed.", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "It's like not talking yet.", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
		case R.id.ib_Return:
			finish();
			onDestroy();
			break;
			
		case R.id.ib_ClearChat:
			Animation anim = AnimationUtils.loadAnimation(this, R.anim.image_button_anim);
			ib_ClearChat.setAnimation(anim);
			sb_Robot.delete(0, sb_Robot.length());
			ib_ClearChat.setVisibility(View.INVISIBLE);
			tv_RobotData.setText(sb_Robot.toString());
			break;
			
		case R.id.et_ChatData:
//			InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//			if(imm.isActive()){
//				scrollToBottom(sv_ScrollView, tv_RobotData);
//			}
			scrollToBottom(sv_ScrollView, tv_RobotData);
			break;

		default:
			break;
		}
	}
	
	private String getChatURL(String chat) {
		StringBuilder sb = new StringBuilder("http://route.showapi.com/60-27?");
		sb.append("showapi_appid=");
		sb.append(API.ShOWAPI_KUAIDI_appid);
		sb.append("&showapi_sign=simple_");
		sb.append(API.ShOWAPI_KUAIDI_secret);
		sb.append("&showapi_timestamp=");
		sb.append(DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()));
		sb.append("&info=");
		sb.append(chat);
		
		String url = sb.toString();
//		try {
//			url = URLEncoder.encode(url, "utf-8");
//		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
//			Log.e(TAG, "RobotActivity.UnsupportedEncodingException:"+e.toString());
//		}
		url = url.replaceAll(" ", "%20");
		url = url.replaceAll("\n", "%0a");
		Log.i(TAG, "getChatURL.ShowAPIChatThread.getChatURL:" + url);
		return url;
	}
	
	public static void scrollToBottom(final View scroll, final View inner) {
		Handler mHandler = new Handler();

		mHandler.post(new Runnable() {
			public void run() {
				if (scroll == null || inner == null) { return; }

				int offset = inner.getMeasuredHeight() - scroll.getHeight();
				if (offset < 0) {
					offset = 0;
				}

				scroll.scrollTo(0, offset);
			}
		});
	}

	@Override
	public boolean onLongClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_RobotData:
			ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			cbm.setText(tv_RobotData.getText().toString().trim());
			Toast.makeText(this, "Chat data has been copied.", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		return false;
	}

}

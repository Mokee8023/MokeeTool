package com.mokee.Robot;

import android.app.Activity;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.NetConnect.HttpGetThread_Apache;
import com.mokee.tools.R;

public class RobotActivity extends Activity implements OnClickListener {
	private static final String TAG = "RobotActivity";
	private Button btn_SendChat;
	private ImageButton ib_Return, ib_Save;
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
				HttpGetThread_Apache chatThread = new HttpGetThread_Apache(mHandler, getChatURL(chat), API.SHOWAPI_ROBOT_CHAT);
				chatThread.start();
			} else {
				Toast.makeText(getApplicationContext(), "Chat content is empty.", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.ib_Return:
			finish();
			onDestroy();
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
		url = url.replaceAll(" ", "%20");
		Log.i(TAG, "ShowAPIChatThread.getChatURL:" + url);
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

}

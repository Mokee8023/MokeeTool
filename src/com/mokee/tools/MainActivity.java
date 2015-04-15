package com.mokee.tools;

import com.mokee.MobileService.MobileService;
import com.mokee.TimeService.TimeService;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		OnLongClickListener {
	private final String TAG = "MainActivity";

	private EditText et_PhoneNumbers;
	private ImageButton ib_QueryPhone;
	private TextView tv_PhoneInformation;
	private TextView tv_SystemTime;

	private static final int GET_PHONE_INFORMATION = 0;// WebService获取Phone信息
	private static final int TIMESERVICE = 1;// 系统时间获取

	private Handler MyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case GET_PHONE_INFORMATION:
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(), "Query failed!",
							Toast.LENGTH_SHORT).show();
				} else {
					tv_PhoneInformation.setText(msg.obj.toString());
				}
				break;
			case TIMESERVICE:
				tv_SystemTime.setText(msg.obj.toString());
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initThread();
	}

	private void initThread() {
		new TimeService(MyHandler).start();
	}

	private void initView() {
		et_PhoneNumbers = (EditText) findViewById(R.id.et_PhoneNumbers);
		ib_QueryPhone = (ImageButton) findViewById(R.id.ib_QueryPhone);
		tv_PhoneInformation = (TextView) findViewById(R.id.tv_PhoneInformation);
		tv_SystemTime = (TextView) findViewById(R.id.tv_SystemTime);

		tv_PhoneInformation.setOnLongClickListener(this);
		tv_SystemTime.setOnLongClickListener(this);

		ib_QueryPhone.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_QueryPhone:
			String phoneNumbers;
			phoneNumbers = et_PhoneNumbers.getText().toString().trim();
			if (phoneNumbers == null || phoneNumbers.isEmpty()) {
				Toast.makeText(getApplicationContext(),
						"Please Input Phone Numbers!", Toast.LENGTH_SHORT)
						.show();
			} else if (phoneNumbers.length() < 11 || phoneNumbers.length() > 11) {
				Toast.makeText(getApplicationContext(),
						"Please Input 11-digit Number!", Toast.LENGTH_SHORT)
						.show();
			} else {
				MobileService phone = new MobileService(MyHandler, phoneNumbers);
				phone.start();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View view) {
		ClipboardManager cbm = (ClipboardManager) MainActivity.this
				.getSystemService(Context.CLIPBOARD_SERVICE);
		switch (view.getId()) {
		case R.id.tv_PhoneInformation:
			cbm.setText(tv_PhoneInformation.getText().toString().trim());
			Toast.makeText(getApplicationContext(),
					"Phone information has been copied!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.tv_SystemTime:
			cbm.setText(tv_SystemTime.getText().toString().trim());
			Toast.makeText(getApplicationContext(),
					"System time has been copied!", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;

		}

		return false;
	}
}

package com.mokee.tools;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.Help.HelpActivity;
import com.mokee.MobileService.MobileService;
import com.mokee.TimeService.TimeService;

public class MainActivity extends Activity implements OnClickListener,
		OnLongClickListener {
	private final String TAG = "MainActivity";

	private EditText et_PhoneNumbers;
	private ImageButton ib_QueryPhone;
	private TextView tv_PhoneInformation;
	private TextView tv_SystemTime;
	private ImageView iv_Contact;

	private static final int GET_PHONE_INFORMATION = 0;// WebService获取Phone信息
	private static final int TIMESERVICE = 1;// 系统时间获取
	private static final int GET_PHONE = 2;// 获取联系人

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
		iv_Contact = (ImageView) findViewById(R.id.iv_Contact);

		tv_PhoneInformation.setOnLongClickListener(this);
		tv_SystemTime.setOnLongClickListener(this);

		ib_QueryPhone.setOnClickListener(this);
		iv_Contact.setOnClickListener(this);
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
				MobileService getPhoneInfo = new MobileService(MyHandler,
						phoneNumbers);
				getPhoneInfo.start();
			}
			break;
		case R.id.iv_Contact:
			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, GET_PHONE);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onLongClick(View view) {
		ClipboardManager cbm = (ClipboardManager) MainActivity.this
				.getSystemService(Context.CLIPBOARD_SERVICE);
		switch (view.getId()) {
		case R.id.tv_PhoneInformation:
			String phone_Information = tv_PhoneInformation.getText().toString()
					.trim();

			if (!phone_Information.equals("")) {
				cbm.setText(tv_PhoneInformation.getText().toString().trim());
				Toast.makeText(getApplicationContext(),
						"Phone information has been copied!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Phone information is Empty!", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.tv_SystemTime:
			cbm.setText(tv_SystemTime.getText().toString().trim());
			Toast.makeText(getApplicationContext(),
					"System time has been copied!", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;

		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		/**
		 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 4、文本，菜单的显示文本
		 */
		menu.add(Menu.NONE, Menu.FIRST, 0, "User Manual");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, HelpActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case GET_PHONE:
			if (resultCode == RESULT_OK) {
				Uri contact = data.getData();
				Cursor cursor = managedQuery(contact, null, null, null, null);
				cursor.moveToFirst();
				String phone_Numbers = getContactPhone(cursor);
				if (phone_Numbers.equals("")) {
					Toast.makeText(getApplicationContext(),
							"This contact no phone number!", Toast.LENGTH_SHORT)
							.show();
				} else {
					et_PhoneNumbers.setText(phone_Numbers);
				}
			}
			break;
		default:
			break;
		}
	}

	private String getContactPhone(Cursor cursor) {
		int hasPhoneColumn = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int hasPhone = cursor.getInt(hasPhoneColumn);
		String result = "";
		if (hasPhone > 0) {// 有联系人
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获取联系人的电话
			Cursor phone = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
							+ contactId, null, null);
			if (phone.moveToFirst()) {
				for (; !phone.isAfterLast(); phone.moveToNext()) {
					int index = phone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					String phoneNumber = phone.getString(index);
					result = phoneNumber;
				}
				if (!phone.isClosed()) {
					phone.close();
				}
			}
		}
		return result;
	}
}

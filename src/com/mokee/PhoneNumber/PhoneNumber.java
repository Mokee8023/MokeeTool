package com.mokee.PhoneNumber;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.MobileService.MobileService;
import com.mokee.tools.R;

public class PhoneNumber extends Activity implements OnLongClickListener,
		OnClickListener {

	private EditText et_PhoneNumbers;
	private ImageButton ib_QueryPhone;
	private Button btn_QueryPhoneCancle;
	private TextView tv_PhoneInformation;
	private ImageView iv_Contact;

	private Handler MyPhoneHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case API.GET_PHONE_INFORMATION:
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(), "Query failed!",
							Toast.LENGTH_SHORT).show();
				} else {
					tv_PhoneInformation.setText(msg.obj.toString());
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setFinishOnTouchOutside(false);// 设置点击Dialog之外空白区域不关闭Dialog
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_phone);

		initView();
	}

	private void initView() {
		et_PhoneNumbers = (EditText) findViewById(R.id.et_PhoneNumbers);
		ib_QueryPhone = (ImageButton) findViewById(R.id.ib_QueryPhone);
		btn_QueryPhoneCancle = (Button) findViewById(R.id.btn_QueryPhoneCancle);
		tv_PhoneInformation = (TextView) findViewById(R.id.tv_PhoneInformation);
		iv_Contact = (ImageView) findViewById(R.id.iv_Contact);

		tv_PhoneInformation.setOnLongClickListener(this);

		ib_QueryPhone.setOnClickListener(this);
		iv_Contact.setOnClickListener(this);
		btn_QueryPhoneCancle.setOnClickListener(this);
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
				MobileService getPhoneInfo = new MobileService(MyPhoneHandler,
						phoneNumbers);
				getPhoneInfo.start();
			}
			break;
		case R.id.iv_Contact:
			et_PhoneNumbers.setText("");
			tv_PhoneInformation.setText("");

			Intent intent = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(intent, API.GET_PHONE);
			break;
		case R.id.btn_QueryPhoneCancle:
			finish();
			onDestroy();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View view) {

		ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		switch (view.getId()) {
		case R.id.tv_PhoneInformation:
			String phone_Information = tv_PhoneInformation.getText().toString()
					.trim();

			if (!phone_Information.equals("")) {
				cbm.setText(phone_Information);
				Toast.makeText(getApplicationContext(),
						"Phone information has been copied!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Phone information is Empty!", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		default:
			break;

		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case API.GET_PHONE:
			if (resultCode == RESULT_OK) {
				Uri contact = data.getData();
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(contact, null, null, null, null);
				cursor.moveToFirst();
				String phone_Numbers = getContactPhone(cursor);
				if (phone_Numbers.equals("")) {
					Toast.makeText(getApplicationContext(),
							"This contact no phone number!", Toast.LENGTH_SHORT)
							.show();
				} else {
					et_PhoneNumbers.setText(phone_Numbers);
					MobileService getPhoneInfo = new MobileService(
							MyPhoneHandler, phone_Numbers);
					getPhoneInfo.start();
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

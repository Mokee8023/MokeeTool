package com.mokee.Fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.MobileService.MobileService;
import com.mokee.TimeService.TimeService;
import com.mokee.tools.R;

public class HomeFragment extends Fragment implements OnLongClickListener,
		OnClickListener {

	private EditText et_PhoneNumbers;
	private ImageButton ib_QueryPhone;
	private TextView tv_PhoneInformation;
	private TextView tv_SystemTime;
	private ImageView iv_Contact;

	private Handler MyHomeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case API.GET_PHONE_INFORMATION:
				if (msg.obj == null) {
					Toast.makeText(getActivity(), "Query failed!",
							Toast.LENGTH_SHORT).show();
				} else {
					tv_PhoneInformation.setText(msg.obj.toString());
				}
				break;
			case API.TIMESERVICE:
				tv_SystemTime.setText(msg.obj.toString());
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home, container, false);

		et_PhoneNumbers = (EditText) view.findViewById(R.id.et_PhoneNumbers);
		ib_QueryPhone = (ImageButton) view.findViewById(R.id.ib_QueryPhone);
		tv_PhoneInformation = (TextView) view
				.findViewById(R.id.tv_PhoneInformation);
		tv_SystemTime = (TextView) view.findViewById(R.id.tv_SystemTime);
		iv_Contact = (ImageView) view.findViewById(R.id.iv_Contact);

		tv_PhoneInformation.setOnLongClickListener(this);
		tv_SystemTime.setOnLongClickListener(this);

		ib_QueryPhone.setOnClickListener(this);
		iv_Contact.setOnClickListener(this);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initThread();// 时间线程
	}

	private void initThread() {
		new TimeService(MyHomeHandler).start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onLongClick(View view) {

		ClipboardManager cbm = (ClipboardManager) getActivity()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		switch (view.getId()) {
		case R.id.tv_PhoneInformation:
			String phone_Information = tv_PhoneInformation.getText().toString()
					.trim();

			if (!phone_Information.equals("")) {
				cbm.setText(tv_PhoneInformation.getText().toString().trim());
				Toast.makeText(getActivity(),
						"Phone information has been copied!",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Phone information is Empty!",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_SystemTime:
			cbm.setText(tv_SystemTime.getText().toString().trim());
			Toast.makeText(getActivity(), "System time has been copied!",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;

		}
		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_QueryPhone:
			String phoneNumbers;
			phoneNumbers = et_PhoneNumbers.getText().toString().trim();
			if (phoneNumbers == null || phoneNumbers.isEmpty()) {
				Toast.makeText(getActivity(), "Please Input Phone Numbers!",
						Toast.LENGTH_SHORT).show();
			} else if (phoneNumbers.length() < 11 || phoneNumbers.length() > 11) {
				Toast.makeText(getActivity(), "Please Input 11-digit Number!",
						Toast.LENGTH_SHORT).show();
			} else {
				MobileService getPhoneInfo = new MobileService(MyHomeHandler,
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
		default:
			break;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case API.GET_PHONE:
			if (resultCode == getActivity().RESULT_OK) {
				Uri contact = data.getData();
				@SuppressWarnings("deprecation")
				Cursor cursor = getActivity().managedQuery(contact, null, null,
						null, null);
				cursor.moveToFirst();
				String phone_Numbers = getContactPhone(cursor);
				if (phone_Numbers.equals("")) {
					Toast.makeText(getActivity(),
							"This contact no phone number!", Toast.LENGTH_SHORT)
							.show();
				} else {
					et_PhoneNumbers.setText(phone_Numbers);
					MobileService getPhoneInfo = new MobileService(MyHomeHandler,
							phone_Numbers);
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
			Cursor phone = getActivity().getContentResolver().query(
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

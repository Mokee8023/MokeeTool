package com.mokee.tools;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mokee.Fragment.HomeFragment;
import com.mokee.Fragment.OtherFragment;
import com.mokee.Fragment.TranslationFragment;

public class MainActivity extends FragmentActivity {
	private final String TAG = "MainActivity";
	
	private ViewPager viewPager;
	
	private ImageButton ib_Translation;
	private ImageButton ib_Home;
	private ImageButton ib_Other;
	
	private TextView tv_Translation;
	private TextView tv_Home;
	private TextView tv_Other;
	
	private HomeFragment homeFragment;
	private TranslationFragment translationFragment;
	private OtherFragment otherFragment;
	
	private List<Fragment> list;

//	private EditText et_PhoneNumbers;
//	private ImageButton ib_QueryPhone;
//	private TextView tv_PhoneInformation;
//	private TextView tv_SystemTime;
//	private ImageView iv_Contact;

//	private static final int GET_PHONE_INFORMATION = 0;// WebService获取Phone信息
//	private static final int TIMESERVICE = 1;// 系统时间获取
//	private static final int GET_PHONE = 2;// 获取联系人
//
//	private Handler MyHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//
//			switch (msg.what) {
//			case GET_PHONE_INFORMATION:
//				if (msg.obj == null) {
//					Toast.makeText(getApplicationContext(), "Query failed!",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					tv_PhoneInformation.setText(msg.obj.toString());
//				}
//				break;
//			case TIMESERVICE:
//				tv_SystemTime.setText(msg.obj.toString());
//				break;
//			default:
//				break;
//			}
//		}
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
//		initEnvents();
//		initThread();
	}

//	private void initEnvents() {
//		
//	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.vp_ViewPager);
		
		ib_Translation = (ImageButton) findViewById(R.id.ib_Translation);
		ib_Home = (ImageButton) findViewById(R.id.ib_Home);
		ib_Other = (ImageButton) findViewById(R.id.ib_Other);
		
		tv_Translation = (TextView) findViewById(R.id.tv_Translation);
		tv_Home = (TextView) findViewById(R.id.tv_Home);
		tv_Other = (TextView) findViewById(R.id.tv_Other);
		
		list = new ArrayList<Fragment>();
		
		HomeFragment home = new HomeFragment();
		TranslationFragment translation = new TranslationFragment();
		OtherFragment other = new OtherFragment();
		
		list.add(home);
		list.add(translation);
		list.add(other);
		
		FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return list.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return list.get(arg0);
			}
		};
		
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				setOriginState();
				setSelectedPager(position);
			}
	@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});		
		
	}
	
	public void setOriginState() {
		
	}
	
	public void setSelectedPager(int position) {
		
	}

//	private void initThread() {
//		new TimeService(MyHandler).start();
//	}

//	private void initView() {
//		et_PhoneNumbers = (EditText) findViewById(R.id.et_PhoneNumbers);
//		ib_QueryPhone = (ImageButton) findViewById(R.id.ib_QueryPhone);
//		tv_PhoneInformation = (TextView) findViewById(R.id.tv_PhoneInformation);
//		tv_SystemTime = (TextView) findViewById(R.id.tv_SystemTime);
//		iv_Contact = (ImageView) findViewById(R.id.iv_Contact);
//
//		tv_PhoneInformation.setOnLongClickListener(this);
//		tv_SystemTime.setOnLongClickListener(this);
//
//		ib_QueryPhone.setOnClickListener(this);
//		iv_Contact.setOnClickListener(this);
		
		
//		viewPager = (ViewPager) findViewById(R.id.vp_ViewPager);
//		ib_Translation = (ImageButton) findViewById(R.id.ib_Translation);
//		ib_Home = (ImageButton) findViewById(R.id.ib_Home);
//		ib_Other = (ImageButton) findViewById(R.id.ib_Other);
//		
//		list = new ArrayList<Fragment>();
//		
//		HomeFragment home = new HomeFragment();
//		TranslationFragment translation = new TranslationFragment();
//		OtherFragment other = new OtherFragment();
//		
//		list.add(home);
//		list.add(translation);
//		list.add(other);
//		
//		FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
//			
//			@Override
//			public int getCount() {
//				return list.size();
//			}
//			
//			@Override
//			public Fragment getItem(int arg0) {
//				return list.get(arg0);
//			}
//		};
//		
//		viewPager.setAdapter(adapter);
//		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int arg0) {
//				
//			}
//			
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				
//			}
//		});
//	}

//	@Override
//	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.ib_QueryPhone:
//			String phoneNumbers;
//			phoneNumbers = et_PhoneNumbers.getText().toString().trim();
//			if (phoneNumbers == null || phoneNumbers.isEmpty()) {
//				Toast.makeText(getApplicationContext(),
//						"Please Input Phone Numbers!", Toast.LENGTH_SHORT)
//						.show();
//			} else if (phoneNumbers.length() < 11 || phoneNumbers.length() > 11) {
//				Toast.makeText(getApplicationContext(),
//						"Please Input 11-digit Number!", Toast.LENGTH_SHORT)
//						.show();
//			} else {
//				MobileService getPhoneInfo = new MobileService(MyHandler,
//						phoneNumbers);
//				getPhoneInfo.start();
//			}
//			break;
//		case R.id.iv_Contact:
//			et_PhoneNumbers.setText("");
//			tv_PhoneInformation.setText("");
//			
//			Intent intent = new Intent(Intent.ACTION_PICK,
//					ContactsContract.Contacts.CONTENT_URI);
//			startActivityForResult(intent, GET_PHONE);
//			break;
//		default:
//			break;
//		}
//	}
//
//	@SuppressWarnings("deprecation")
//	@Override
//	public boolean onLongClick(View view) {
//		ClipboardManager cbm = (ClipboardManager) MainActivity.this
//				.getSystemService(Context.CLIPBOARD_SERVICE);
//		switch (view.getId()) {
//		case R.id.tv_PhoneInformation:
//			String phone_Information = tv_PhoneInformation.getText().toString()
//					.trim();
//
//			if (!phone_Information.equals("")) {
//				cbm.setText(tv_PhoneInformation.getText().toString().trim());
//				Toast.makeText(getApplicationContext(),
//						"Phone information has been copied!",
//						Toast.LENGTH_SHORT).show();
//			} else {
//				Toast.makeText(getApplicationContext(),
//						"Phone information is Empty!", Toast.LENGTH_SHORT)
//						.show();
//			}
//			break;
//		case R.id.tv_SystemTime:
//			cbm.setText(tv_SystemTime.getText().toString().trim());
//			Toast.makeText(getApplicationContext(),
//					"System time has been copied!", Toast.LENGTH_SHORT).show();
//			break;
//		default:
//			break;
//
//		}
//		return true;
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		/**
//		 * add()方法的四个参数，依次是： 1、组别，如果不分组的话就写Menu.NONE,
//		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单 3、顺序，那个菜单现在在前面由这个参数的大小决定
//		 * 4、文本，菜单的显示文本
//		 */
//		menu.add(Menu.NONE, Menu.FIRST, 0, "User Manual");
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case Menu.FIRST:
//			Intent intent = new Intent();
//			intent.setClass(MainActivity.this, HelpActivity.class);
//			startActivity(intent);
//			break;
//		default:
//			break;
//		}
//		return true;
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		switch (requestCode) {
//		case GET_PHONE:
//			if (resultCode == RESULT_OK) {
//				Uri contact = data.getData();
//				Cursor cursor = managedQuery(contact, null, null, null, null);
//				cursor.moveToFirst();
//				String phone_Numbers = getContactPhone(cursor);
//				if (phone_Numbers.equals("")) {
//					Toast.makeText(getApplicationContext(),
//							"This contact no phone number!", Toast.LENGTH_SHORT)
//							.show();
//				} else {
//					et_PhoneNumbers.setText(phone_Numbers);
//					MobileService getPhoneInfo = new MobileService(MyHandler,
//							phone_Numbers);
//					getPhoneInfo.start();
//				}
//			}
//			break;
//		default:
//			break;
//		}
//	}
//
//	private String getContactPhone(Cursor cursor) {
//		int hasPhoneColumn = cursor
//				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
//		int hasPhone = cursor.getInt(hasPhoneColumn);
//		String result = "";
//		if (hasPhone > 0) {// 有联系人
//			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
//			String contactId = cursor.getString(idColumn);
//			// 获取联系人的电话
//			Cursor phone = getContentResolver().query(
//					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//					null,
//					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
//							+ contactId, null, null);
//			if (phone.moveToFirst()) {
//				for (; !phone.isAfterLast(); phone.moveToNext()) {
//					int index = phone
//							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//					String phoneNumber = phone.getString(index);
//					result = phoneNumber;
//				}
//				if (!phone.isClosed()) {
//					phone.close();
//				}
//			}
//		}
//		return result;
//	}
}

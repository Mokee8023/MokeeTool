package com.mokee.application.IdCard;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.application.API.API;
import com.mokee.application.PhoneNumber.BaiduAPIMobileInformation;
import com.mokee.application.PhoneNumber.MobileService;
import com.mokee.database.SPSetting.MyValuesInt;
import com.mokee.main.MainActivity.MainActivity;
import com.mokee.tools.R;
import com.mokee.widget.CircleProgress.CircleProgress;

public class QueryIdCardInfoActivity extends Activity implements OnClickListener,
		OnLongClickListener {
	private static final String TAG = "QueryIdCardInfoActivity";

	private EditText et_IdCard;
	private ImageButton ib_QueryIdCard, btn_Return;
	private TextView tv_IdCardInfo;
	private TextView tv_IdCardStyle;

	String idCardNumber = null;
	private Dialog process;

	private Handler queryIdCardInfoHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case API.QUERY_IDCARD_INFO://该字段使用gpsso查询，暂时Service代码注销了
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(), "Query IdCard Information Failed!", 0).show();
				} else {
					tv_IdCardInfo.setText(msg.obj.toString());
				}
				break;
			case API.BAIDU_QUERY_IDCARD_INFO://该字段使用百度接口查询
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(), "Query IdCard Information Failed!", 0).show();
				} else {
					tv_IdCardInfo.setText(msg.obj.toString());
				}
				break;

			default:
				break;
			}
			process.dismiss();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.activity_idcard);

		initView();
	}

	private void initView() {
		et_IdCard = (EditText) findViewById(R.id.et_IdCard);
		ib_QueryIdCard = (ImageButton) findViewById(R.id.ib_QueryIdCard);
		btn_Return = (ImageButton) findViewById(R.id.btn_Return);
		tv_IdCardInfo = (TextView) findViewById(R.id.tv_IdCardInfo);
		tv_IdCardStyle = (TextView) findViewById(R.id.tv_IdCardStyle);

		ib_QueryIdCard.setOnClickListener(this);
		btn_Return.setOnClickListener(this);
		tv_IdCardInfo.setOnLongClickListener(this);
		tv_IdCardStyle.setOnLongClickListener(this);
		

		if(MyValuesInt.getIDCardStyle() == 0){
			tv_IdCardStyle.setText("基于百度API");
		} else if(MyValuesInt.getIDCardStyle() == 1){
			tv_IdCardStyle.setText("基于GPSSO");
		}
		tv_IdCardStyle.setTag(MyValuesInt.getIDCardStyle());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_QueryIdCard:
			idCardNumber = et_IdCard.getText().toString().trim();
			if (idCardNumber == null || idCardNumber.equals("")) {
				Toast.makeText(getApplicationContext(), "Please input IdCard number!", 0).show();
			} else if (idCardNumber.length() < 18) {
				Toast.makeText(getApplicationContext(), "Please enter 18 digit ID number!", 0).show();
			} else if (!(API.StringISNum(idCardNumber.substring(0, idCardNumber.length() - 2)))) {
				Toast.makeText(getApplicationContext(), "Can't contain letters before 17!", 0).show();
			} else {
				selectPhoneStyle(idCardNumber);
			}
			break;
			
		case R.id.btn_Return:
			finish();
			onDestroy();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View view) {
		switch (view.getId()) {
		case R.id.tv_IdCardInfo:
			ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			String idCardInfo = tv_IdCardInfo.getText().toString().trim();
			if (idCardInfo == null || idCardInfo.equals("")) {
				Toast.makeText(getApplicationContext(), "IdCard Information is Empty!", Toast.LENGTH_SHORT).show();
			} else {
				cbm.setText(idCardInfo);
				Toast.makeText(getApplicationContext(), "IdCard information has been copied!", 0).show();
			}
			break;
		case R.id.tv_IdCardStyle:
			if ((Integer) tv_IdCardStyle.getTag() == 0) {
				MyValuesInt.setIDCardStyle(1);
				tv_IdCardStyle.setText("基于GPSSO");
			} else if((Integer) tv_IdCardStyle.getTag() == 1) {
				MyValuesInt.setIDCardStyle(0);
				tv_IdCardStyle.setText("基于百度API");
			}
			tv_IdCardStyle.setTag(MyValuesInt.getIDCardStyle());
			break;

		default:
			break;
		}
		return true;
	}
	
	private void selectPhoneStyle(String idcard){
		if (MyValuesInt.getIDCardStyle() == 0) {
			BaiduIDCardService baiDuService = new BaiduIDCardService(queryIdCardInfoHandler, idcard);
			baiDuService.start();
			
		} else if (MyValuesInt.getIDCardStyle() == 1) {
			IdCardService gpssoService = new IdCardService(queryIdCardInfoHandler, idcard);
			gpssoService.start();
		}
		process = CircleProgress.createCircleProgressDialog(this, "Query");
		process.show();
	}
}

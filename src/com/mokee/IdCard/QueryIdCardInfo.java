package com.mokee.IdCard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.tools.R;

public class QueryIdCardInfo extends Activity implements OnClickListener {
	private static final String TAG = "QueryIdCardInfo";

	private EditText et_IdCard;
	private Button btn_QueryIdCardCancle;
	private ImageButton ib_QueryIdCard;
	private TextView tv_IdCardInfo;

	String idCardNumber = null;

	private Handler MyQueryIdCardInfo = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case API.QUERY_IDCARD_INFO:
				if (msg.obj == null) {
					Toast.makeText(getApplicationContext(),
							"Query IdCard Information Failed!",
							Toast.LENGTH_SHORT).show();
				} else {
					tv_IdCardInfo.setText(msg.obj.toString());
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

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);
		setContentView(R.layout.activity_idcard);

		initView();
	}

	private void initView() {
		et_IdCard = (EditText) findViewById(R.id.et_IdCard);
		btn_QueryIdCardCancle = (Button) findViewById(R.id.btn_QueryIdCardCancle);
		ib_QueryIdCard = (ImageButton) findViewById(R.id.ib_QueryIdCard);
		tv_IdCardInfo = (TextView) findViewById(R.id.tv_IdCardInfo);

		ib_QueryIdCard.setOnClickListener(this);
		btn_QueryIdCardCancle.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_QueryIdCard:
			idCardNumber = et_IdCard.getText().toString().trim();
			if (idCardNumber == null || idCardNumber.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Please input IdCard number!", Toast.LENGTH_SHORT)
						.show();

			} else if (idCardNumber.length() < 18) {
				Toast.makeText(getApplicationContext(),
						"Please enter 18 digit ID number!", Toast.LENGTH_SHORT)
						.show();
			} else if(!(API.StringISNum(idCardNumber.substring(0, idCardNumber.length()-2)))){
				Toast.makeText(getApplicationContext(),
						"Can't contain letters before 17!", Toast.LENGTH_SHORT)
						.show();
			}
			else{
				IdCardService service = new IdCardService(MyQueryIdCardInfo,
						idCardNumber);
				service.start();
			}
			break;
		case R.id.btn_QueryIdCardCancle:
			finish();
			onDestroy();
			break;

		default:
			break;
		}
	}
}

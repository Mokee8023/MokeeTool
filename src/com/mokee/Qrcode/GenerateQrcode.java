package com.mokee.Qrcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.mokee.tools.R;
import com.zxing.encoding.EncodingHandler;

public class GenerateQrcode extends Activity implements OnClickListener {
	private static final String TAG = "GenerateQrcode";

	private ImageButton ib_Return;
	private Button btn_GenerateQrcode;
	private EditText et_QrcodeText;
	private ImageView iv_Qrcode;
	private Spinner sp_QrcodeSize;
	private TextView activity_Text;

	private String generateText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.generate_qrcode);

		initView();
	}

	private void initView() {
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		btn_GenerateQrcode = (Button) findViewById(R.id.btn_GenerateQrcode);
		et_QrcodeText = (EditText) findViewById(R.id.et_QrcodeText);
		iv_Qrcode = (ImageView) findViewById(R.id.iv_Qrcode);
		sp_QrcodeSize = (Spinner) findViewById(R.id.sp_QrcodeSize);
		activity_Text = (TextView) findViewById(R.id.activity_Text);

		sp_QrcodeSize.setSelection(5);
		activity_Text.setText(R.string.generate_qrcode);

		ib_Return.setOnClickListener(this);
		btn_GenerateQrcode.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_Return:
			finish();
			// System.exit(0);
			break;
		case R.id.btn_GenerateQrcode:
			generateText = et_QrcodeText.getText().toString().trim();
			if (generateText == null || generateText.equals("")) {
				Toast.makeText(getApplicationContext(), "Please Input Text!",
						Toast.LENGTH_SHORT).show();
			} else {
				GenerateQrcode(generateText);
			}
			break;
		default:
			break;
		}
	}

	private void GenerateQrcode(String text) {
		try {
			Bitmap qrCodeBitmap = EncodingHandler.createQRCode(text,
					250 + sp_QrcodeSize.getSelectedItemPosition() * 50);
			iv_Qrcode.setImageBitmap(qrCodeBitmap);
		} catch (WriterException e) {
			Toast.makeText(getApplicationContext(), "Generate Qrcode Error!",
					Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Generate Qrcode Error:" + e.toString());
		}
	}
}

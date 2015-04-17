package com.mokee.Qrcode;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.tools.R;

public class Sweep extends Activity implements OnLongClickListener, OnClickListener {

	private String data;
	private EditText et_SweepResult;
	private ImageButton ib_Return;
	private TextView activity_Text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sweep_qrcode);

		Intent intent = getIntent();
		Bundle dataBundle = intent.getBundleExtra("result");
		data = dataBundle.getString("result");

		initView();
	}

	private void initView() {
		et_SweepResult = (EditText) findViewById(R.id.et_SweepResult);
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		activity_Text = (TextView) findViewById(R.id.activity_Text);
		
		activity_Text.setText(R.string.sweep);
		et_SweepResult.setText(data);
		et_SweepResult.setOnLongClickListener(this);
		ib_Return.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onLongClick(View view) {
		ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		String text = et_SweepResult.getText().toString().trim();
		if (text.equals("")) {
			Toast.makeText(getApplicationContext(), "Text Edit Box is Empty!",
					Toast.LENGTH_SHORT).show();
		} else {
			cbm.setText(text);
			Toast.makeText(getApplicationContext(), "Text is Copied!",
					Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_Return:
			finish();
//			System.exit(0);
			break;
		default:
			break;
		}
	}
}

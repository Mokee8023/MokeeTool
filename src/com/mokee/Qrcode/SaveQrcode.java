package com.mokee.Qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.mokee.tools.R;

public class SaveQrcode extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_save);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("");
	}

}

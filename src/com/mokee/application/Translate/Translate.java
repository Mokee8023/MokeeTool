package com.mokee.application.Translate;

import com.mokee.application.API.API;
import com.mokee.application.API.ApiLanguage;
import com.mokee.tools.R;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Translate extends Activity implements OnClickListener, OnLongClickListener {
	private static final String TAG = "Translate";

	private EditText et_TranslateText;
	private TextView tv_ResultText;
	private Spinner sp_SourceLang;
	private Spinner sp_TargetLang;
	private ImageButton ib_tranlate;
	private ImageButton ib_Return;
	private ImageButton ib_ClearChat;
	private TextView activity_Text;

	private String sourceLang = "auto";// 默认情况下是自动识别
	private String targetLang = "auto";

	private Handler MyTranslateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case API.GET_TRANSLATE_TEXT:
				if (msg.obj != null) {
					tv_ResultText.setText(msg.obj.toString());
				} else {
					Toast.makeText(getApplicationContext(),
							"Result text is NULL!", Toast.LENGTH_SHORT).show();
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
		setContentView(R.layout.activity_translation);

		initView();
	}

	private void initView() {
		et_TranslateText = (EditText) findViewById(R.id.et_TranslateText);
		tv_ResultText = (TextView) findViewById(R.id.tv_ResultText);
		sp_SourceLang = (Spinner) findViewById(R.id.sp_SourceLang);
		sp_TargetLang = (Spinner) findViewById(R.id.sp_TargetLang);
		ib_tranlate = (ImageButton) findViewById(R.id.ib_tranlate);
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		ib_ClearChat = (ImageButton) findViewById(R.id.ib_ClearChat);
		activity_Text = (TextView) findViewById(R.id.activity_Text);

		String[] items = getResources().getStringArray(R.array.Language);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(),
				R.layout.my_simple_spinner_item, items);

		sp_SourceLang.setAdapter(adapter);
		sp_TargetLang.setAdapter(adapter);

		sp_SourceLang.setSelection(1);
		sp_TargetLang.setSelection(2);
		activity_Text.setText(R.string.translation);
		
		sp_SourceLang.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				sourceLang = ApiLanguage.GetLanguageCode(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		sp_TargetLang.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				targetLang = ApiLanguage.GetLanguageCode(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		ib_tranlate.setOnClickListener(this);
		ib_Return.setOnClickListener(this);
		ib_ClearChat.setOnClickListener(this);
		tv_ResultText.setOnLongClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_tranlate:
			String text = et_TranslateText.getText().toString().trim();
			if (text.isEmpty() || text.equals("")) {
				Toast.makeText(getApplicationContext(), "Please enter the text!", Toast.LENGTH_SHORT).show();
			} else {
				TranslteThread translate = new TranslteThread(
						MyTranslateHandler, text, sourceLang, targetLang);
				translate.start();
			}
			break;
			
		case R.id.ib_Return:
			finish();
			onDestroy();
			break;
			
		case R.id.ib_ClearChat:
			et_TranslateText.setText("");
			tv_ResultText.setText("");
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_ResultText:
			ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			String text = tv_ResultText.getText().toString().trim();
			if(!text.equals("")){
				cbm.setText(text);
				Toast.makeText(this, "Translate result has been copied.", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		return true;
	}
}
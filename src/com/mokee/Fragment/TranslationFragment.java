package com.mokee.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.API.ApiLanguage;
import com.mokee.Translate.TranslteThread;
import com.mokee.tools.R;

public class TranslationFragment extends Fragment {

	private EditText et_TranslateText;
	private Spinner sp_SourceLang;
	private Spinner sp_TargetLang;
	private Button btn_tranlate;

	private String sourceLang = "auto";// 默认情况下是自动识别
	private String targetLang = "auto";
	
	private Handler MyTranslateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case API.GET_TRANSLATE_TEXT:
				if(msg.obj != null){
					et_TranslateText.setText(msg.obj.toString());
				}else{
					
				}
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.translation, container, false);

		initView(view);

		return view;
	}

	private void initView(View view) {
		et_TranslateText = (EditText) view.findViewById(R.id.et_TranslateText);
		sp_SourceLang = (Spinner) view.findViewById(R.id.sp_SourceLang);
		sp_TargetLang = (Spinner) view.findViewById(R.id.sp_TargetLang);
		btn_tranlate = (Button) view.findViewById(R.id.btn_tranlate);

		String[] items = getResources().getStringArray(R.array.Language);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, items);

		sp_SourceLang.setAdapter(adapter);
		sp_TargetLang.setAdapter(adapter);
		
		sp_SourceLang.setSelection(1);
		sp_TargetLang.setSelection(2);

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
		
		btn_tranlate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				String text = et_TranslateText.getText().toString().trim();
				if(text.isEmpty()||text.equals("")){
					Toast.makeText(getActivity(), "Please enter the text to be translated!", Toast.LENGTH_SHORT).show();
				}else{
					TranslteThread translate = new TranslteThread(MyTranslateHandler, text, sourceLang, targetLang);
					translate.start();
				}
			}
		});
	}
}

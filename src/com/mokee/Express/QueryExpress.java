package com.mokee.Express;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.API.APIExpressName;
import com.mokee.tools.R;
import com.zxing.activity.CaptureActivity;

public class QueryExpress extends Activity implements OnClickListener,
		OnLongClickListener {
	private static final String TAG = "QueryExpress";

	private ImageButton ib_Return;
	private ImageButton ib_QueryExpress;
	private ImageButton ib_QueryExpress_GetNum;
	private TextView activity_Text;
	private TextView tv_ExpressQueryResult;
	private EditText et_QueryExpressNumber;
	private Spinner sp_ExpressName;

	private String expressName = "shunfeng";
	
	private Handler MyQueryExpressHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case API.QUERY_EXPRESS_INFO:
				Log.i(TAG, "返回的 Query Result：" + msg.obj);
				String result = (String) msg.obj;
				if (result == null || result.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Query Result Information is Empty!",
							Toast.LENGTH_SHORT).show();
				} else {
					tv_ExpressQueryResult.setText(result);
				}
				
//			case API.QUERY_EXPRESS_LIST_INFO:
//				Log.i(TAG, "返回的 Query Result：" + msg.obj);
//				JSONObject returnData = (JSONObject) msg.obj;
//				try {
//					if(returnData.getInt("showapi_res_code") == 0){
//						JSONObject body = returnData.getJSONObject("showapi_res_body");
//						if(body.getString("flag").equals("true")){
//							JSONArray list = body.getJSONArray("expressList");
//							items = new String[list.length()];
//							for (int i = 0; i < list.length(); i++) {
//								items[i] = list.getJSONObject(i).getString("expName");
//							}
//							expressName = items[0];
//							ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_simple_spinner_item, items);
//							sp_ExpressName.setAdapter(adapter);
//							sp_ExpressName.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//								@Override
//								public void onItemSelected(AdapterView<?> parent, View view,
//										int position, long id) {
////									expressName = APIExpressName.GetExpressCode(position);
//									expressName = items[position];
//									Log.i(TAG, "expressName:" + expressName);
//								}
//
//								@Override
//								public void onNothingSelected(AdapterView<?> arg0) {
//								}
//							});
//						} else {
//							Toast.makeText(getApplicationContext(), "Data is Null.", Toast.LENGTH_SHORT).show();
//						}
//					} else {
//						Toast.makeText(getApplicationContext(), "Query company list failed.", Toast.LENGTH_SHORT).show();
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				
//				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_express);

		initView();
		
		QueryExpressCompList compList = new QueryExpressCompList(MyQueryExpressHandler);
		compList.start();
	}

	private void initView() {
		activity_Text = (TextView) findViewById(R.id.activity_Text);
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		ib_QueryExpress = (ImageButton) findViewById(R.id.ib_QueryExpress);
		ib_QueryExpress_GetNum = (ImageButton) findViewById(R.id.ib_QueryExpress_GetNum);
		tv_ExpressQueryResult = (TextView) findViewById(R.id.tv_ExpressQueryResult);
		et_QueryExpressNumber = (EditText) findViewById(R.id.et_QueryExpressNumber);
		sp_ExpressName = (Spinner) findViewById(R.id.sp_ExpressName);

		ib_Return.setOnClickListener(this);
		ib_QueryExpress_GetNum.setOnClickListener(this);
		ib_QueryExpress.setOnClickListener(this);

		tv_ExpressQueryResult.setOnLongClickListener(this);

		activity_Text.setText(R.string.express_query);

		String[]items = getResources().getStringArray(R.array.Express_Name);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_simple_spinner_item, items);
		sp_ExpressName.setAdapter(adapter);
		sp_ExpressName.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				expressName = APIExpressName.GetExpressCode(position);
				Log.i(TAG, "expressName:" + expressName);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_Return:
			finish();
			onDestroy();
			break;

		case R.id.ib_QueryExpress_GetNum:
			Intent sweepIntent = new Intent(getApplicationContext(),
					CaptureActivity.class);
			startActivityForResult(sweepIntent, API.SWEEP);
			break;

		case R.id.ib_QueryExpress:
			String queryNumber = et_QueryExpressNumber.getText().toString()
					.trim();
			if (queryNumber == null || queryNumber.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Please input express number", Toast.LENGTH_SHORT)
						.show();
			} else if (expressName == null || expressName.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Please choice express!", Toast.LENGTH_SHORT).show();
			} else {
				QueryShowAPIExpressInfo query = new QueryShowAPIExpressInfo(MyQueryExpressHandler, queryNumber, expressName);
				query.start();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onLongClick(View view) {
		switch (view.getId()) {
		case R.id.tv_ExpressQueryResult:
			ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			String expressInfo = tv_ExpressQueryResult.getText().toString()
					.trim();

			if (expressInfo == null || expressInfo.equals("")) {
				Toast.makeText(getApplicationContext(),
						"Express Information is Empty!", Toast.LENGTH_SHORT)
						.show();
			} else {
				cbm.setText(expressInfo);
				Toast.makeText(getApplicationContext(),
						"Express information has been copied!",
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case API.SWEEP:
			if (resultCode == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				String result = bundle.getString("result");

				if (result == null || result.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Scan results is empty!", Toast.LENGTH_SHORT)
							.show();
				} else {
					et_QueryExpressNumber.setText(result);
				}
			}
			break;

		default:
			break;
		}
	}
}

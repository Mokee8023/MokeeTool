package com.mokee.application.Socket;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.database.SPSetting.MyValuesInt;
import com.mokee.database.SPSetting.MyValuesString;
import com.mokee.network.NetConnect.ConstantUtil;
import com.mokee.network.NetConnect.SocketClientThread;
import com.mokee.network.NetConnect.SocketServerThread;
import com.mokee.tools.R;

public class SocketDebugActivity extends Activity implements OnClickListener, OnLongClickListener {
	private static final String tag = "SocketDebugActivity";

	private ImageButton ib_Return;
	private Button btn_SendSocket, btn_SetSocketIp, btn_SetSocketPort;
	private TextView activity_Text,tv_SocketResult;
	private EditText et_SocketIp, et_SocketPort, et_SocketDataText;
	private Spinner sp_SocketDataType, sp_SocketDataImageFrom;
	private ImageView iv_SocketImage;
	
	private byte[] imageByte = null;
	
	private static final int SOCKET_CAMERA_IMAGE = 1;
	private static final int SOCKET_PHONE_IMAGE = 2;
	private int mPosition;
	private StringBuilder tv_sb = new StringBuilder();
	
	private Handler socketHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case ConstantUtil.SOCKET_CLIENT_THREAD:
				tv_SocketResult.setText(msg.obj.toString());
				break;
				
			case ConstantUtil.SOCKET_SERVER_THREAD:
				tv_sb.append(msg.obj).append("\n");
				tv_SocketResult.setText(tv_sb.toString());
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
		setContentView(R.layout.activity_socket);
		
		initView();
		initEvent();
		
		//开启Socket接受线程
		SocketServerThread socketServer = new SocketServerThread(socketHandler, MyValuesInt.getSocketPort());
		socketServer.start();
	}

	private void initView() {
		btn_SendSocket = (Button) findViewById(R.id.btn_SendSocket);
		btn_SetSocketIp = (Button) findViewById(R.id.btn_SetSocketIp);
		btn_SetSocketPort = (Button) findViewById(R.id.btn_SetSocketPort);
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		activity_Text = (TextView) findViewById(R.id.activity_Text);
		tv_SocketResult = (TextView) findViewById(R.id.tv_SocketResult);
		
		et_SocketIp = (EditText) findViewById(R.id.et_SocketIp);
		et_SocketPort = (EditText) findViewById(R.id.et_SocketPort);
		et_SocketDataText = (EditText) findViewById(R.id.et_SocketDataText);
		
		sp_SocketDataType = (Spinner) findViewById(R.id.sp_SocketDataType);
		sp_SocketDataImageFrom = (Spinner) findViewById(R.id.sp_SocketDataImageFrom);
		
		iv_SocketImage = (ImageView) findViewById(R.id.iv_SocketImage);
	}

	private void initEvent() {
		activity_Text.setText("Socket Debug Tool");
		et_SocketIp.setText(MyValuesString.getSocketIP());
		et_SocketPort.setText(MyValuesInt.getSocketPort() + "");
		
		ib_Return.setOnClickListener(this);
		btn_SendSocket.setOnClickListener(this);
		btn_SetSocketIp.setOnClickListener(this);
		btn_SetSocketPort.setOnClickListener(this);
		iv_SocketImage.setOnClickListener(this);
		tv_SocketResult.setOnLongClickListener(this);
		
		String[] dataType = getResources().getStringArray(R.array.SocketDataType);
		ArrayAdapter<String> dataTypeAdapter = new ArrayAdapter<String>(this, R.layout.my_simple_spinner_item, dataType);
		sp_SocketDataType.setAdapter(dataTypeAdapter);
		
		String[] iamgeFrom = getResources().getStringArray(R.array.SocketImageFrom);
		ArrayAdapter<String> iamgeFromAdapter = new ArrayAdapter<String>(this, R.layout.my_simple_spinner_item, iamgeFrom);
		sp_SocketDataImageFrom.setAdapter(iamgeFromAdapter);
		
		sp_SocketDataType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						if (position == 0) {
							imageByte = null;
							et_SocketDataText.setVisibility(View.VISIBLE);
							sp_SocketDataImageFrom.setVisibility(View.INVISIBLE);
							iv_SocketImage.setVisibility(View.GONE);
						} else {
							sp_SocketDataImageFrom.setSelection(0);
							et_SocketDataText.setVisibility(View.INVISIBLE);
							sp_SocketDataImageFrom.setVisibility(View.VISIBLE);
						}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		
		sp_SocketDataType.setSelection(0);
		
		sp_SocketDataImageFrom.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						mPosition = position;
						imageByte = null;
						iv_SocketImage.setVisibility(View.GONE);
						Log.i(tag, "sp_SocketDataImageFrom.position:" + position);
						if (position == 1 && sp_SocketDataType.getSelectedItemPosition() == 1) {
							// 调用android自带的照相机
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent, SOCKET_CAMERA_IMAGE);
						} else if(position == 2 && sp_SocketDataType.getSelectedItemPosition() == 1){
							// 调用android的图库
							Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(intent, SOCKET_PHONE_IMAGE);
						}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case SOCKET_CAMERA_IMAGE:
			if(resultCode == Activity.RESULT_OK){
				iv_SocketImage.setVisibility(View.VISIBLE);
				Bitmap imageCamera = (Bitmap) data.getExtras().get("data");
				imageByte = Bitmap2Bytes(imageCamera);
				iv_SocketImage.setImageBitmap(imageCamera);
			} else {
				sp_SocketDataImageFrom.setSelection(0);
				Toast.makeText(this, "Not select image", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case SOCKET_PHONE_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				iv_SocketImage.setVisibility(View.VISIBLE);
				
				Cursor cursor = this.getContentResolver().query(data.getData(), null, null, null, null);
				cursor.moveToFirst();
				String imgPath = cursor.getString(1); // 图片文件路径
				cursor.close();
				
				Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;
				options.inSampleSize = 5;
				Bitmap imagePhone = BitmapFactory.decodeFile(imgPath, options);
				
				imageByte = Bitmap2Bytes(BitmapFactory.decodeFile(imgPath, options));
				iv_SocketImage.setImageBitmap(imagePhone);
			} else {
				sp_SocketDataImageFrom.setSelection(0);
				Toast.makeText(this, "Not select image", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_Return:
			finish();
			onDestroy();
			break;
			
		case R.id.btn_SendSocket:
			tv_SocketResult.setText("");
			tv_sb.delete(0, tv_sb.length());
			
			if(sp_SocketDataType.getSelectedItemPosition() == 0) {
				String socketText = et_SocketDataText.getText().toString().trim();
				if(socketText != null && !socketText.equals("")){
					SocketClientThread sendTextSocket = new SocketClientThread(
							socketHandler, socketText, MyValuesString.getSocketIP(), MyValuesInt.getSocketPort());
					sendTextSocket.start();
					Toast.makeText(this, "Text Socket Sent", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Please input Text", Toast.LENGTH_SHORT).show();
				}
			} else if(imageByte != null){
				SocketClientThread sendImageSocket = new SocketClientThread(
						socketHandler, imageByte, MyValuesString.getSocketIP(), MyValuesInt.getSocketPort());
				sendImageSocket.start();
			} else {
				Toast.makeText(this, "Please select image Or take pictures", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.btn_SetSocketIp:
			String ip = et_SocketIp.getText().toString().trim();
			if(ip != null && !ip.equals("")){
				MyValuesString.setSocketIP(ip);
			} else {
				Toast.makeText(this, "Please input Correct IP", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.btn_SetSocketPort:
			String port = et_SocketPort.getText().toString().trim();
			if(port != null && !port.equals("")){
				MyValuesInt.setSocketPort(Integer.valueOf(port));
			} else {
				Toast.makeText(this, "Please input Correct Port", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.iv_SocketImage:
			if (mPosition == 1) {
				// 调用android自带的照相机
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, SOCKET_CAMERA_IMAGE);
			} else if(mPosition == 2){
				// 调用android的图库
				Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, SOCKET_PHONE_IMAGE);
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onLongClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_SocketResult:
			String text = tv_SocketResult.getText().toString().trim();
			if(text != null && !text.equals("")){
				ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cbm.setText(text);
				Toast.makeText(this, "Text has been copied", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
		return true;
	}
	
	public byte[] Bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}

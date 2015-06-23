package com.mokee.application.Socket;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.database.SPSetting.MyValuesInt;
import com.mokee.database.SPSetting.MyValuesString;
import com.mokee.network.NetConnect.ConstantUtil;
import com.mokee.tools.R;

public class SocketSingleImageActivity extends Activity implements OnClickListener {
	
	private Button btn_SelectPic, btn_SendPic, btn_SetSocketIp, btn_SetSocketPort;
	private ImageButton ib_Return;
	private TextView tv_SocketResult, activity_Text;
	private ImageView iv_SocketImage;
	private EditText et_SocketIp, et_SocketPort;
	
	private static final int Test_SOCKET_PHONE_IMAGE = 1;
	private byte[] imageByte = null;
	private StringBuilder resultSB = new StringBuilder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_socket_single_image);
		
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		
		btn_SelectPic = (Button) findViewById(R.id.btn_SelectTestPic);
		btn_SendPic = (Button) findViewById(R.id.btn_SendTestPic);
		btn_SetSocketIp = (Button) findViewById(R.id.btn_SetSocketSingleIp);
		btn_SetSocketPort = (Button) findViewById(R.id.btn_SetSocketSinglePort);
		
		et_SocketIp = (EditText) findViewById(R.id.et_SocketSingleIp);
		et_SocketPort = (EditText) findViewById(R.id.et_SocketSinglePort);
		tv_SocketResult = (TextView) findViewById(R.id.tv_SocketTestResult);
		activity_Text = (TextView) findViewById(R.id.activity_Text);
		iv_SocketImage = (ImageView) findViewById(R.id.iv_SocketTestImage);
		
		et_SocketIp.setText(MyValuesString.getSocketIP());
		et_SocketPort.setText(MyValuesInt.getSocketPort() + "");
		
		btn_SetSocketIp.setOnClickListener(this);
		btn_SetSocketPort.setOnClickListener(this);
		btn_SelectPic.setOnClickListener(this);
		btn_SendPic.setOnClickListener(this);
		ib_Return.setOnClickListener(this);
		
		activity_Text.setText("Socket Debug Single Version");
	}
	
	private Handler socketHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case ConstantUtil.SOCKET_CLIENT_THREAD_OPEN:
				resultSB.append(msg.obj).append("\n");
				if(((String)msg.obj).equals("Socket connect success")){
					Toast.makeText(SocketSingleImageActivity.this, "Socket connect success", Toast.LENGTH_SHORT).show();
				} else {					
					Toast.makeText(SocketSingleImageActivity.this, "Socket connect fail", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case ConstantUtil.SOCKET_CLIENT_THREAD_CLOSE:
				resultSB.append(msg.obj).append("\n");
				Toast.makeText(SocketSingleImageActivity.this, "Socket disconnect success", Toast.LENGTH_SHORT).show();
				break;
				
			case ConstantUtil.SOCKET_CLIENT_THREAD_SEND:
				resultSB.append(msg.obj).append("\n");
				break;
				
			case ConstantUtil.SOCKET_CLIENT_THREAD_ACCEPT:
				resultSB.append("Accept data:").append(msg.obj).append("\n");
				break;
				
			case ConstantUtil.SOCKET_CLIENT_THREAD_INIT:
				resultSB.append(msg.obj).append("\n");
				break;
				
			case ConstantUtil.SOCKET_CLIENT_THREAD_ACCEPT_DATA:
				resultSB.append("Result data:").append(msg.obj).append("\n");
				break;

			default:
				break;
			}
			tv_SocketResult.setText(resultSB.toString());
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK){
			iv_SocketImage.setVisibility(View.VISIBLE);
			
			Cursor cursor = this.getContentResolver().query(data.getData(), null, null, null, null);
			cursor.moveToFirst();
			String imgPath = cursor.getString(1);
			cursor.close();
			
			Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = 5;
			Bitmap imagePhone = BitmapFactory.decodeFile(imgPath, options);
			
			imageByte = Bitmap2Bytes(BitmapFactory.decodeFile(imgPath, options));
			iv_SocketImage.setImageBitmap(imagePhone);
		} else {
			Toast.makeText(this, "Not select image", Toast.LENGTH_SHORT).show();
		}
	}
	
	public byte[] Bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_SelectTestPic:
			resultSB.delete(0, resultSB.length());
			iv_SocketImage.setVisibility(View.GONE);
			Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, Test_SOCKET_PHONE_IMAGE);		
			break;
		case R.id.btn_SendTestPic:
			if(imageByte != null){
				SocketSingleImageThread socketThread = new SocketSingleImageThread(socketHandler, imageByte);
				socketThread.start();
				resultSB.append("Image Socket Sent...").append("\n");
			} else {
				Toast.makeText(SocketSingleImageActivity.this, "Please select image.", Toast.LENGTH_SHORT).show();
			}
			
			tv_SocketResult.setText(resultSB.toString());
			break;
		case R.id.btn_SetSocketSingleIp:
			String ip = et_SocketIp.getText().toString().trim();
			if(ip != null && !ip.equals("")){
				MyValuesString.setSocketIP(ip);
			} else {
				Toast.makeText(this, "Please input Correct IP", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_SetSocketSinglePort:
			String port = et_SocketPort.getText().toString().trim();
			if(port != null && !port.equals("")){
				MyValuesInt.setSocketPort(Integer.valueOf(port));
			} else {
				Toast.makeText(this, "Please input Correct Port", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.ib_Return:
			finish();
			onDestroy();
			break;

		default:
			break;
		}
	}
}

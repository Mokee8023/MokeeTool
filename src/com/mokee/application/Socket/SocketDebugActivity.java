package com.mokee.application.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

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
import com.mokee.network.NetConnect.SocketClientThread_Return;
import com.mokee.network.NetConnect.SocketServerThread;
import com.mokee.tools.R;

public class SocketDebugActivity extends Activity implements OnClickListener, OnLongClickListener {
	private static final String tag = "SocketDebugActivity";

	private ImageButton ib_Return, ib_Network;
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
	
	private Socket socket = null; 
	SocketServerThread socketServer = null;
	
	private Handler socketHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case ConstantUtil.SOCKET_CLIENT_THREAD_OPEN:
				tv_sb.append(msg.obj).append("\n");
				if(((String)msg.obj).equals("Socket connect success")){
					ib_Network.setImageResource(R.drawable.network_connect);
					Toast.makeText(SocketDebugActivity.this, "Socket connect success", Toast.LENGTH_SHORT).show();
				} else {					
					Toast.makeText(SocketDebugActivity.this, "Socket connect fail", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case ConstantUtil.SOCKET_CLIENT_THREAD_CLOSE:
				tv_sb.append(msg.obj).append("\n");
				if(((String)msg.obj).equals("Socket disconnect success")){
					ib_Network.setImageResource(R.drawable.network_disconnect);					
					Toast.makeText(SocketDebugActivity.this, "Socket disconnect success", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SocketDebugActivity.this, "Socket disconnect fail", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case ConstantUtil.SOCKET_CLIENT_THREAD_SEND:
				tv_sb.append(msg.obj).append("\n");
				break;
				
			case ConstantUtil.SOCKET_CLIENT_THREAD_ACCEPT:
				tv_sb.append("Accept data:").append(msg.obj).append("\n");
				break;
				
			case ConstantUtil.SOCKET_CLIENT_THREAD_INIT:
				tv_sb.append(msg.obj).append("\n");
				break;
				
			case ConstantUtil.SOCKET_SERVER_THREAD:
				tv_sb.append("Accept data:").append(msg.obj).append("\n");
				break;

			default:
				break;
			}
			tv_SocketResult.setText(tv_sb.toString());
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_socket);
		
		initView();
		initEvent();	
		
		//监听端口
		socketServer = new SocketServerThread(socketHandler, MyValuesInt.getSocketPort());
		socketServer.start();
	}

	private void initView() {
		btn_SendSocket = (Button) findViewById(R.id.btn_SendSocket);
		btn_SetSocketIp = (Button) findViewById(R.id.btn_SetSocketIp);
		btn_SetSocketPort = (Button) findViewById(R.id.btn_SetSocketPort);
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		ib_Network = (ImageButton) findViewById(R.id.ib_Save);
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
		ib_Network.setOnClickListener(this);
		
		activity_Text.setOnLongClickListener(this);
		
		ib_Network.setVisibility(View.VISIBLE);
		ib_Network.setImageResource(R.drawable.network_disconnect);
		ib_Network.setTag("1");
		
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
			if(socket == null){
				Toast.makeText(this, "Please connect socket.", Toast.LENGTH_SHORT).show();
			} else {
				if(sp_SocketDataType.getSelectedItemPosition() == 0) {
					String socketText = et_SocketDataText.getText().toString().trim();
					if(socketText != null && !socketText.equals("")){
						SocketClientThread_Return socketThread = new SocketClientThread_Return(socketHandler, socketText, writer, reader);
						socketThread.start();
						Toast.makeText(this, "Text Socket Sent", Toast.LENGTH_SHORT).show();
						
						tv_sb.append("Send text:").append(socketText).append("\n");
					} else {
						Toast.makeText(this, "Please input Text", Toast.LENGTH_SHORT).show();
					}
				} else if(imageByte != null){
					SocketClientThread_Return socketThread = new SocketClientThread_Return(socketHandler, imageByte, dos, reader);
					socketThread.start();
					Toast.makeText(this, "Image Socket Sent", Toast.LENGTH_SHORT).show();
					
					tv_sb.append("Send image.").append("\n");
				} else {
					Toast.makeText(this, "Please select image Or take pictures", Toast.LENGTH_SHORT).show();
				}
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
			
		case R.id.ib_Save:
			if(socket == null){
				new Thread(new Runnable() {
					@Override
					public void run() {
						openSocket(MyValuesString.getSocketIP(), MyValuesInt.getSocketPort());
					}
				}).start();
				tv_SocketResult.setText("");
				tv_sb.delete(0, tv_sb.length());
			} else {
				closeSocket();
				tv_SocketResult.setText("");
				tv_sb.delete(0, tv_sb.length());
			}			
			break;

		default:
			break;
		}
	}
	
	/**
	 * 连接Socket
	 */
	private void openSocket(String ip, int port){
		Message message = new Message();
		message.what = ConstantUtil.SOCKET_CLIENT_THREAD_OPEN;
		try {
			socket = new Socket(ip, port);
			socket.setKeepAlive(true);
			message.obj = "Socket connect success";
			socketHandler.sendMessage(message);
			Log.i(tag, "SocketDebugActivity.openSocket success");
			
			initStreamClass();
		} catch (UnknownHostException e) {
			Log.e(tag, "SocketDebugActivity.openSocket-->UnknownHostException:" + e.toString());
			message.obj = "SocketDebugActivity.openSocket-->UnknownHostException:" + e.toString();
			socketHandler.sendMessage(message);
		} catch (IOException e) {
			Log.e(tag, "SocketDebugActivity.openSocket-->IOException:" + e.toString());
			message.obj = "SocketDebugActivity.openSocket-->IOException:" + e.toString();
			socketHandler.sendMessage(message);
		}
	}
	
	/**
	 * 关闭Socket
	 */
	private void closeSocket(){
		Message message = new Message();
		message.what = ConstantUtil.SOCKET_CLIENT_THREAD_CLOSE;				
		if(socket != null){
			try {
				socket.close();
				socket = null;
				message.obj = "Socket disconnect success";
				
				closeStreamClass();
			} catch (IOException e) {
				Log.e(tag, "SocketDebugActivity.closeSocket-->UnknownHostException:" + e.toString());
				message.obj = "SocketDebugActivity.closeSocket-->UnknownHostException:" + e.toString();				
			} finally {
				socketHandler.sendMessage(message);
			}
		}
	}
	
	private BufferedWriter writer = null;
	private BufferedReader reader = null;
	private DataOutputStream dos = null;
	
	/**
	 * 初始化BufferedWriter、DataOutputStream、BufferedReader
	 */
	private void initStreamClass(){
		Message initMessage = new Message();
		try {
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			dos = new DataOutputStream(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			
			Log.i(tag, "initStreamClass is success");
		} catch (IOException e) {
			Log.e(tag, "SocketDebugActivity-->IOException:" + e.toString());
			initMessage.what = ConstantUtil.SOCKET_CLIENT_THREAD_INIT;
			initMessage.obj = ConstantUtil.NETWORK_STATUS_ERROR_IOException + ":" + e.toString();
			socketHandler.sendMessage(initMessage);
		}
	}
	/**
	 * 关闭BufferedWriter、DataOutputStream、BufferedReader
	 */
	private void closeStreamClass(){
		try {
			if(writer != null){
				writer.close();
			}
			if(writer != null){
				dos.close();
			}
			if(writer != null){
				reader.close();
			}
			writer = null;
			dos = null;
			reader = null;
		} catch (IOException e) {
			e.printStackTrace();
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
		case R.id.activity_Text:
			Intent singleSocket = new Intent(this, SocketSingleImageActivity.class);
			startActivity(singleSocket);
			break;

		default:
			break;
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(tag, "onDestroy has been called");
		
		closeSocket();
		closeStreamClass();
	}
	
	public byte[] Bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
}

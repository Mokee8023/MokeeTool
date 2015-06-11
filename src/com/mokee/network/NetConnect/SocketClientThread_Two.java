package com.mokee.network.NetConnect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketClientThread_Two extends Thread {
	private static final String TAG = "SocketClientThread_Two";
	private Handler handler;
	private String ip;
	private int port;
	public boolean isRunning = true;
	private BufferedWriter writer;
	private BufferedReader reader;
	private DataOutputStream dos;
	
	private byte[] imageMsg = null;
	private String textMsg = null;
	
	private Socket socket; 
	private boolean init_Flag = false;
	private boolean sendData_Flag = true;//是否有新的素材要发送
	
	/** int type 当type时,表示发送文字,为2时表示发送byte[]数据 */
	private int type;
	
	/**
	 * Socket客户端，向指定ip和port的服务端，文字信息
	 * 
	 * @param handler
	 *            用于传递信息的Handler
	 * @param msg
	 *            需要发送的信息
	 * @param ip
	 *            发送信息的ip地址
	 * @param port
	 *            对应Ip的端口
	 */
	public SocketClientThread_Two(Handler handler, String ip, int port) {
		this.handler = handler;
		this.ip = ip;
		this.port = port;
		this.type = 1;
	}
	
	/**
	 * 初始化用于Socket通信的元素：socket、reader、writer、dos
	 */
	private void init(){
		init_Flag = true;// 已经初始化一次，就无需再初始化

		Message message = new Message();
		message.what = ConstantUtil.SOCKET_CLIENT_THREAD_INIT;
		try {
			socket = new Socket(this.ip, this.port);
			socket.setKeepAlive(true);

			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			dos = new DataOutputStream(socket.getOutputStream());

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			message.obj = "true";
		} catch (UnknownHostException e) {
			Log.e(TAG, "SocketClientThread_Two.init-->UnknownHostException:" + e.toString());
			message.obj = "SocketClientThread_Two.init-->UnknownHostException:" + e.toString();
			handler.sendMessage(message);
		} catch (IOException e) {
			Log.e(TAG, "SocketClientThread_Two.init-->IOException:" + e.toString());
			message.obj = "SocketClientThread_Two.init-->IOException:" + e.toString();
			handler.sendMessage(message);
		}
	}
	
	@Override
	public void run() {
		super.run();
		
		// 发送文字
		sendSocketData();
		
		Message message = new Message();
		message.what = ConstantUtil.SOCKET_CLIENT_THREAD_ACCEPT;
		
		while(isRunning){
			
			if(!init_Flag && !(imageMsg == null || textMsg == null)){
				init();// 初始化相关量
			}
			
			if(sendData_Flag){
				sendSocketData();
				sendData_Flag = false;
			}
			String in;
			StringBuilder sb = new StringBuilder();
			String result = null;
			try {
				while((in = reader.readLine()) != null){
					sb.append(in);
					sb.append("\n");
				}
				result = sb.toString();
			} catch (IOException e) {
				Log.e(TAG, "SocketClientThread_Two.run-->IOException:" + e.toString());
				result = ConstantUtil.NETWORK_STATUS_ERROR_IOException + ":" + e.toString();
			} finally {
				message.obj = result;
				handler.sendMessage(message);
			}
		}
		
		stopSocket();
	}
	
	/**
	 * 设置文字信息
	 * @param msg
	 */
	public void setStringSources(String msg){
		this.textMsg = msg;
		sendData_Flag = true;
	}
	
	/**
	 * 设置图像信息
	 * @param msg
	 */
	public void setImageSources(byte[] msg){
		this.imageMsg = msg;
		sendData_Flag = true;
	}
	
	/**
	 * 发送文字信息、图像信息
	 */
	private void sendSocketData() {
		Message message = new Message();
		message.what = ConstantUtil.SOCKET_CLIENT_THREAD_SEND;
		
		try {
			if(type == 1){
				writer.write(textMsg);
				writer.flush();
			} else {
				dos.write(imageMsg);
				dos.flush();
			}
			
			message.obj = "Send success";
		} catch (IOException e) {
			message.obj = "Send failed:" + e.toString();
			Log.e(TAG, "SocketClientThread_Two.sendSocketData-->IOException:" + e.toString());
		} finally {
			handler.sendMessage(message);
		}
	}
	
	/**
	 * 关闭Socket并清理BufferedWriter、BufferedReader、DataOutputStream
	 */
	private void stopSocket(){
		try {
			writer.close();
			dos.close();
			reader.close();
			socket.close();
		} catch (IOException e) {
			Log.e(TAG, "SocketClientThread_Two.stopSocket-->IOException:" + e.toString());
		}
	}
}

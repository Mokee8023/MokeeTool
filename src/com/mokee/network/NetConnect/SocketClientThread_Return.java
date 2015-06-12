package com.mokee.network.NetConnect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.CharBuffer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketClientThread_Return extends Thread {
	private static final String TAG = "SocketClientThread_Return";
	private Handler handler;
	private String msg;
	private byte[] mData;
	
	private BufferedWriter mWriter = null;
	private BufferedReader mReader = null;
	private DataOutputStream mDos = null;
	
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
	public SocketClientThread_Return(Handler handler, String msg, BufferedWriter writer, BufferedReader reader) {
		this.handler = handler;
		this.msg = msg;
		this.mWriter = writer;
		this.mReader = reader;
		this.type = 1;
	}
	
	/**
	 * Socket客户端，向指定ip和port的服务端，(文字信息 或 者图片信息...)
	 * 
	 * @param handler
	 *            用于传递信息的Handler
	 * @param data
	 *            需要发送的byte[]型信息(可以为图片的byte[])
	 * @param ip
	 *            发送信息的ip地址
	 * @param port
	 *            对应Ip的端口
	 */
	public SocketClientThread_Return(Handler handler, byte[] data, DataOutputStream dos, BufferedReader reader) {
		this.handler = handler;
		this.mData = data;
		this.mDos = dos;
		this.mReader = reader;
		this.type = 2;
	}	
	
	@Override
	public void run() {
		super.run();
		sendSocketData();// 发送信息
		
		// 不接受信息
//		Message dataMessage = new Message();
//		dataMessage.what = ConstantUtil.SOCKET_CLIENT_THREAD_ACCEPT;
//		String result = null;
//		try {
//			char[] buffer = new char[1024 * 10];
//			int temp = mReader.read(buffer);
//			result = String.valueOf(buffer).substring(0, temp);
//			
//			Log.i(TAG, "Accepted data:" + result);
//		} catch (IOException e) {
//			Log.e(TAG, "SocketClientThread_Return.run-->IOException:" + e.toString());
//			result = ConstantUtil.NETWORK_STATUS_ERROR_IOException + ":" + e.toString();
//		} finally {
//			dataMessage.obj = result;
//			handler.sendMessage(dataMessage);
//		}
	}
	
	/**
	 * 发送文字信息、图像信息
	 */
	private void sendSocketData() {
		Message sendMessage = new Message();
		sendMessage.what = ConstantUtil.SOCKET_CLIENT_THREAD_SEND;
		
		if(mDos != null || mWriter != null){
			try {
				if(type == 1){
					mWriter.write(msg);
					mWriter.flush();
					Log.i(TAG, "Send textMsg data :" + msg);
				} else if(type == 2){
					mDos.write(mData);
					mDos.flush();
					Log.i(TAG, "Send imageMsg data :" + mData.toString());
				}
				sendMessage.obj = "Send data success";
			} catch (IOException e) {
				sendMessage.obj = "Send failed:" + e.toString();
				Log.e(TAG, "SocketClientThread_Return.sendSocketData-->IOException:" + e.toString());
			} finally {
				handler.sendMessage(sendMessage);
			}
		} else {
			sendMessage.obj = "Send failed, dos and writer is null";
			Log.e(TAG, "Send failed, dos and writer is null");
			handler.sendMessage(sendMessage);
		}
	}
}

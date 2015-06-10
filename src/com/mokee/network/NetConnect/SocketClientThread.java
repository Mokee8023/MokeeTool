package com.mokee.network.NetConnect;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketClientThread extends Thread {
	private final String TAG = getClass().getName();
	private Handler handler;
	private String msg;
	private String ip;
	private int port;
	private byte[] mData;
	
	/** int type 当type时,表示发送文字,为2时表示发送byte[]数据 */
	private int type;
	
	private Socket socket; 
	
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
	public SocketClientThread(Handler handler, String msg, String ip, int port) {
		this.handler = handler;
		this.msg = msg;
		this.ip = ip;
		this.port = port;
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
	public SocketClientThread(Handler handler, byte[] data, String ip, int port) {
		this.handler = handler;
		this.mData = data;
		this.ip = ip;
		this.port = port;
		this.type = 2;
	}
	
	@Override
	public void run() {
		super.run();
		
		Message message = new Message();
		message.what = ConstantUtil.SOCKET_CLIENT_THREAD;
		
		String result = null;
		try {
			socket = new Socket(ip, port);
			
			if(type == 1){
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				writer.write(msg);
				writer.flush();
			} else {
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.write(mData);
				dos.close(); 
			}
			
			result = ConstantUtil.NETWORK_STATUS_SUCCESS;
			
			Log.i(TAG, "SocketClientThread-->Send Socket Success:" + result);
		} catch (UnknownHostException e) {
			Log.e(TAG, "SocketClientThread-->UnknownHostException:" + e.toString());
			result = ConstantUtil.NETWORK_STATUS_ERROR_UnknownHostException + ":" + e.toString();
		} catch (IOException e) {
			Log.e(TAG, "SocketClientThread-->IOException:" + e.toString());
			result = ConstantUtil.NETWORK_STATUS_ERROR_IOException + ":" + e.toString();
		} catch( Exception e){
			Log.e(TAG, "SocketClientThread-->IOException:" + e.toString());
			result = ConstantUtil.NETWORK_STATUS_ERROR_Exception + ":" + e.toString();
		} finally {
			try {
				if(socket != null){
					socket.close();
				}
			} catch (IOException e) {
				Log.e(TAG,"SocketClientThread--> socket.close() is failed!" + e.toString());
			}
			message.obj = result;
			handler.sendMessage(message);
		}
	}
}

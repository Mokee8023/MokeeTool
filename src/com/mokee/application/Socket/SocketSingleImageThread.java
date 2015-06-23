package com.mokee.application.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.CharBuffer;

import com.mokee.database.SPSetting.MyValuesInt;
import com.mokee.database.SPSetting.MyValuesString;
import com.mokee.network.NetConnect.ConstantUtil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketSingleImageThread extends Thread {
	private static final String tag = "SocketThread";
	
	private Handler mHandler;
	private byte[] mImageBytes;
	
	private BufferedWriter mWriter = null;
	private BufferedReader mReader = null;
	private DataOutputStream mDos = null;
	private Socket socket = null; 

	public SocketSingleImageThread(Handler handler, byte[] imageBytes){
		this.mHandler = handler;
		this.mImageBytes = imageBytes;
	}
	
	@Override
	public void run() {
		super.run();
		Message openMessage = new Message();
		openMessage.what = ConstantUtil.SOCKET_CLIENT_THREAD_OPEN;
		try {
			socket = new Socket(MyValuesString.getSocketIP(), MyValuesInt.getSocketPort());
			socket.setKeepAlive(true);
			openMessage.obj = "Socket connect success";
			mHandler.sendMessage(openMessage);
			
			Log.i(tag, "SocketThread.openSocket success");
		} catch (UnknownHostException e) {
			Log.e(tag, "SocketThread.openSocket-->UnknownHostException:" + e.toString());
			openMessage.obj = "SocketThread.openSocket-->UnknownHostException:" + e.toString();
			mHandler.sendMessage(openMessage);
			socket = null;
		} catch (IOException e) {
			Log.e(tag, "SocketThread.openSocket-->IOException:" + e.toString());
			openMessage.obj = "SocketThread.openSocket-->IOException:" + e.toString();
			mHandler.sendMessage(openMessage);
			socket = null;
		}
		
		if(socket != null){
			try {
				mWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				mDos = new DataOutputStream(socket.getOutputStream());
				mReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				
				Log.i(tag, "initStreamClass is success");
			} catch (IOException e) {
				Log.e(tag, "SocketThread-->IOException:" + e.toString());
				Message streamMessage = new Message();
				streamMessage.what = ConstantUtil.SOCKET_CLIENT_THREAD_INIT;
				streamMessage.obj = ConstantUtil.NETWORK_STATUS_ERROR_IOException + ":" + e.toString();
				mHandler.sendMessage(streamMessage);
				mWriter = null;
				mDos = null;
				mReader = null;
			}
			
			if(mWriter != null && mDos != null && mReader != null){
				Message sendMessage = new Message();
				sendMessage.what = ConstantUtil.SOCKET_CLIENT_THREAD_SEND;
				if(mDos != null || mWriter != null){
					try {
						mDos.write(mImageBytes);
						mDos.flush();
						sendMessage.obj = "Send data success";
						Log.i(tag, "SocketThread.sendSocketData-->success");
					} catch (IOException e) {
						sendMessage.obj = "Send failed:" + e.toString();
						Log.e(tag, "SocketThread.sendSocketData-->IOException:" + e.toString());
					} finally {
						mHandler.sendMessage(sendMessage);
					}
				} else {
					sendMessage.obj = "Send failed, dos and writer is null";
					Log.e(tag, "Send failed, dos and writer is null");
					mHandler.sendMessage(sendMessage);
				}
				
				try {
					Thread.sleep(1000 * 2);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				Message acceptMessage = new Message();
				acceptMessage.what = ConstantUtil.SOCKET_CLIENT_THREAD_ACCEPT_DATA;
				try {
					// char[] buffer = new char[1024];
					// mReader.read(buffer);
					
					// CharBuffer charBuffer = CharBuffer.allocate(1024);
					// mReader.read(charBuffer);
					
					String line = mReader.readLine();
					acceptMessage.obj = line;
					Log.i(tag, "SocketThread.acceptSocketData-->success:" + line);
				} catch (IOException e) {
					Log.e(tag, "SocketThread.openSocket-->IOException:" + e.toString());
					acceptMessage.obj = "SocketThread.openSocket-->IOException:" + e.toString();
				} finally {
					mHandler.sendMessage(acceptMessage);
				}
				
				Message closeMessage = new Message();
				closeMessage.what = ConstantUtil.SOCKET_CLIENT_THREAD_CLOSE;
				try {
					socket.close();
					mWriter.close();
					mReader.close();
					mDos.close();
					
					Log.i(tag, "SocketThread.Close-->success");
					closeMessage.obj = "Socket disconnect success";
				} catch (IOException e) {
					Log.e(tag, "SocketThread.closeSocket-->IOException:" + e.toString());
					closeMessage.obj = "SocketThread.closeSocket-->IOException:" + e.toString();
				} finally {
					mHandler.sendMessage(closeMessage);
				}
			} else {
				if(socket != null){
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

package cn.mokee.NetConnect;

import java.io.BufferedWriter;
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
	
	private Socket socket; 
	
	/**
	 * Socket客户端，向指定ip和port的服务端，发送文字信息
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
	}
	
	@Override
	public void run() {
		super.run();
		
		Message message = new Message();
		message.what = ConstantUtil.SOCKET_CLIENT_THREAD;
		
		String result = null;
		try {
			socket = new Socket(ip, port);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(msg);
			writer.flush();
			result = ConstantUtil.NETWORK_STATUS_SUCCESS;
			
			Log.i(TAG, "SocketClientThread-->Send Socket Success:" + result);
		} catch (UnknownHostException e) {
			Log.e(TAG, "SocketClientThread-->UnknownHostException:" + e.toString());
			result = ConstantUtil.NETWORK_STATUS_ERROR_UnknownHostException;
		} catch (IOException e) {
			Log.e(TAG, "SocketClientThread-->IOException:" + e.toString());
			result = ConstantUtil.NETWORK_STATUS_ERROR_IOException;
		} catch( Exception e){
			Log.e(TAG, "SocketClientThread-->IOException:" + e.toString());
			result = ConstantUtil.NETWORK_STATUS_ERROR_Exception;
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				Log.e(TAG,"SocketClientThread--> socket.close() is failed!" + e.toString());
			}
			message.obj = result;
			handler.sendMessage(message);
		}
	}
}

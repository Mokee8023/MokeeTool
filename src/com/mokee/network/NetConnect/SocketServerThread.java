package com.mokee.network.NetConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketServerThread extends Thread {
	private final String TAG = getClass().getName();
	
	public static final int DemoSocketServer = 1;
	private Handler handler;
	/** Server Port */
	private int serverPort;
	private ServerSocket server;
	private Socket socket;
	private StringBuilder sb;
	
	/**
	 * 注册Socket服务，一直监听端口：port，程序处于死循环中
	 * 
	 * @param handler
	 *            用于传递信息的Handler
	 * @param port
	 *            SocketServer监听的端口
	 */
	public SocketServerThread(Handler handler,int port){
		this.handler = handler;
		this.serverPort = port;
	}
	
	@Override
	public void run() {
		super.run();
		
		Message msg = new Message();
		msg.what = ConstantUtil.SOCKET_SERVER_THREAD;
		
		try {
			server = new ServerSocket(serverPort);
			
			while (true) {
				/** 在此处会阻塞线程 */
				socket = server.accept();
				sb = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
				
				/** 读取数据 */
				String str = null;
				while((str = br.readLine()) != null){
					sb.append(str);  
					sb.append("\n"); 
				}
				
				/** 关闭资源 */
				br.close();
				socket.close();
				
				msg.obj = sb.toString();
				handler.sendMessage(msg);
				
				Log.i(TAG, "SocketServerThread-->Result:" + sb.toString());
			}
		} catch (IOException e) {
			Log.e(TAG, "SocketServerThread-->Creating ServerSocket Fails:" + e.toString());
			
			msg.obj = ConstantUtil.NETWORK_STATUS_ERROR_IOException;
			handler.sendMessage(msg);
		}
	}
}

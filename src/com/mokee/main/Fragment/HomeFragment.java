package com.mokee.main.Fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.application.API.API;
import com.mokee.network.NetConnect.NetworkConnectionUtil;
import com.mokee.network.NetConnect.WifiManagerUtil;
import com.mokee.tools.R;
import com.mokee.tools.TimeService.TimeService;
import com.mokee.tools.Util.MobilePhoneUtil;
import com.mokee.tools.Util.SIMUtil;

public class HomeFragment extends Fragment implements OnLongClickListener {

	private LinearLayout layout_HomeFragment;
	private TextView tv_SystemTime;
	private TextView tv_Wifi;
	private TextView tv_SIM;
	private TextView tv_System;
	
	private StringBuilder sb;
	private WifiManagerUtil wifiUtil;
	private NetworkConnectionUtil netUtil;
	private SIMUtil simUtil;
	private MobilePhoneUtil mobileUtil;

	private Handler MyHomeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case API.TIMESERVICE:
				tv_SystemTime.setText(msg.obj.toString());
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		
		initView(view);
		initEvent();

		return view;
	}

	private void initView(View view) {
		layout_HomeFragment = (LinearLayout) view.findViewById(R.id.layout_HomeFragment);
		tv_SystemTime = (TextView) view.findViewById(R.id.tv_SystemTime);
		tv_Wifi = (TextView) view.findViewById(R.id.tv_Wifi);
		tv_SIM = (TextView) view.findViewById(R.id.tv_SIM);
		tv_System = (TextView) view.findViewById(R.id.tv_System);
		
		layout_HomeFragment.setOnLongClickListener(this);
		tv_SystemTime.setOnLongClickListener(this);
		tv_Wifi.setOnLongClickListener(this);
		tv_SIM.setOnLongClickListener(this);
		tv_System.setOnLongClickListener(this);
	}
	
	private void initEvent() {
		
		netUtil = NetworkConnectionUtil.getInstance(getActivity());
		wifiUtil = new WifiManagerUtil(getActivity());
		simUtil = SIMUtil.getInstance(getActivity());
		mobileUtil = MobilePhoneUtil.getInstance(getActivity());
		sb = new StringBuilder();
		
		if(wifiUtil.checkState() == WifiManager.WIFI_STATE_ENABLED){
			sb.append("Ip Address：").append(WifiManagerUtil.intIPToStringIp(wifiUtil.getIpAddress())).append("\n\n");
			sb.append("Mac Address：").append(wifiUtil.getMacAddress()).append("\n\n");
			sb.append("BSSID：").append(wifiUtil.getBSSID());
		} else {
			sb.append("Wifi did not open，please open Wifi.");
		}
		tv_Wifi.setText(sb.toString());
		
		sb.delete(0, sb.length());
		
		sb.append("Operator：" + simUtil.getSimOperatorName() + "\n\n");
		String netType = netUtil.getCurrentNetType();
		if(netType.equals("null")){
			sb.append("Network Type：").append("The network did not open.").append("\n\n");
		} else {
			sb.append("Network Type：").append(netType).append("\n\n");
		}
		
		long[] romMemroy = mobileUtil.getRomMemroy();
		long[] internalSdCardMemroy = mobileUtil.getInternalSDCardMemory();
		long[] externalSdCardMemroy = mobileUtil.getExternalSDCardMemory();
		int[] runTimes = mobileUtil.getSystemRunTimes();
		String[] version = mobileUtil.getVersion();
		
		sb.append("SIM Serial：" + simUtil.getSimSerialNumber()).append("\n\n");
		sb.append("IMEI/MEID：" + mobileUtil.getImeiOrMeid()).append("\n\n");
		sb.append("Phone Number：" + mobileUtil.getPhoneNumber());
		
		tv_SIM.setText(sb.toString());
		
		sb.delete(0, sb.length());
		
		sb.append("RAM->").append("Total：" + mobileUtil.getRAMTotalMemory()).append("		Avail：" + mobileUtil.getRAMAvailMemory()).append("\n\n");
		sb.append("Rom->").append("Total：").append(Formatter.formatFileSize(getActivity(), romMemroy[0])).append("		Avail：").append(Formatter.formatFileSize(getActivity(), romMemroy[1])).append("\n\n");
		sb.append("Internal->").append("Total：").append(Formatter.formatFileSize(getActivity(), internalSdCardMemroy[0])).append("		Avail：").append(Formatter.formatFileSize(getActivity(), internalSdCardMemroy[1])).append("\n\n");
		sb.append("External->").append("Total：").append(Formatter.formatFileSize(getActivity(), externalSdCardMemroy[0])).append("		Avail：").append(Formatter.formatFileSize(getActivity(), externalSdCardMemroy[1])).append("\n\n");
		sb.append("System Run Times：" + runTimes[0]).append(" Hour ").append(runTimes[1]).append(" Minute").append("\n\n");
		
		sb.append("CPU Serial：" + mobileUtil.getCPUSerial()).append("\n\n");
		
		sb.append("Version:\n\n");
		sb.append("		Model Version：").append(version[2]).append("\n\n");
		sb.append("		Firmware version：").append(version[1]).append("\n\n");
		sb.append("		Kernel Version：").append(version[0]).append("\n\n");
		sb.append("		System version：").append(version[3]);
		
		tv_System.setText(sb.toString());
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initThread();// 时间线程
	}

	private void initThread() {
		new TimeService(MyHomeHandler).start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onLongClick(View view) {

		ClipboardManager cbm = (ClipboardManager) getActivity()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		switch (view.getId()) {
		case R.id.tv_SystemTime:
			cbm.setText(tv_SystemTime.getText().toString().trim());
			Toast.makeText(getActivity(), "System time has been copied!", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.tv_Wifi:
			cbm.setText(tv_Wifi.getText().toString().trim());
			Toast.makeText(getActivity(), "Wifi info has been copied!", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.layout_HomeFragment:
			initEvent();
			Toast.makeText(getActivity(), "Refresh complete.", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.tv_SIM:
			cbm.setText(tv_SIM.getText().toString().trim());
			Toast.makeText(getActivity(), "SIM info has been copied.", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.tv_System:
			cbm.setText(tv_System.getText().toString().trim());
			Toast.makeText(getActivity(), "System info has been copied", Toast.LENGTH_SHORT).show();
			break;
			
		default:
			break;

		}
		return true;
	}
}

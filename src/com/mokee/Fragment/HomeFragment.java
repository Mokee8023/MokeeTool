package com.mokee.Fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.NetConnect.WifiManagerUtil;
import com.mokee.TimeService.TimeService;
import com.mokee.tools.MainActivity;
import com.mokee.tools.R;

public class HomeFragment extends Fragment implements OnLongClickListener {

	private TextView tv_SystemTime;
	private TextView tv_Wifi;
	
	private StringBuilder sb;
	private WifiManagerUtil wifi;

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

		tv_SystemTime = (TextView) view.findViewById(R.id.tv_SystemTime);
		tv_Wifi = (TextView) view.findViewById(R.id.tv_Wifi);
		tv_SystemTime.setOnLongClickListener(this);
		tv_Wifi.setOnLongClickListener(this);
		
		wifi = new WifiManagerUtil(getActivity());
		sb = new StringBuilder();
		
		if(wifi.checkState() == WifiManager.WIFI_STATE_ENABLED){
			sb.append("Ip Address：").append(WifiManagerUtil.intIPToStringIp(wifi.getIpAddress())).append("\n\n");
			sb.append("Mac Address：").append(wifi.getMacAddress()).append("\n\n");
			sb.append("BSSID：").append(wifi.getBSSID()).append("\n\n");
		} else {
			sb.append("Wifi不可用,请开启Wifi");
		}
		tv_Wifi.setText(sb.toString());
		
		return view;
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
			
		default:
			break;

		}
		return true;
	}
}

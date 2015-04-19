package com.mokee.Fragment;

import android.content.ClipboardManager;
import android.content.Context;
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
import com.mokee.TimeService.TimeService;
import com.mokee.tools.R;

public class HomeFragment extends Fragment implements OnLongClickListener {

	private TextView tv_SystemTime;

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
		View view = inflater.inflate(R.layout.home, container, false);

		tv_SystemTime = (TextView) view.findViewById(R.id.tv_SystemTime);
		tv_SystemTime.setOnLongClickListener(this);
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
			Toast.makeText(getActivity(), "System time has been copied!",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;

		}
		return true;
	}
}

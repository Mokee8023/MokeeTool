package com.mokee.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.Qrcode.GenerateQrcode;
import com.mokee.Qrcode.Sweep;
import com.mokee.tools.R;
import com.zxing.activity.CaptureActivity;

public class OtherFragment extends Fragment implements OnClickListener {
	private static final String TAG = "OtherFragment";

	private LinearLayout layout_Sweep;
	private LinearLayout layout_QrCode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.other, container, false);

		layout_Sweep = (LinearLayout) view.findViewById(R.id.layout_Sweep);
		layout_QrCode = (LinearLayout) view.findViewById(R.id.layout_QrCode);

		layout_Sweep.setOnClickListener(this);
		layout_QrCode.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.layout_Sweep:
			Intent sweepIntent = new Intent(getActivity(),
					CaptureActivity.class);
			startActivityForResult(sweepIntent, API.SWEEP);
			break;
		case R.id.layout_QrCode:
			Intent qrcodeIntent = new Intent(getActivity(),
					GenerateQrcode.class);
			startActivity(qrcodeIntent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case API.SWEEP:
			if (resultCode == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				String result = bundle.getString("result");

				if (result.equals("")) {
					Toast.makeText(getActivity(), "Scan results is empty!",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent resultIntent = new Intent(getActivity(), Sweep.class);
					Bundle bundleData = new Bundle();
					bundleData.putString("result", result);
					resultIntent.putExtra("result", bundleData);
					startActivity(resultIntent);
				}
			}
			break;
		default:
			break;
		}

	}
}

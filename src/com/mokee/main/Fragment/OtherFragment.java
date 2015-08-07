package com.mokee.main.Fragment;

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

import com.google.zxing.oned.rss.FinderPattern;
import com.mokee.application.API.API;
import com.mokee.application.Bomber.BomberActivity;
import com.mokee.application.Express.QueryExpressActivity;
import com.mokee.application.IdCard.QueryIdCardInfoActivity;
import com.mokee.application.PhoneNumber.PhoneNumberActivity;
import com.mokee.application.PriceCompare.GoodsPriceListActivity;
import com.mokee.application.Qrcode.GenerateQrcode;
import com.mokee.application.Qrcode.Sweep;
import com.mokee.application.Robot.RobotActivity;
import com.mokee.application.Socket.SocketDebugActivity;
import com.mokee.application.Translate.TranslateActivity;
import com.mokee.database.SPSetting.MyValuesBool;
import com.mokee.tools.R;
import com.zxing.activity.CaptureActivity;

public class OtherFragment extends Fragment implements OnClickListener {
	private LinearLayout layout_Sweep;
	private LinearLayout layout_QrCode;
	private LinearLayout layout_Contact;
	private LinearLayout layout_IdCard;
	private LinearLayout layout_GoodsPrice;
	private LinearLayout layout_Translate;
	private LinearLayout layout_QueryExpress;
	private LinearLayout layout_RobotChat;
	private LinearLayout layout_Socket;
	private LinearLayout layout_Bomber;
	
	private LinearLayout layout_1, layout_2, layout_3, layout_4;
	
	private View sweep_down_view;
	private View contact_down_view;
	private View idCard_down_view;
	private View translate_down_view;
	private View express_down_view;
	private View socket_down_view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_other, container, false);

		layout_Sweep = (LinearLayout) view.findViewById(R.id.layout_Sweep);
		layout_Socket = (LinearLayout) view.findViewById(R.id.layout_Socket);
		layout_QrCode = (LinearLayout) view.findViewById(R.id.layout_QrCode);
		layout_IdCard = (LinearLayout) view.findViewById(R.id.layout_IdCard);
		layout_Bomber = (LinearLayout) view.findViewById(R.id.layout_Bomber);
		layout_Contact = (LinearLayout) view.findViewById(R.id.layout_Contact);
		layout_RobotChat = (LinearLayout) view.findViewById(R.id.layout_RobotChat);
		layout_Translate = (LinearLayout) view.findViewById(R.id.layout_Translate);
		layout_GoodsPrice = (LinearLayout) view.findViewById(R.id.layout_GoodsPrice);
		layout_QueryExpress = (LinearLayout) view.findViewById(R.id.layout_QueryExpress);
		
		layout_1 = (LinearLayout) view.findViewById(R.id.layout_1);
		layout_2 = (LinearLayout) view.findViewById(R.id.layout_2);
		layout_3 = (LinearLayout) view.findViewById(R.id.layout_3);
		layout_4 = (LinearLayout) view.findViewById(R.id.layout_4);
		
		sweep_down_view = view.findViewById(R.id.sweep_down_view);
		socket_down_view = view.findViewById(R.id.socket_down_view);
		idCard_down_view = view.findViewById(R.id.idCard_down_view);
		express_down_view = view.findViewById(R.id.express_down_view);
		contact_down_view = view.findViewById(R.id.contact_down_view);
		translate_down_view = view.findViewById(R.id.translate_down_view);
		
		setLayoutVisibility();
		
		layout_Sweep.setOnClickListener(this);
		layout_Socket.setOnClickListener(this);
		layout_QrCode.setOnClickListener(this);
		layout_IdCard.setOnClickListener(this);
		layout_Bomber.setOnClickListener(this);
		layout_Contact.setOnClickListener(this);
		layout_RobotChat.setOnClickListener(this);
		layout_GoodsPrice.setOnClickListener(this);
		layout_Translate.setOnClickListener(this);
		layout_QueryExpress.setOnClickListener(this);

		return view;
	}
	
	private void setLayoutVisibility() {
		if(!MyValuesBool.isSweep()){
			layout_Sweep.setVisibility(View.GONE);
			sweep_down_view.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isGenerateQrCode()){
			layout_QrCode.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isPhoneNumber()){
			layout_Contact.setVisibility(View.GONE);
			contact_down_view.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isIdCard()){
			layout_IdCard.setVisibility(View.GONE);
			idCard_down_view.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isGoodsPrice()){
			layout_GoodsPrice.setVisibility(View.GONE);
			idCard_down_view.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isTranslation()){
			layout_Translate.setVisibility(View.GONE);
			translate_down_view.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isExpress()){
			layout_QueryExpress.setVisibility(View.GONE);
			express_down_view.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isRobotChat()){
			layout_RobotChat.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isSocketDebug()){
			layout_Socket.setVisibility(View.GONE);
			socket_down_view.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isHackerBomber()){
			layout_Bomber.setVisibility(View.GONE);
			socket_down_view.setVisibility(View.GONE);
		}
		
		if(!MyValuesBool.isSweep() && !MyValuesBool.isGenerateQrCode()){
			layout_1.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isPhoneNumber() && !MyValuesBool.isIdCard() && !MyValuesBool.isGoodsPrice()){
			layout_2.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isTranslation() && !MyValuesBool.isExpress() && !MyValuesBool.isRobotChat()){
			layout_3.setVisibility(View.GONE);
		}
		if(!MyValuesBool.isSocketDebug() && !MyValuesBool.isSocketDebug()){
			layout_4.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.layout_Sweep:
			Intent sweepIntent = new Intent(getActivity(), CaptureActivity.class);
			startActivityForResult(sweepIntent, API.SWEEP);
			break;
		case R.id.layout_QrCode:
			Intent qrcodeIntent = new Intent(getActivity(), GenerateQrcode.class);
			startActivity(qrcodeIntent);
			break;
		case R.id.layout_Contact:
			Intent contactIntent = new Intent(getActivity(), PhoneNumberActivity.class);
			startActivity(contactIntent);
			break;
		case R.id.layout_IdCard:
			Intent idCardIntent = new Intent(getActivity(), QueryIdCardInfoActivity.class);
			startActivity(idCardIntent);
			break;
		case R.id.layout_GoodsPrice:
			Intent goodsPriceIntent = new Intent(getActivity(), GoodsPriceListActivity.class);
			startActivity(goodsPriceIntent);
			break;
		case R.id.layout_Translate:
			Intent translateIntent = new Intent(getActivity(), TranslateActivity.class);
			startActivity(translateIntent);
			break;
		case R.id.layout_QueryExpress:
			Intent expressIntent = new Intent(getActivity(), QueryExpressActivity.class);
			startActivity(expressIntent);
			break;
		case R.id.layout_RobotChat:
			Intent robotChatIntent = new Intent(getActivity(), RobotActivity.class);
			startActivity(robotChatIntent);
			break;
		case R.id.layout_Socket:
			Intent socketIntent = new Intent(getActivity(), SocketDebugActivity.class);
			startActivity(socketIntent);
			break;
		case R.id.layout_Bomber:
			Intent bomberIntent = new Intent(getActivity(), BomberActivity.class);
			startActivity(bomberIntent);
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

package com.mokee.PriceCompare;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.SQLite.Goods;
import com.mokee.SQLite.SQLiteDBManager;
import com.mokee.Util.StringUtil;
import com.mokee.tools.R;

public class AddGoodsActivity extends Activity implements OnClickListener {
	private static final String TAG = "AddGoodsActivity";

	private ImageButton ib_Return, ib_Save;
	private TextView activity_Text;
	private EditText et_InputGoodsName, et_InputGoodsBarcode, et_InputSuperMarket1, 
					 et_InputSuperMarket2, et_InputSuperMarket3, et_InputPrice1, 
					 et_InputPrice2, et_InputPrice3, et_InputInfo;
	
	private String goodsName = null, goodsBarcode = null, super1 = null, super2 = null, super3 = null, info = null;
	private float price1 = -1, price2 = -1, price3 = -1;
	
	SQLiteDBManager dbManager;
	List<Goods> goodsList = new ArrayList<Goods>();
	Goods goods = new Goods();
	
	String requestCode;
	Bundle data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.goods_detail);
		
		Intent intent = getIntent();
		requestCode = intent.getStringExtra("requestCode");
		if(requestCode.equals("edit")){
			data = intent.getBundleExtra("data");
		}
		
		dbManager = new SQLiteDBManager(this);

		initView();
	}

	private void initView() {
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
		ib_Save = (ImageButton) findViewById(R.id.ib_Save);
		activity_Text = (TextView) findViewById(R.id.activity_Text);

		et_InputGoodsName = (EditText) findViewById(R.id.et_InputGoodsName);
		et_InputGoodsBarcode = (EditText) findViewById(R.id.et_InputGoodsBarcode);
		et_InputSuperMarket1 = (EditText) findViewById(R.id.et_InputSuperMarket1);
		et_InputSuperMarket2 = (EditText) findViewById(R.id.et_InputSuperMarket2);
		et_InputSuperMarket3 = (EditText) findViewById(R.id.et_InputSuperMarket3);
		et_InputPrice1 = (EditText) findViewById(R.id.et_InputPrice1);
		et_InputPrice2 = (EditText) findViewById(R.id.et_InputPrice2);
		et_InputPrice3 = (EditText) findViewById(R.id.et_InputPrice3);
		et_InputInfo = (EditText) findViewById(R.id.et_InputInfo);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(requestCode.equals("add")){
			activity_Text.setText(getResources().getString(R.string.add_goods_detail_title));
		} else if(requestCode.equals("edit")){
			activity_Text.setText(getResources().getString(R.string.edit_goods_detail_title));
			SetGoodsDetail();
		}
		ib_Save.setVisibility(View.VISIBLE);
		ib_Save.setOnClickListener(this);
		ib_Return.setOnClickListener(this);
	}

	private void SetGoodsDetail() {
		if(data != null){
			et_InputGoodsName.setText(data.getString("name"));
			et_InputGoodsBarcode.setText(data.getString("barcode"));
			et_InputSuperMarket1.setText(data.getString("supermarketname1"));
			et_InputSuperMarket2.setText(data.getString("supermarketname2"));
			et_InputSuperMarket3.setText(data.getString("supermarketname3"));
			
			if(data.getFloat("price1") != -1){
				et_InputPrice1.setText(data.getFloat("price1") + "");
			}
			if(data.getFloat("price2") != -1){
				et_InputPrice2.setText(data.getFloat("price2") + "");
			}
			if(data.getFloat("price3") != -1){
				et_InputPrice3.setText(data.getFloat("price3") + "");
			}
			
			et_InputInfo.setText(data.getString("info"));
		}
	}

	@Override
	public void onClick(View v) {
		
		goods = GetValue();
		Log.i(TAG, "goods:"+goods.toString());
		goodsList.clear();
		goodsList.add(goods);

		switch (v.getId()) {
		case R.id.ib_Save:
			if (!StringUtil.isNullOrEmpty(goods.getGoodsName())
					&& !StringUtil.isNullOrEmpty(goods.getSuperMarketName1())
					&& goods.getPrice1() != -1) {
				if(requestCode != null && requestCode.equals("add")){
					dbManager.add(goodsList);
				} else if(requestCode != null && requestCode.equals("edit")){
					if(goods.getGoodsName().equals(data.getString("name"))){
						dbManager.updateGoods(goods);
					} else {
						dbManager.deleteOldGoods(data.getString("name"));
						dbManager.add(goodsList);
					}
				}
				
				finish();
				onDestroy();
			} else {
				Toast.makeText(this,
						"Please input goods name and super1 and price1!",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.ib_Return:
			finish();
			onDestroy();
			break;

		default:
			break;
		}
	}

	/**
	 * 获取Goods对象的各个值
	 */
	private Goods GetValue() {
		goodsName = et_InputGoodsName.getText().toString().trim();
		goodsBarcode = et_InputGoodsBarcode.getText().toString().trim();
		super1 = et_InputSuperMarket1.getText().toString().trim();
		super2 = et_InputSuperMarket2.getText().toString().trim();
		super3 = et_InputSuperMarket3.getText().toString().trim();
		info = et_InputInfo.getText().toString().trim();
		
		if(!StringUtil.isNullOrEmpty(et_InputPrice1.getText().toString().trim())){
			price1 = Float.parseFloat(et_InputPrice1.getText().toString().trim());
		}
		if(!StringUtil.isNullOrEmpty(et_InputPrice2.getText().toString().trim())){
			price2 = Float.parseFloat(et_InputPrice2.getText().toString().trim());
		}
		if(!StringUtil.isNullOrEmpty(et_InputPrice3.getText().toString().trim())){
			price3 = Float.parseFloat(et_InputPrice3.getText().toString().trim());
		}
		
		
		Goods good;
		good = new Goods();
		if(!StringUtil.isNullOrEmpty(goodsName)){
			good.setGoodsName(goodsName);
		}
		if(!StringUtil.isNullOrEmpty(goodsBarcode)){
			good.setBarCode(goodsBarcode);
		}
		if(!StringUtil.isNullOrEmpty(super1)){
			good.setSuperMarketName1(super1);
		}
		if(!StringUtil.isNullOrEmpty(super2)){
			good.setSuperMarketName2(super2);
		}
		if(!StringUtil.isNullOrEmpty(super3)){
			good.setSuperMarketName3(super3);
		}
		if(!StringUtil.isNullOrEmpty(info)){
			good.setInfo(info);
		}
		if(price1 != -1){
			good.setPrice1(price1);
		}
		if(price2 != -1){
			good.setPrice2(price2);
			Log.i(TAG, "price2:"+price2);
		}
		if(price3 != -1){
			good.setPrice3(price3);
		}
		
		return good;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbManager.closeDB();
	}
}

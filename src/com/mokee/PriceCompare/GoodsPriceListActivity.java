package com.mokee.PriceCompare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.SQLite.Goods;
import com.mokee.SQLite.SQLiteDBManager;
import com.mokee.tools.R;

public class GoodsPriceListActivity extends Activity implements OnClickListener, OnItemClickListener {
	private static final String TAG = "GoodsPriceListActivity";
	
	private ListView lv_goodsDetail;
	private ImageButton ib_Save;
	private ImageButton ib_Return;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private TextView activity_Text;
	
	SimpleAdapter adapter;
	SQLiteDBManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_goods_list);
		
		dbManager = new SQLiteDBManager(this);
		
		initView();
	}

	private void initView() {
		lv_goodsDetail = (ListView) findViewById(R.id.lv_goodsDetail);
		activity_Text = (TextView) findViewById(R.id.activity_Text);
		ib_Save = (ImageButton) findViewById(R.id.ib_Save);
		ib_Return = (ImageButton) findViewById(R.id.ib_Return);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(list.size() > 0){
			Log.i(TAG, "list:"+list.size());
			list.clear();
		}
		
		activity_Text.setText("Goods Price");
		ib_Save.setVisibility(View.VISIBLE);
		ib_Save.setImageDrawable(getResources().getDrawable(R.drawable.other));
		
		/** 从数据库查询数据   */
		SetListData();
		
		// 设置适配器
		adapter = new SimpleAdapter(
				getApplicationContext(), list,
				R.layout.goods_list_detail,
				new String[] { "name", "barcode", "supermarketname1","price1" , 
					"supermarketname2","price2", "supermarketname3","price3","info"}, 
					new int[] {R.id.tv_GoodsName,R.id.tv_Barcode,
							   R.id.tv_SuperMarketName1,R.id.tv_Price1,
							   R.id.tv_SuperMarketName2,R.id.tv_Price2,
							   R.id.tv_SuperMarketName3,R.id.tv_Price3,
							   R.id.tv_GoodsInfo});
		if(list.size() <= 0){
			Toast.makeText(this, "无数据", Toast.LENGTH_SHORT).show();
		} else {
			lv_goodsDetail.setAdapter(adapter);
		}
		
		ib_Return.setOnClickListener(this);
		ib_Save.setOnClickListener(this);
		lv_goodsDetail.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_Return:
			finish();
			onDestroy();
			break;
			
		case R.id.ib_Save:
			Intent intent = new Intent(GoodsPriceListActivity.this,AddGoodsActivity.class);
			intent.putExtra("requestCode", "add");
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 从数据库查询数据
	 */
	private void SetListData() {
		List<Goods> goodsList = dbManager.query();
		for(Goods item : goodsList){
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("name", item.getGoodsName());
			map.put("barcode", item.getBarCode());
			map.put("supermarketname1", item.getSuperMarketName1());
			map.put("price1", item.getPrice1());
			map.put("supermarketname2", item.getSuperMarketName2());
			map.put("price2", item.getPrice2());
			map.put("supermarketname3", item.getSuperMarketName3());
			map.put("price3", item.getPrice3());
			map.put("info", item.getInfo());
			
			list.add(map);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Map<String, Object> map = (Map<String, Object>) arg0.getItemAtPosition(position);

//		Goods goods = new Goods();
//		goods.goodsName = (String) map.get("name");
//		goods.barCode = (String) map.get("barcode");
//		goods.superMarketName1 = (String) map.get("supermarketname1");
//		goods.price1 = (Float) map.get("price1");
//		goods.superMarketName2 = (String) map.get("supermarketname2");
//		goods.price2 = (Float) map.get("price2");
//		goods.superMarketName3 = (String) map.get("supermarketname3");
//		goods.price3 = (Float) map.get("price3");
//		goods.info = (String) map.get("info");
		
		Bundle data = new Bundle();
//		data.putString("requestCode","edit");
		
		data.putString("name", (String) map.get("name"));
		data.putString("supermarketname1", (String) map.get("supermarketname1"));
		data.putString("supermarketname2", (String) map.get("supermarketname2"));
		data.putString("supermarketname3", (String) map.get("supermarketname3"));
		data.putString("barcode", (String) map.get("barcode"));
		data.putString("info", (String) map.get("info"));
		
		data.putFloat("price1", (Float) map.get("price1"));
		data.putFloat("price2", (Float) map.get("price2"));
		data.putFloat("price3", (Float) map.get("price3"));
		
		Intent intent = new Intent(this, AddGoodsActivity.class);
		intent.putExtra("requestCode", "edit");
		intent.putExtra("data", data);
		startActivity(intent);
	}
}

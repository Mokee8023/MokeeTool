package com.mokee.PriceCompare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.SQLite.Goods;
import com.mokee.SQLite.SQLiteDBManager;
import com.mokee.tools.R;

public class GoodsPriceListActivity extends Activity implements OnClickListener {
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
}

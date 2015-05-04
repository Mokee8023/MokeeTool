package com.mokee.PriceCompare;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mokee.tools.R;

public class GoodsPriceList extends Activity {
	private static final String TAG = "GoodsPriceList";
	
	private ListView lv_goodsDetail;
	private ImageButton ib_Save;
	private ImageButton ib_Return;
	private TextView activity_Text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_list);
		
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
	}
}

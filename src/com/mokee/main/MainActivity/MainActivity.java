package com.mokee.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mokee.database.SPSetting.MySharedPreferences;
import com.mokee.main.Fragment.HomeFragment;
import com.mokee.main.Fragment.OtherFragment;
import com.mokee.main.Help.HelpActivity;
import com.mokee.tools.R;
import com.mokee.widget.CircleProgress.CircleProgress;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private final String TAG = "MainActivity";

	private ViewPager viewPager;

	private ImageButton ib_Home;
	private ImageButton ib_Other;
	private ImageButton ib_Help;

	private TextView tv_Home;
	private TextView tv_Other;
	
	// private HomeFragment homeFragment;
	// private TranslationFragment translationFragment;
	// private OtherFragment otherFragment;

	private List<Fragment> list;
	private Long exitTime = 0L;
	private static final int EXITINTERVAL = 2000;// 退出间隔
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		initViewPager();
		setOriginState();
		setSelectedPager(1);
		
		MySharedPreferences.init(this);
	}

	private void initView() {

		ib_Home = (ImageButton) findViewById(R.id.ib_Home);
		ib_Other = (ImageButton) findViewById(R.id.ib_Other);
		ib_Help = (ImageButton) findViewById(R.id.ib_Help);

		tv_Home = (TextView) findViewById(R.id.tv_Home);
		tv_Other = (TextView) findViewById(R.id.tv_Other);

		ib_Home.setOnClickListener(this);
		ib_Other.setOnClickListener(this);
		ib_Help.setOnClickListener(this);

	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vp_ViewPager);

		list = new ArrayList<Fragment>();

		HomeFragment home = new HomeFragment();
		OtherFragment other = new OtherFragment();

		list.add(home);
		list.add(other);

		FragmentPagerAdapter adapter = new FragmentPagerAdapter(
				getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return list.get(arg0);
			}
		};

		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(1);// 初始化的Fragment
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				setOriginState();
				setSelectedPager(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	public void setOriginState() {
		ib_Home.setBackgroundResource(R.drawable.home_nomal_gray);
		ib_Other.setBackgroundResource(R.drawable.other_normal_gray);

		tv_Home.setTextColor(Color.BLACK);
		tv_Other.setTextColor(Color.BLACK);
	}

	public void setSelectedPager(int position) {
		switch (position) {
		case 0:
			ib_Home.setBackgroundResource(R.drawable.home_nomal_bright);
			tv_Home.setTextColor(Color.BLUE);
			break;
		case 1:
			ib_Other.setBackgroundResource(R.drawable.other_nomal_bright);
			tv_Other.setTextColor(Color.BLUE);
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_Help:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, HelpActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_Home:
			setOriginState();
			setSelectedPager(0);
			viewPager.setCurrentItem(0);
			break;
		case R.id.ib_Other:
			setOriginState();
			setSelectedPager(1);
			viewPager.setCurrentItem(1);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (System.currentTimeMillis() - exitTime > EXITINTERVAL) {// 2s
				Toast.makeText(getApplicationContext(),
						"Press again to exit the program！", Toast.LENGTH_SHORT)
						.show();
				exitTime = System.currentTimeMillis();
			} else {
				// onDestroy();// 只是结束此Activity的生命周期
				finish();// 将活动推向后台，并没有立即释放内存
				System.exit(0);// 杀死了整个Application，活动所占的资源被释放
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}

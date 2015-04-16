package com.mokee.tools;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mokee.Fragment.HomeFragment;
import com.mokee.Fragment.OtherFragment;
import com.mokee.Fragment.TranslationFragment;
import com.mokee.Help.HelpActivity;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private final String TAG = "MainActivity";

	private ViewPager viewPager;

	private ImageButton ib_Translation;
	private ImageButton ib_Home;
	private ImageButton ib_Other;
	private ImageButton ib_Help;

	private TextView tv_Translation;
	private TextView tv_Home;
	private TextView tv_Other;

	// private HomeFragment homeFragment;
	// private TranslationFragment translationFragment;
	// private OtherFragment otherFragment;

	private List<Fragment> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initView();
		initViewPager();
		setOriginState();
		setSelectedPager(1);
	}
	
	private void initView() {

		ib_Translation = (ImageButton) findViewById(R.id.ib_Translation);
		ib_Home = (ImageButton) findViewById(R.id.ib_Home);
		ib_Other = (ImageButton) findViewById(R.id.ib_Other);
		ib_Help = (ImageButton) findViewById(R.id.ib_Help);

		tv_Translation = (TextView) findViewById(R.id.tv_Translation);
		tv_Home = (TextView) findViewById(R.id.tv_Home);
		tv_Other = (TextView) findViewById(R.id.tv_Other);
		
		ib_Translation.setOnClickListener(this);
		ib_Home.setOnClickListener(this);
		ib_Other.setOnClickListener(this);
		ib_Help.setOnClickListener(this);

	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vp_ViewPager);

		list = new ArrayList<Fragment>();

		HomeFragment home = new HomeFragment();
		TranslationFragment translation = new TranslationFragment();
		OtherFragment other = new OtherFragment();

		list.add(translation);
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
		ib_Translation.setBackgroundResource(R.drawable.translation_nomal);
		ib_Home.setBackgroundResource(R.drawable.home_nomal);
		ib_Other.setBackgroundResource(R.drawable.other_normal);

		tv_Translation.setTextColor(Color.BLACK);
		tv_Home.setTextColor(Color.BLACK);
		tv_Other.setTextColor(Color.BLACK);
	}

	public void setSelectedPager(int position) {
		switch (position) {
		case 0:
			ib_Translation
					.setBackgroundResource(R.drawable.translation_pressed);
			tv_Translation.setTextColor(Color.BLUE);
			break;
		case 1:
			ib_Home.setBackgroundResource(R.drawable.home_pressed);
			tv_Home.setTextColor(Color.BLUE);
			break;
		case 2:
			ib_Other.setBackgroundResource(R.drawable.other_pressed);
			tv_Other.setTextColor(Color.BLUE);
			break;
		default:
			break;
		}

	}

	@Override
	public void onClick(View view) {
		Log.i(TAG, "view.getId()：" + view.getId());
		switch (view.getId()) {
		case R.id.ib_Help:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, HelpActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_Translation:
			setOriginState();
			setSelectedPager(0);
			viewPager.setCurrentItem(0);
			break;
		case R.id.ib_Home:
			setOriginState();
			setSelectedPager(1);
			viewPager.setCurrentItem(1);
			break;
		case R.id.ib_Other:
			setOriginState();
			setSelectedPager(2);
			viewPager.setCurrentItem(2);
			break;
		}
	}
}

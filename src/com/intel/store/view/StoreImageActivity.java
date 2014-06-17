package com.intel.store.view;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.intel.store.R;
import com.intel.store.view.fragment.SeasonCurrentFragment;
import com.intel.store.view.fragment.SeasonHistoryFragment;
import com.intel.store.widget.LoadingView;
import com.intel.store.widget.SyncHorizontalScrollView;

public class StoreImageActivity extends BaseActivity{
	private  ArrayList<Fragment> mFragments;
	private ViewPager mViewPager;
	private RelativeLayout rl_nav;
	private SyncHorizontalScrollView mHsv;
	public RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private LoadingView loadingView;
	private int indicatorWidth;
	private TabFragmentPagerAdapter mAdapter;
	private SeasonCurrentFragment currentFragment;
	private SeasonHistoryFragment historyFragment;
	private int currentIndicatorLeft = 0;
	private LayoutInflater mInflater;
	private static String[] tabTitle = {"当前季度上传轮次","历史季度上传轮次"}; // 标题
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workprop);
		initPassedValues();
		initView();
		setListener();
	}

	private void initPassedValues() {
		//TODO
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 	Intent intent_recent = new Intent();
			intent_recent.setClass(StoreImageActivity.this,
					StoreImageRecentActivity.class);
			startActivity(intent_recent);
			*/
		return super.onOptionsItemSelected(item);
	}
	
	public void initView() {
		rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		indicatorWidth = dm.widthPixels / 2;
		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);

		mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, this);

		// 获取布局填充器
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		initNavigationHSV();
		mViewPager.setAdapter(mAdapter);
		mViewPager=(ViewPager) findViewById(R.id.mViewPager);
		mFragments=new ArrayList<Fragment>();
		currentFragment=new SeasonCurrentFragment();
		historyFragment=new SeasonHistoryFragment();
		mFragments.add(currentFragment);
		mFragments.add(historyFragment);
		mAdapter=new TabFragmentPagerAdapter(getSupportFragmentManager(),mFragments);
		mViewPager.setAdapter(mAdapter);
	}

	private void initNavigationHSV() {
		rg_nav_content.removeAllViews();
		for (int i = 0; i < tabTitle.length; i++) {
			RadioButton rb = (RadioButton) mInflater.inflate(
					R.layout.nav_radiogroup_item, null);
			if (i == 0) {
				rb.setChecked(true);
			}
			rb.setId(i);
			rb.setText(tabTitle[i]);
			rb.setLayoutParams(new LayoutParams(indicatorWidth,
					LayoutParams.MATCH_PARENT));
			rg_nav_content.addView(rb);
		}
	}
	public void setListener() {

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (rg_nav_content != null
						&& rg_nav_content.getChildCount() > position) {
					((RadioButton) rg_nav_content.getChildAt(position))
							.performClick();
				}
				if (position == 0){
					currentFragment.DataLoaded();
				}
				if (position == 1) {
					historyFragment.DataLoaded();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		rg_nav_content
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (rg_nav_content.getChildAt(checkedId) != null) {
							RadioButton currentCheckedRadioButton = (RadioButton) rg_nav_content
									.getChildAt(checkedId);
							TranslateAnimation animation = new TranslateAnimation(
									currentIndicatorLeft,
									currentCheckedRadioButton.getLeft(), 0f, 0f);
							animation.setInterpolator(new LinearInterpolator());
							animation.setDuration(100);
							animation.setFillAfter(true);

							// 执行位移动画
							iv_nav_indicator.startAnimation(animation);

							mViewPager.setCurrentItem(checkedId); // ViewPager
																	// 跟随一起 切换

							// 记录当前 下标的距最左侧的 距离
							currentIndicatorLeft = currentCheckedRadioButton
									.getLeft();
						}
					}
				});

	
		
	}

	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		private ArrayList<Fragment> mFragments=new ArrayList<Fragment>();
		public TabFragmentPagerAdapter(FragmentManager manager) {
			super(manager);
		}
		public TabFragmentPagerAdapter(FragmentManager manager,ArrayList<Fragment> fragments) {
			super(manager);
			this.mFragments=fragments;
		}

		@Override
		public Fragment getItem(int index) {
			return mFragments.get(index);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	
}

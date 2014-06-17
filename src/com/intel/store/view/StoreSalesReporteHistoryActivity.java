package com.intel.store.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.view.fragment.SaleReportHistoryUnuploadFragment;
import com.intel.store.view.fragment.SaleReportHistoryUploadedFragment;
import com.intel.store.widget.LoadingView;
import com.intel.store.widget.SyncHorizontalScrollView;
import com.pactera.framework.util.ToastHelper;

public class StoreSalesReporteHistoryActivity extends BaseActivity {

	private RelativeLayout rl_nav;
	private SyncHorizontalScrollView mHsv;
	public RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private ViewPager mViewPager;
	private int indicatorWidth;
	private static String[] tabTitle = {StoreApplication.getAppContext().getString(R.string.sales_reporte_uploaded),StoreApplication.getAppContext().getString(R.string.sales_reporte_not_uploaded) }; // 标题
	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;
	// private ProgressDialog mProgressDialog;
	static Fragment f1, f2;

	private static final int UPDATE_SUCCESS = 1;

	private LoadingView loadingView;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_SUCCESS:
				loadingView.hideLoading();
				((SaleReportHistoryUploadedFragment) f1).DataLoaded();
				((SaleReportHistoryUnuploadFragment) f2).DataLoaded();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_sales_report_history);
		findViewById();
		initView();
		setListener();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				handler.sendEmptyMessage(UPDATE_SUCCESS);
			}
		},50);
	}

	private void setListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (rg_nav_content != null
						&& rg_nav_content.getChildCount() > position) {
					((RadioButton) rg_nav_content.getChildAt(position))
							.performClick();
				}
				if (position == 0
						&& ((SaleReportHistoryUnuploadFragment) f2).f1Changed) {
					((SaleReportHistoryUploadedFragment) f1).DataLoaded();
					((SaleReportHistoryUnuploadFragment) f2).f1Changed = false;
				}
				if (position == 1) {
					((SaleReportHistoryUnuploadFragment) f2).DataLoaded();
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
//		btn_back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (((SaleReportHistoryUnuploadFragment) f2).inUpload) {
//					ToastHelper.getInstance().showShortMsg("正在上传销量，不能返回");
//					return;
//				}
//				finish();
//			}
//		});

	}

	private void initView() {
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
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
//		btn_back = (Button) findViewById(R.id.common_id_btn_back);
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

	private void findViewById() {
		rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
	}

	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				f1 = new SaleReportHistoryUploadedFragment();
				return f1;
			default:
				f2 = new SaleReportHistoryUnuploadFragment();
				return f2;
			}
		}

		@Override
		public int getCount() {
			return tabTitle.length;
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& ((SaleReportHistoryUnuploadFragment) f2).inUpload) {
			ToastHelper.getInstance().showShortMsg(getString(R.string.sales_reporte_uploading));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode==SaleReportDetailActivity.ResultCodeDelSuccess) {
			((SaleReportHistoryUnuploadFragment)f2).DataLoaded();
		}
	}
}

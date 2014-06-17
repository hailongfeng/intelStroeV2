package com.intel.store.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.intel.store.model.CheckTarVersionModel;
import com.intel.store.model.ProductTypeEnum;
import com.intel.store.model.UpdateDataVersion;
import com.intel.store.util.StoreSession;
import com.intel.store.view.fragment.ProductFindFragment;
import com.intel.store.widget.LoadingView;
import com.intel.store.widget.SyncHorizontalScrollView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PhoneStateUtil;
import com.pactera.framework.util.ToastHelper;

public class ProductFindActivityOld extends BaseFragmentActivity {

	private RelativeLayout rl_nav;
	private SyncHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private ViewPager mViewPager;
	private int indicatorWidth;
	private static String[] tabTitle = { "Mobile", "Desktop" }; // 标题
	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;
	private Button btn_back;
	private StoreController storeController;
	// private ProgressDialog mProgressDialog;
	static Fragment f1, f2;

	public static final int UPDATE_SUCCESS = 1;
	public static final int UPDATE_FAIL = 2;
	UpdateDataVersion updateDataVersion;
	private LoadingView loadingView;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_SUCCESS:
				loadingView.hideLoading();
				((ProductFindFragment) f1).DataLoaded();
				((ProductFindFragment) f2).DataLoaded();
				break;
			case UPDATE_FAIL:
				loadingView.errorLoaded(getString(R.string.txt_fail_retry)
						+ "\n" + getString(R.string.txt_fail_detail)
						+ msg.obj.toString());
				ToastHelper.getInstance().showLongMsg(
						getString(R.string.txt_fail));
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_find_old);
		findViewById();
		initView();
		setListener();
		updateDataVersion=new UpdateDataVersion(ProductFindActivityOld.this,handler);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				updateDataVersion.LoadDataFromLocal();
			}
		}, 100);
		// 更新之前调用回调接口
		Loger.d("产品版本：" + StoreSession.getDataVersion());
		storeController.checkTarVersion(new CheckTarVersionUpdateView(
				ProductFindActivityOld.this), StoreSession.getDataVersion());

	}

	private void setListener() {
		loadingView.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				loadingView.showLoading();
				if (PhoneStateUtil.hasInternet()) {
					storeController.checkTarVersion(
							new CheckTarVersionUpdateView(
									ProductFindActivityOld.this), StoreSession
									.getDataVersion());
				} else {
					ToastHelper.getInstance()
							.showLongMsg(
									getResources().getString(
											R.string.comm_no_internet));
					loadingView.errorLoaded(getString(R.string.txt_fail));
				}
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (rg_nav_content != null
						&& rg_nav_content.getChildCount() > position) {
					((RadioButton) rg_nav_content.getChildAt(position))
							.performClick();
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
							// int x=(checkedId > 1 ?currentIndicatorLeft : 0) -
							// ((RadioButton)
							// rg_nav_content.getChildAt(2)).getLeft();
							// mHsv.smoothScrollTo(x, 0);
						}
					}
				});
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);

		storeController = new StoreController();
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

		// 另一种方式获取
		// LayoutInflater mInflater = LayoutInflater.from(this);

		initNavigationHSV();
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		btn_back = (Button) findViewById(R.id.common_id_btn_back);
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
		// iv_nav_left = (ImageView) findViewById(R.id.iv_nav_left);
		// iv_nav_right = (ImageView) findViewById(R.id.iv_nav_right);

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
				f1 = new ProductFindFragment(ProductTypeEnum.Mobile);
				return f1;
			default:
				f2 = new ProductFindFragment(ProductTypeEnum.Desktop);
				return f2;
			}
		}

		@Override
		public int getCount() {

			return tabTitle.length;
		}

	}

	private class CheckTarVersionUpdateView extends
			StoreCommonUpdateView<MapEntity> {
		public CheckTarVersionUpdateView(Context context) {
			super(context);

		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(MapEntity result) {
			if (!result.getBool(CheckTarVersionModel.RESULT)) {
				Loger.d("有更新");
				updateDataVersion.update(result);
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		public void handleException(IException ex) {
			loadingView.errorLoaded(ex.getMessage());
			ex.printStackTrace();
		}
	}

}

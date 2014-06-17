package com.intel.store.view;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.intel.store.StoreApplication;
import com.intel.store.controller.StoreController;
import com.intel.store.model.ProductTypeEnum;
import com.intel.store.view.fragment.ProductAddTypeSearchFragment;
import com.intel.store.view.fragment.ProductAddTypeSelectFragment;
import com.intel.store.widget.LoadingView;
import com.intel.store.widget.SyncHorizontalScrollView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;

public class ProductAddTypeActivity extends BaseFragmentActivity {

	private RelativeLayout rl_nav;
	private SyncHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private ViewPager mViewPager;
	private int indicatorWidth;
	private static String[] tabTitle = { "搜索", "类别" }; // 标题
	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;
	private Button btn_back;
	private StoreController storeController;
	// private ProgressDialog mProgressDialog;
	static Fragment f1, f2;

	public static final int LOCAL_EXIT = 1;
	public static final int UPDATE_SUCCESS = 2;
	public static final int UPDATE_FAIL = 3;
	private LoadingView loadingView;

	public static String photeQty = "";

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOCAL_EXIT:
				loadingView.hideLoading();
				((ProductAddTypeSearchFragment) f1).DataLoaded();
				((ProductAddTypeSelectFragment) f2).DataLoaded();
				break;
			case UPDATE_FAIL:
				loadingView.hideLoading();
				loadingView.errorLoaded(StoreApplication.getAppContext().getString(R.string.txt_fail_retry));
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_add_tpye);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			photeQty = bundle.getString("PHT_QTY");
		}
		findViewById();
		initView();
		setListener();
		loadingView.showLoading();
			// 从网络下载数据
			storeController.getProductTypeFromeRemote(new ProductLoadUpdateView(ProductAddTypeActivity.this),
					"all");

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
				startActivity(new Intent(ProductAddTypeActivity.this,StoreImageTypeListActivity.class));
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
				f1 = new ProductAddTypeSearchFragment(ProductTypeEnum.Mobile);
				return f1;
			default:
				f2 = new ProductAddTypeSelectFragment();
				return f2;
			}
		}

		@Override
		public int getCount() {

			return tabTitle.length;
		}

	}

	private class ProductLoadUpdateView extends
			StoreCommonUpdateView<List<MapEntity>> {
		
		public ProductLoadUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
		}


		@Override
		public void onPostExecute(List<MapEntity> data) {
			//storeController.deleteProductType();
			storeController.saveProductType(data);
			// 下载完成后，相当于本地存在
			handler.sendEmptyMessage(ProductAddTypeActivity.LOCAL_EXIT);
		}

		@Override
		public void handleException(IException ex) {
			DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					storeController.getProductTypeFromeRemote(new ProductLoadUpdateView(ProductAddTypeActivity.this),
							"all");
				}
			};
			viewHelper.showErrorDialog(ex, positiveListener, null);
			ex.printStackTrace();
		}
	}

}

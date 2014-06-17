package com.intel.store.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.intel.store.util.StoreSession;
import com.intel.store.util.UpgradeProgress;
import com.intel.store.util.ViewHelper;
import com.intel.store.view.setting.StoreSettingActivity;
import com.intel.store.view.webview.IrepStudyActivity;
import com.intel.store.view.webview.ProductFindActivity;
import com.intel.store.widget.LoadingView;
import com.intel.store.widget.PopupWindowIREPLogin;
import com.pactera.framework.exception.IException;
import com.pactera.framework.util.PhoneStateUtil;
import com.pactera.framework.util.ThreadUtils;
import com.pactera.framework.util.ToastHelper;

public class MainActivity extends BaseActivity {

	private PopupWindowIREPLogin mPopupWindow_irsp;
	private String[] storeFunctionNames;
	// <item>门店业绩</item>
	// <item>销量申报</item>
	// <item>产品速查</item>
	// <item>IREP培训</item>
	
	// <item>门店图片</item>
	// <item>门店积分</item>
	// <item>我的店员</item>
	// <item>门店资料</item>
	
	private Class[] storeFunctionClazz = new Class[] {
			StoreSalesCountActivity.class, 
			StoreSalesReporteActivity.class,
			ProductFindActivity.class, 
			StoreIrepLoginActivity.class,
			StoreImageActivity.class, 
			StoreIntegralActivity.class,
			StoreMyClerkActivity.class, 
			StoreInfoActivity.class };
	private Boolean[] storeFunctionSelector = new Boolean[] {
			false,true,false,false,
			true,false,true,true };
	
	private int[] storeFunctionIcon = new int[] {
			R.drawable.store_main_function_yeji,
			R.drawable.store_main_function_sales_reportor,
			R.drawable.store_main_function_product_search,
			R.drawable.store_main_function_irep_study,
			R.drawable.store_main_function_picture,
			R.drawable.store_main_function_integral,
			R.drawable.store_main_function_my_clerk,
			R.drawable.store_main_function_store_info };

	// private int[] storeFunctionIcons={
	// R.drawable.
	//
	// };
	private StoreFunctionAdapter adapter;
	private ListView listView;
	private static final int PERSONAL_INFO_REQUEST_ID = 1;
	public static final int LOG_OUT_RESULT_CODE = 22;
	public static final int SCAN_RESULT_CODE = 24;

	private StoreController storeController;
	private LoadingView loadingView;
	private LoadingView loadingView2;

	private String sr_id = "";
	private long exitTime = 0;
	private UpdateView viewUpdateCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StoreSession.setCurrentStoreRole("");
		actionBar.setTitle("零售宝");
		actionBar.setDisplayShowHomeEnabled(true);// 返回的应用的icon,不是返回箭头
		actionBar.setHomeButtonEnabled(false);// 返回是否起作用
		actionBar.setDisplayHomeAsUpEnabled(false);// 返回是否起作用

		storeFunctionNames = getResources().getStringArray(
				R.array.main_function_diy);

		initView();
		setListener();
		// 第一次登陆始终检查更新
		if (StoreSession.getSuggestUpdate()) {
			UpgradeProgress upgradeProgress = UpgradeProgress.getInstance();
			//upgradeProgress.checkVersion(MainActivity.this, true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setIconEnable(menu, true);
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent	intent = null;
		switch (item.getItemId()) {
		case R.id.action_bar_menu_item_info:
			ToastHelper.getInstance().showLongMsg("我的消息");
			break;
		case R.id.action_bar_menu_item_person_info:
			intent=new Intent(MainActivity.this,PersonalInfoActivity.class);
			break;
		case R.id.action_bar_menu_item_setting:
			intent=new Intent(MainActivity.this,StoreSettingActivity.class);
			break;
		default:
			break;
		}
		if (intent!=null) {
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		viewUpdateCallback = new UpdateView(MainActivity.this);
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		loadingView2 = (LoadingView) findViewById(R.id.common_id_ll_loading2);
		// storeFunctionNames = new ArrayList<String>();
		listView = (ListView) findViewById(R.id.common_id_lv);

		storeController = new StoreController();
		adapter = new StoreFunctionAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				boolean isStoreSelect= storeFunctionSelector[position];
				Class<?> clickClass = storeFunctionClazz[position];
				Intent intent = new Intent();
				if (isStoreSelect) {
					intent.setClass(MainActivity.this, StoreSelectActivity.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("whichActivity", clickClass);
					intent.putExtras(bundle);
				}else{
				/*	if (clickClass==IrepStudyActivity.class) {
						if (PhoneStateUtil.hasInternet()) {
							mPopupWindow_irsp = null;
							mPopupWindow_irsp = new PopupWindowIREPLogin(MainActivity.this, storeController,
									viewUpdateCallback);
							mPopupWindow_irsp.showAtLocation(
									MainActivity.this.findViewById(R.id.main), Gravity.BOTTOM
									| Gravity.CENTER_HORIZONTAL, 0, 0); 
						} else {
							ToastHelper.getInstance().showLongMsg(getString(R.string.comm_txt_no_network));
						}
						return ;
					}*/
					intent.setClass(MainActivity.this, clickClass);
				}
				startActivity(intent);
				
			}
		});

	}

	class ViewHolder {
		public ImageView img;
		public TextView name;
		public TextView time;
	}

	private class StoreFunctionAdapter extends BaseAdapter {
		// private AsyncImageLoader asyncImageLoader = null;

		@Override
		public int getCount() {
			return storeFunctionNames.length;
		}

		@Override
		public Object getItem(int position) {
			return storeFunctionNames[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(MainActivity.this).inflate(
						R.layout.store_function_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView) convertView
						.findViewById(R.id.iv_store_function_icon);
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.txt_store_function_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.img.setBackgroundResource(storeFunctionIcon[position]);
			;
			viewHolder.name.setText(storeFunctionNames[position]);
			return convertView;
		}

	}

	private void setListener() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.txt_leave_pushmore),
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				ThreadUtils.exitProcess(getApplicationContext());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class UpdateView extends StoreCommonUpdateView<Boolean> {

		public UpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView2.showLoading();
		}

		@Override
		public void onPostExecute(Boolean result) {
			loadingView2.hideLoading();
			if (result == true) {
				if (mPopupWindow_irsp != null) {
					mPopupWindow_irsp.dismiss();
				}
				toIrepStudyActivity();
			}
		}

		@Override
		public void handleException(IException ex) {
			viewHelper.showErrorDialog(ex);
			loadingView2.hideLoading();
			// if failed,un-show the popwindow
			/*
			 * if (mPopupWindow_irsp!=null) { mPopupWindow_irsp.dismiss(); }
			 */
		}

	}

	/** ==================end UPGRADE===================== */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PERSONAL_INFO_REQUEST_ID) {
			if (resultCode == LOG_OUT_RESULT_CODE) {
				this.finish();
			}
		}
		if (requestCode == SCAN_RESULT_CODE) {
			if (resultCode == RESULT_OK) {
				ViewHelper helper = new ViewHelper(MainActivity.this);
				Bundle bundle = data.getExtras();
				String resultString = bundle.getString("result");
				helper.showBarcodeDialog(resultString);
			}
		}
	}
	private void toIrepStudyActivity() {
		Intent intent = new Intent(MainActivity.this, IrepStudyActivity.class);
		startActivity(intent);
	}
}

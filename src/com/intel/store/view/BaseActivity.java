package com.intel.store.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.intel.store.StoreApplication;
import com.intel.store.util.TypefaceCache;
import com.pactera.framework.util.Loger;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends ActionBarActivity {

	private String pageName = "";
	protected ActionBar actionBar;
	protected Context mContext;

	protected final boolean isOldDevice = VERSION.SDK_INT <= 10 ? true : false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		int titleId = Resources.getSystem().getIdentifier("action_bar_title",
				"id", "android");
		TextView title = (TextView) findViewById(titleId);
		TypefaceCache.changeFonts(title, mContext);
		
		if (!isOldDevice)
			getOverflowMenu();
		Class<?> clazz = getClass();
		pageName = clazz.getSimpleName();
		Loger.d("pageName:" + pageName);
		((StoreApplication) StoreApplication.getApp()).addActivity(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		// initView();
		// setListener();
		// View view = this.findViewById(android.R.id.content).getRootView();
		// TypefaceCache.changeFonts(view,mContext);
	}

	// protected void initView() {
	//
	// }
	//
	// protected void setListener() {
	//
	// }
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(pageName);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(pageName);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// enable为true时，菜单添加图标有效，enable为false时无效。4.0系统默认无效
	protected void setIconEnable(Menu menu, boolean enable) {
		try {
			Class<?> clazz = Class
					.forName("com.android.internal.view.menu.MenuBuilder");
			Method m = clazz.getDeclaredMethod("setOptionalIconsVisible",
					boolean.class);
			m.setAccessible(true);
			// MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
			m.invoke(menu, enable);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

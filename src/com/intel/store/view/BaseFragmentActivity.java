package com.intel.store.view;

import com.pactera.framework.util.Loger;
import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseFragmentActivity extends FragmentActivity {
	
	private String pageName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Class<?> clazz = getClass();
		pageName = clazz.getSimpleName();
		Loger.d("pageName:" + pageName);
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
}

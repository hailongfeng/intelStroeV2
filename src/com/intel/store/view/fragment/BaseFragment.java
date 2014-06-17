package com.intel.store.view.fragment;

import android.support.v4.app.Fragment;
import com.pactera.framework.util.Loger;
import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment {
	private String pageName="";
	public void onResume() {
		super.onResume();
		Class<?> clazz = getClass();
		pageName=clazz.getSimpleName();
		Loger.d("pageName:"+pageName);
		MobclickAgent.onPageStart(pageName);
	}

	public void onPause() {
		super.onPause();
		// 保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPageEnd(pageName);
	}

}

package com.intel.store.view.setting;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.util.StoreSession;
import com.intel.store.view.BaseActivity;
import com.intel.store.widget.SwitchButton;
import com.intel.store.widget.SwitchButton.OnCheckedChangeListener;
import com.pactera.framework.imgload.BitmapCache;
import com.pactera.framework.util.ToastHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class StoreSettingActivity extends BaseActivity implements OnClickListener {
	LinearLayout mlinear_clear_crash_select,mlinear_about_app_select;
	SwitchButton msb_auto_login,msb_gps_locate;
	private OnCheckedChangeListener listenerLogin,listenerGPS;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
		setListener();
	}
	private void initView(){
		mlinear_clear_crash_select=(LinearLayout) findViewById(R.id.linear_clear_crash_select);
		mlinear_about_app_select=(LinearLayout) findViewById(R.id.linear_about_app_select);
		msb_auto_login=(SwitchButton) findViewById(R.id.sb_auto_login);
		msb_auto_login.setChecked(StoreSession.getUserLoginAuto());
		msb_gps_locate=(SwitchButton) findViewById(R.id.sb_gps_locate);
		msb_gps_locate.setChecked(StoreSession.getGPSstatu());
	}
	private void setListener(){
		mlinear_clear_crash_select.setOnClickListener(this);
		mlinear_about_app_select.setOnClickListener(this);
		listenerLogin=new OnCheckedChangeListener() {
			
			@Override
			public void checkedChange(boolean isChecked) {
				StoreSession.setUserLoginAuto(isChecked);
			}
		};
		listenerGPS=new OnCheckedChangeListener() {
			
			@Override
			public void checkedChange(boolean isChecked) {
				StoreSession.setGPSstatu(isChecked);
			}
		};
		msb_auto_login.setOnCheckedChangeListener(listenerLogin);
		msb_gps_locate.setOnCheckedChangeListener(listenerGPS);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linear_clear_crash_select:
			cleanAppCash();
			break;
		case R.id.linear_about_app_select:
			aboutApp();
			break;

		default:
			break;
		}
	}
	
	private synchronized void cleanAppCash() {
		StoreApplication.asyncImageLoaderRecle();
		ToastHelper.getInstance().showLongMsg(getString(R.string.personal_txt_clear_crash_finshed));
		BitmapCache.clearSdCache();
	}

	private void aboutApp() {
		Intent intent =new Intent(StoreSettingActivity.this,SettingAboutActivity.class);
		startActivity(intent);
	}
}

package com.intel.store.view;

import java.io.Serializable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.ClerkController;
import com.intel.store.model.ClerkModel;
import com.intel.store.util.LocationManager;
import com.intel.store.util.StoreSession;
import com.intel.store.util.UpgradeProgress;
import com.intel.store.util.ViewHelper;
import com.intel.store.view.webview.PrivacyPolicyActivity;
import com.pactera.framework.exception.IException;
import com.pactera.framework.imgload.BitmapCache;
import com.pactera.framework.location.LocationHelper.OnGetBaiDuCityListener;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.ThreadUtils;
import com.pactera.framework.util.ToastHelper;

/**
 * @author P0033759
 * 最后修改时间	2014-3-17-下午4:38:09
 * 功能	TODO
 */
public class PersonalInfoActivity extends BaseActivity implements
		OnClickListener {
	private static final int RequestCodeEdit = 20;
	private LinearLayout mlinear_logout_select;
	private LinearLayout mlinear_password_change_select;
	private LinearLayout mlinear_clean_select;
	private LinearLayout mlinear_update_select;
	private LinearLayout mlinear_privacy_policy;
	private LinearLayout mlinear_edit_personindfo;
	private TextView mtxt_username;
	private TextView mtxt_role_name;
	private TextView mtxt_phonenum;
	private TextView mtxt_email;
	private LinearLayout mll_mylocal;
	private TextView mtxt_userlocal;
	private ClerkController clerkController;
	private Button mbtn_back;
	private ViewHelper mViewHelper;
	private CheckBox mCb_GPS;
	
	//代码更新
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);
		initView();
		setListener();
	}

	private void initView() {
		mViewHelper = new ViewHelper(this);
		//个人资料显示
		mtxt_username = (TextView) findViewById(R.id.txt_username);
		mtxt_role_name = (TextView) findViewById(R.id.txt_clerk_position);
		mtxt_email = (TextView) findViewById(R.id.txt_clerk_email);
		mtxt_phonenum = (TextView) findViewById(R.id.txt_clerk_phone_number);
		
		
		Loger.d("getname"+StoreSession.getName());
		if (StoreSession.getName()!=null) {
			mtxt_username.setText(StoreSession.getName());
		} else {
			mtxt_username.setText(StoreApplication.getAppContext().getString(R.string.comm_txt_abnormal_data));
		}
		Loger.d("getname"+StoreSession.getRoleName());
		if (StoreSession.getName()!=null) {
			mtxt_role_name.setText(StoreSession.getRoleName());
		} else {
			mtxt_role_name.setText(StoreApplication.getAppContext().getString(R.string.comm_txt_abnormal_data));
		}
		if (StoreSession.getName()!=null) {
			mtxt_email.setText(StoreSession.getEmail());
		} else {
			mtxt_email.setText(StoreApplication.getAppContext().getString(R.string.comm_txt_abnormal_data));
		}
		if (StoreSession.getName()!=null) {
			mtxt_phonenum.setText(StoreSession.getTelephone());
		} else {
			mtxt_phonenum.setText(StoreApplication.getAppContext().getString(R.string.comm_txt_abnormal_data));
		}
		mbtn_back = (Button) findViewById(R.id.btn_back);
		mlinear_logout_select = (LinearLayout) findViewById(R.id.linear_logout_select);
		mlinear_password_change_select = (LinearLayout) findViewById(R.id.linear_password_change_select);
		mlinear_clean_select=(LinearLayout) findViewById(R.id.linear_clean_select);
		mlinear_update_select=(LinearLayout) findViewById(R.id.linear_update_select);
		mlinear_privacy_policy=(LinearLayout) findViewById(R.id.linear_privacy_policy);
		mlinear_edit_personindfo=(LinearLayout) findViewById(R.id.linear_edit_personindfo);
		mCb_GPS=(CheckBox) findViewById(R.id.cb_gps);
		if (StoreSession.getGPSstatu()==true) {
			mCb_GPS.setChecked(true);
			startLocation();
			
		} else {
			mCb_GPS.setChecked(false);
		}
		mtxt_userlocal= (TextView) findViewById(R.id.txt_userlocal);
		mll_mylocal= (LinearLayout) findViewById(R.id.ll_mylocal);
		updateLocView();
	}

	public void updateLocView() {
			if (StoreSession.getGPSstatu()&&StoreSession.getLocateOkId()=="true") {
				
				mtxt_userlocal.setVisibility(View.VISIBLE);
				mll_mylocal.setVisibility(View.VISIBLE);
			
				if (mtxt_userlocal!=null) {
					mtxt_userlocal.setText(StoreSession.getCurrentAddress());
				}
			}
			else {
				mtxt_userlocal.setVisibility(View.GONE);
				mll_mylocal.setVisibility(View.GONE);
			}
	}

	private void setListener() {
		mlinear_logout_select.setOnClickListener(this);
		mlinear_password_change_select.setOnClickListener(this);
		mlinear_clean_select.setOnClickListener(this);
		mlinear_update_select.setOnClickListener(this);
		mlinear_privacy_policy.setOnClickListener(this);
		mlinear_edit_personindfo.setOnClickListener(this);
		mbtn_back.setOnClickListener(this);
		mCb_GPS.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					startLocation();
				} else {
					LocationManager.getInstance().stopLocation();
					StoreSession.setGPSstatu(false);
					ToastHelper.getInstance().showShortMsg(getString(R.string.personal_txt_gps_closed));
				}
				updateLocView();
			
			
			}
			
		});
	}

	protected void startLocation() {
		LocationManager.getInstance().stopLocation();
		LocationManager.getInstance().locationOnce(new OnGetBaiDuCityListener() {
			
			@Override
			public void onGetBaiDuCityOk(String sLatitude, String sLongitude,
					String province, String city, String district, String street,
					String streetNumber, String addrStr) {
				updateLocView();
			}
			
			@Override
			public void onGetBaiDuCityErr() {
				
			}
		});
		StoreSession.setGPSstatu(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.linear_edit_personindfo:
			Intent infoChangeIntent = new Intent();
			infoChangeIntent.setClass(PersonalInfoActivity.this,
					StoreMyClerkEditActivity.class);
			MapEntity clerkInfo=new MapEntity();
			clerkInfo.setValue(ClerkModel.REP_NM, StoreSession.getName());
			clerkInfo.setValue(ClerkModel.REP_TEL, StoreSession.getTelephone());
			clerkInfo.setValue(ClerkModel.REP_EMAIL, StoreSession.getEmail());
			clerkInfo.setValue(ClerkModel.REP_ID, StoreSession.getRepID());
			clerkInfo.setValue(ClerkModel.REP_ASGN_ROLE_NM, StoreSession.getRoleName());
			infoChangeIntent.putExtra("clerkInfo", (Serializable)clerkInfo);
			startActivityForResult(infoChangeIntent, RequestCodeEdit);
			break;
		case R.id.linear_password_change_select:
			Intent passwordChangeIntent = new Intent();
			passwordChangeIntent.setClass(PersonalInfoActivity.this,
					PasswordChangeActivity.class);
			startActivity(passwordChangeIntent);
			break;
		case R.id.linear_logout_select:
			logout();
			break;
		case R.id.linear_clean_select:
			cleanAppCash();
			StoreApplication.asyncImageLoaderRecle();
			ToastHelper.getInstance().showLongMsg(getString(R.string.personal_txt_clear_crash_finshed));
			break;
		case R.id.linear_update_select:
			// 标记手动点击更新的
			UpgradeProgress.getInstance().checkVersion(PersonalInfoActivity.this,false);
			break;
		case R.id.linear_privacy_policy:
			Intent intent=new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
			startActivity(intent);
		break;
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
	}

	private synchronized void cleanAppCash() {
		BitmapCache.clearSdCache();
	}

	private synchronized void logout() {
		String title = getString(R.string.personal_txt_title);
		String msg =  getString(R.string.personal_txt_log_out_ask);
		String btn1_content = getString(R.string.comm_ok);
		String btn2_content =  getString(R.string.comm_back);
		android.content.DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				clerkController = new ClerkController();
				clerkController.logout(new LogoutUpdateView(PersonalInfoActivity.this));
			}
		};
		android.content.DialogInterface.OnClickListener negativeListener = null;
		OnCancelListener onCancelListener = null;
		mViewHelper.showBTN2Dialog(title, msg, btn1_content, btn2_content,
				positiveListener, negativeListener, onCancelListener);
	
	}

	class LogoutUpdateView extends StoreCommonUpdateView<Boolean> {
		public LogoutUpdateView(Context context) {
			super(context);
		}

		private ProgressDialog mpDialog;

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			mpDialog = new ProgressDialog(PersonalInfoActivity.this);
			mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mpDialog.setMessage(getString(R.string.personal_txt_log_outting));
			mpDialog.setIndeterminate(false);
			mpDialog.setCancelable(true);
			mpDialog.show();
		}


		@Override
		public void onPostExecute(Boolean arg0) {
			mpDialog.dismiss();
			ThreadUtils.exitProcess(getApplicationContext());
		}

		@Override
		public void handleException(IException ex) {
			mViewHelper.showErrorDialog(ex);
		
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode==RequestCodeEdit) {
				if (resultCode==RESULT_OK) {
				MapEntity mclerkInfo=(MapEntity) data.getSerializableExtra("clerkInfo");
				StoreSession.setEmail(mclerkInfo.getString(ClerkModel.REP_EMAIL));
				StoreSession.setTelephone(mclerkInfo.getString(ClerkModel.REP_TEL));
				initView();
				}
			}
		super.onActivityResult(requestCode, resultCode,data);
	}
}

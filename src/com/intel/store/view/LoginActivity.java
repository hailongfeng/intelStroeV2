package com.intel.store.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.intel.store.R;
import com.intel.store.controller.ClerkController;
import com.intel.store.util.Constants;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ViewHelper;
import com.intel.store.widget.DialogHelper;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.ToastHelper;
import com.pactera.framework.util.Version;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends BaseActivity {

	private EditText edt_login_username, edt_login_password;
	private CheckBox check_remember_passowrd, check_automatic_login;
	private Button btn_login;
	private ClerkController clerkController;
	public static final int LINE_DX = 1;
	public static final int LINE_WT = 2;
	private int whichLine = LINE_DX;
	private LoadingView loadingView;
	
	final String title="使用条款的说明";
	final String content="英特尔® MR是英特尔零售部门开发的面向合作伙伴的管理沟通工具。" +
			"英特尔零售部门将通过此工具定期向合作伙伴更新英特尔产品信息，培训、促销活动，新闻或者销售话术等，" +
			"并提供各种参与 零售前端活动的功能，具体内容如下：\n\n"+
			"加入英特尔店面合作计划的伙伴，将跟英特尔相互合作， 需按照跟英特尔的相应合作计划要求落实好在店面的英特尔相关物料、" +
			"产品及演示的呈现， 积极参加相关培训，积极推进英特尔架构产品的销售，并且通过APP上传相应的销售数据、出样、物料照片、" +
			"演示照片，等等信息 ,包括上传含有GPS定位信息的数据。相关数据将且仅用于店面合作业绩的评定。\n\n"+
			"在此工具中，店面可以上传店面销售人员手机号信息，英特尔将通过这些手机信息给店员 开通APP的使用权限， " +
			"这样相关店员也将可以登录使用这个APP。此工具数据库中的手机号码将对外保密，并严格在此范围内使用。\n\n"+
			"英特尔® MR直接跟IREP系统相连，在此您可以方便快捷地直接访问IREP(英特尔零售先锋营)。" +
			"在输入用户名和密码后即可在APP上参与IREP最新的课程学习，了解最新英特尔产品动向，提升自我的产品技术水平。";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar.hide();
		setContentView(R.layout.activity_login);
		initView();
		MobclickAgent.onEvent(LoginActivity.this, "store_login_envet");
		setListener();
		if (isFirst()) {
			showDialog();
		}
		if (StoreSession.getUserLoginAuto()) {
			login();
		}
	}

	boolean isFirst(){
		SharedPreferences setting = getSharedPreferences("share"+Version.getVersionName(), 0);
        Boolean user_first = setting.getBoolean("FIRST",true);
        //第一次
        if(user_first){
        	return true;
        }else{
        	return false;
        }
	}
	
	void showDialog(){
		View.OnClickListener positiveListener=new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences setting = getSharedPreferences("share"+Version.getVersionName(), 0);
				setting.edit().putBoolean("FIRST", false).commit();
				setting.edit().clear();
			}
		};
		View.OnClickListener negativeListener=new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		};
		DialogHelper.showDialog(LoginActivity.this, title, content, "同意并使用", positiveListener, "拒绝并退出", negativeListener);
	
	}

	private void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);

		edt_login_username = (EditText) findViewById(R.id.edt_login_username);
		edt_login_password = (EditText) findViewById(R.id.edt_login_password);
		btn_login = (Button) findViewById(R.id.btn_login_ok);
		check_automatic_login = (CheckBox) findViewById(R.id.chk_login_automatic_login);
		check_remember_passowrd = (CheckBox) findViewById(R.id.chk_login_remember_pwd);


		if (StoreSession.getRemenberAccount()) {
			check_remember_passowrd.setChecked(true);
			edt_login_username.setText(StoreSession.getAccount());
			edt_login_password.setText(StoreSession.getPassword());
		} else {
			check_remember_passowrd.setChecked(false);
			edt_login_username.setText("");
			edt_login_password.setText("");
		}
		if (StoreSession.getUserLoginAuto()) {
			check_automatic_login.setChecked(true);
		} else {
			check_automatic_login.setChecked(false);
		}

	}

	private void setListener() {
		check_remember_passowrd
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (!isChecked) {
							check_automatic_login.setChecked(false);
						}
					}
				});

		check_automatic_login
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							check_automatic_login.setChecked(true);
							check_remember_passowrd.setChecked(true);
						}
					}
				});

		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (validateInput())
//				Intent intent=new Intent();
//				intent.setClass(LoginActivity.this, com.example.storeother.MainActivity.class);
//				startActivity(intent);
				login();
			}

		});

	}

	private void login() {
		Loger.i("whichLine:" + whichLine + "");
		if (whichLine == LINE_DX)
			StoreSession.setLine(Constants.LINE_DX);
		else
			StoreSession.setLine(Constants.LINE_WT);

		String userName = edt_login_username.getText().toString();
		String password = edt_login_password.getText().toString();
		String userType = "1";

		if (validateInput()) {
				clerkController = new ClerkController();
				clerkController.loginFromRemote(new LoginUpdateView(LoginActivity.this),
						userName, password, userType);
		}
	}

	private boolean validateInput() {
		String userName = edt_login_username.getText().toString();
		String password = edt_login_password.getText().toString();

		if (TextUtils.isEmpty(userName)) {
			edt_login_username.requestFocus();
			ToastHelper.getInstance().showLongMsg(
					getString(R.string.login_account_not_null));
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			edt_login_password.requestFocus();
			ToastHelper.getInstance().showLongMsg(
					getString(R.string.login_password_not_null));
			return false;
		}
		return true;
	}

	private class LoginUpdateView extends StoreCommonUpdateView<Boolean> {

		public LoginUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(Boolean result) {

			if (result) {
				if (check_remember_passowrd.isChecked()) {
					StoreSession.setRememberAccount(true);
				} else {
					StoreSession.setRememberAccount(false);
				}
				if (check_automatic_login.isChecked()) {
					StoreSession.setUserLoginAuto(true);
				} else {
					StoreSession.setUserLoginAuto(false);
				}

				Intent mainPageIntent = new Intent(LoginActivity.this,
						MainActivity.class);
				btn_login.setEnabled(false);
				startActivity(mainPageIntent);
				finish();
			} else {
				loadingView.hideLoading();
				viewHelper.showBTNDialog(getString(R.string.txt_login_failed));
			}
		}
		
		@Override
		public void handleException(IException ex) {
			viewHelper.showErrorDialog(ex);
			loadingView.hideLoading();
		}
	}
}

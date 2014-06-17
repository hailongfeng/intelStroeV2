package com.intel.store.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.intel.store.util.StoreSession;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.util.InputChecker;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.ToastHelper;

public class PasswordChangeActivity extends BaseActivity implements
		OnClickListener {
	// 输入的旧密码
	String oldPassword;
	// 输入的新密码
	String newPassword;
	// 重复输入的新密码
	String newPasswordRepeat;
	EditText edt_oldpassword;
	EditText edt_newpassword;
	EditText edt_newpassword_repeat;
	Button btn_submit;
	Button btn_back;
	StoreController storeController;
	private LoadingView loadingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_change);
		initView();
		setListener();
		storeController = new StoreController();
	}

	private void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);

		edt_oldpassword = (EditText) findViewById(R.id.edt_oldpassword);
		edt_newpassword = (EditText) findViewById(R.id.edt_newpassword);
		edt_newpassword_repeat = (EditText) findViewById(R.id.edt_newpassword_repeat);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_back = (Button) findViewById(R.id.btn_back);
	}

	private void setListener() {
		btn_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
	}

	private Boolean validateInput() {

		oldPassword = edt_oldpassword.getText().toString();
		newPassword = edt_newpassword.getText().toString();
		newPasswordRepeat = edt_newpassword_repeat.getText().toString();

		if (InputChecker.isEmpty((oldPassword))) {
			ToastHelper.getInstance().showLongMsg(getString(R.string.password_old_null));
			return false;
		}

		if (!oldPassword.equals(StoreSession.getPassword())) {
			ToastHelper.getInstance().showLongMsg(getString(R.string.password_old_error));
			return false;
		}

		if (InputChecker.isEmpty((newPassword))) {
			ToastHelper.getInstance().showLongMsg(getString(R.string.password_new_null));
			return false;
		}
		if (InputChecker.isEmpty((newPasswordRepeat))) {
			ToastHelper.getInstance().showLongMsg(getString(R.string.password_repeat_null));
			return false;
		}
		if (!newPassword.equals(newPasswordRepeat)) {
			ToastHelper.getInstance().showLongMsg(getString(R.string.password_not_same));
			return false;
		}
		
		if(oldPassword.equals(newPassword)){
			ToastHelper.getInstance().showLongMsg(getString(R.string.password_same));
			return false;
		}
		

		return true;
	}

	class PasswordChangeUpdateView extends
			StoreCommonUpdateView<Boolean> {

		public PasswordChangeUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}


		@Override
		public void onPostExecute(Boolean arg0) {
			Loger.d("onPostExecute");
			loadingView.hideLoading();
			if (arg0) {
				ToastHelper.getInstance().showLongMsg(getString(R.string.password_changed));
				Intent intent=new Intent(getApplicationContext(), PersonalInfoActivity.class);
				startActivity(intent);
				finish();
			} else {
				ToastHelper.getInstance().showLongMsg(getString(R.string.password_changed_error));
			}
		}

		@Override
		public void handleException(IException ex) {
			Loger.d("onException");
			loadingView.hideLoading();
			if (ex.getMessage()!=null&&!"".equals(ex.getMessage().trim())){
				ToastHelper.getInstance().showLongMsg(ex.getMessage());
			}
			else{
				ToastHelper.getInstance().showLongMsg(getString(R.string.password_changed_error));
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_submit:

			if (validateInput()) {
				storeController.changePasswordFromRemote(
						new PasswordChangeUpdateView(PasswordChangeActivity.this),
						StoreSession.getAccount(), oldPassword, newPassword);
			}
			break;
		default:
			break;
		}
	}
}

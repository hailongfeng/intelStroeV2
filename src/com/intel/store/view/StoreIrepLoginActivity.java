package com.intel.store.view;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.intel.store.util.InputChecker;
import com.intel.store.view.webview.IrepStudyActivity;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.util.ToastHelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StoreIrepLoginActivity extends BaseActivity implements OnClickListener{
	private EditText edt_login_username,edt_login_password;
	private Button btn_login_ok,btn_register;
	private StoreController storeController;
	private UpdateView viewUpdateCallback;
	private LoadingView loadingView; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar.setDisplayShowHomeEnabled(true);// 返回的应用的icon,不是返回箭头
		setContentView(R.layout.activity_store_irep_login);
		initView();
		setListener();
	}
	private void initView(){
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		edt_login_username=(EditText) findViewById(R.id.edt_login_username);
		edt_login_password=(EditText) findViewById(R.id.edt_login_password);
		btn_login_ok=(Button) findViewById(R.id.btn_login_ok);
		btn_register=(Button) findViewById(R.id.btn_register);
	}
	private void setListener() {
		btn_login_ok.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		String[] strs;
		switch (v.getId()) {
		case R.id.btn_login_ok:
			if ((strs=inputChecked())!=null) {
				irepLogin(strs[0],strs[1]);
			}
			break;
		case R.id.btn_register:
				irepRegister();
			break;

		default:
			break;
		}
	}
	private String[] inputChecked() {
		String[] strings=new String[2];
		if (InputChecker.isEmpty(edt_login_username.getText().toString())) {
			ToastHelper.getInstance().showLongMsg(
					this.getString(R.string.login_account_not_null));
			return null;
		}
		if (InputChecker.isEmpty(edt_login_password.getText().toString())) {
			ToastHelper.getInstance().showLongMsg(
					this.getString(R.string.login_password_not_null));
			return null;
		}
		strings[0]=edt_login_username.getText().toString();
		strings[1]=edt_login_password.getText().toString();
		return strings;
	}
	
	private void irepRegister() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse("http://retailedgecn.intel.com/50/registration");
		intent.setData(content_url);
		this.startActivity(intent);
	
	}
	private void irepLogin(String userName,String password) {
		if (storeController==null) {
			storeController=new StoreController();
		}if(viewUpdateCallback==null){
			viewUpdateCallback=new UpdateView(this);
		}
		storeController.irepLogin(viewUpdateCallback, userName,
				password);
	}
	public class UpdateView extends StoreCommonUpdateView<Boolean> {

		public UpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			loadingView.showLoading();
			super.onPreExecute();
		}

		@Override
		public void onPostExecute(Boolean result) {
			loadingView.hideLoading();
			if (result == true) {
				Intent intent = new Intent(StoreIrepLoginActivity.this, IrepStudyActivity.class);
				startActivity(intent);
			}
		}

		@Override
		public void handleException(IException ex) {
			loadingView.hideLoading();
			viewHelper.showErrorDialog(ex);
		}

	}

}

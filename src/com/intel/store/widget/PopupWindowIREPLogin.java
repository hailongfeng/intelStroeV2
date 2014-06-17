package com.intel.store.widget;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.intel.store.view.MainActivity;
import com.intel.store.view.MainActivity.UpdateView;
import com.pactera.framework.util.ToastHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

@SuppressLint("ViewConstructor")
public class PopupWindowIREPLogin extends PopupWindow {

	private View mPopView;
	private Button btnRegister;
	private Button btnLogin;
	private EditText edtUsername;
	private EditText edtPassword;
	//private TextView mTitle;
	private Activity mActivity;
	private String userName;
	private String password;

	public PopupWindowIREPLogin(Activity context) {
		super(context);

		init(context);
	}

	public PopupWindowIREPLogin(MainActivity mainActivity,
			final StoreController storeController,
			final UpdateView viewUpdateCallback) {
		super(mainActivity);
		init(mainActivity);
		btnLogin = (Button) mPopView.findViewById(R.id.btn_login);
		btnRegister = (Button) mPopView.findViewById(R.id.btn_register);
		edtUsername = (EditText) mPopView.findViewById(R.id.edt_login_username);
		edtPassword = (EditText) mPopView.findViewById(R.id.edt_login_password);
		//mTitle = (TextView) mPopView.findViewById(R.id.title);
		// TODO Auto-generated constructor stub
		// edtUsername.setText("prc@testuser.com");
		 //edtPassword.setText("100%intel");
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateInput()) {
					storeController.irepLogin(viewUpdateCallback, userName,
							password);
				}
			}
		});
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri
						//.parse("https://retailedgecn.intel.com");
						//.parse("https://retailedge.intel.com/ww/registration.aspx");
				.parse("http://retailedgecn.intel.com/50/registration");
				intent.setData(content_url);
				mActivity.startActivity(intent);
			}
		});
	}

	private void init(Activity context) {
		mActivity = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopView = inflater.inflate(R.layout.alert_login_dialog, null);
		this.setContentView(mPopView);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		this.setAnimationStyle(R.style.PopupAnimation);
		ColorDrawable dw = new ColorDrawable();
		this.setBackgroundDrawable(dw);
		mPopView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mPopView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	private boolean validateInput() {
		userName = edtUsername.getText().toString();
		password = edtPassword.getText().toString();

		if (TextUtils.isEmpty(userName)) {
			edtUsername.requestFocus();
			ToastHelper.getInstance().showLongMsg(
					mActivity.getString(R.string.login_account_not_null));
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			edtPassword.requestFocus();
			ToastHelper.getInstance().showLongMsg(
					mActivity.getString(R.string.login_password_not_null));
			return false;
		}
		return true;
	}
}

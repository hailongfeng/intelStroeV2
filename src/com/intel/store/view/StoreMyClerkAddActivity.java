package com.intel.store.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.controller.ClerkController;
import com.intel.store.model.ClerkModel;
import com.intel.store.util.CheckResponse;
import com.intel.store.util.InputChecker;
import com.intel.store.util.MyActivityInterface;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ViewHelper;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.ToastHelper;

public class StoreMyClerkAddActivity extends BaseActivity implements
		MyActivityInterface, OnClickListener {

	private Button mbtn_back;
	private Button mbtn_submit;
	private EditText edt_clerk_name;
	private TextView tv_clerk_position;
	private EditText edt_clerk_phone;
	private EditText edt_clerk_email_address;
	ViewHelper mViewHelper;
	private ClerkController clerkController;
	private MapEntity clerkEntity = new MapEntity();
	private LoadingView loadingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_my_clerk_add);
		initView();
		setListener();
	}

	@Override
	public void initView() {
		mbtn_back = (Button) findViewById(R.id.btn_back);
		mbtn_submit = (Button) findViewById(R.id.btn_submit);
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		edt_clerk_name = (EditText) findViewById(R.id.edt_clerk_name);
		tv_clerk_position = (TextView) findViewById(R.id.tv_clerk_position);
		edt_clerk_phone = (EditText) findViewById(R.id.edt_clerk_phone);
		edt_clerk_email_address = (EditText) findViewById(R.id.edt_clerk_email_address);
		if (mViewHelper == null) {
			mViewHelper = new ViewHelper(this);
		}
		//判断接受隐私条款
				if (!StoreSession.getAcceptPrivate()) {
					android.content.DialogInterface.OnClickListener nagetive=new 	android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (!StoreSession.getAcceptPrivate()) {
								StoreMyClerkAddActivity.this.finish();
							}
						}
					};
					OnCancelListener cancel=new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							if (!StoreSession.getAcceptPrivate()) {
								StoreMyClerkAddActivity.this.finish();
							}
						}
					};
					mViewHelper.showBTN2Dialog(getString(R.string.txt_private_define), getString(R.string.txt_private_content), getString(R.string.txt_accept),getString(R.string.txt_refuse),
							new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							StoreSession.setAcceptPrivate(true);
						}
					}, 
					nagetive, cancel);
					
				}
			
	}

	@Override
	public void setListener() {
		mbtn_back.setOnClickListener(this);
		mbtn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_submit:
			submitMyclerk();
			break;
		default:
			break;
		}
	}

	private void submitMyclerk() {
		if (clerkController == null) {
			clerkController = new ClerkController();
		}
		if (clerkEntity == null) {
			clerkEntity = new MapEntity();
		}
		clerkEntity = createClerk();
		
		CheckResponse checkResponse=InputChecker.isRep(clerkEntity);
		
		if (checkResponse.getResBoolean()) {
			mViewHelper.showBTNDialog(checkResponse.getResString().toString(), getString(R.string.comm_ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clerkController.addClerk(
									new AddClerkUpdateViewAsyncCallback(StoreMyClerkAddActivity.this),
									clerkEntity);

						}
					}, null);

		} else {
			mViewHelper.showBTNDialog(checkResponse.getResString().toString(), getString(R.string.clerk_txt_error));
		}
	}

	private MapEntity createClerk() {
		clerkEntity
				.setValue(ClerkModel.CREATER_REP_ID, StoreSession.getRepID());
		clerkEntity.setValue(ClerkModel.REP_NM, edt_clerk_name.getText()
				.toString());
		clerkEntity.setValue(ClerkModel.REP_TEL, edt_clerk_phone.getText()
				.toString());
		clerkEntity.setValue(ClerkModel.REP_EMAIL, edt_clerk_email_address
				.getText().toString());
		clerkEntity.setValue(ClerkModel.Store_ID,
				StoreSession.getCurrentStoreId());
		clerkEntity.setValue(ClerkModel.REP_ASGN_ROLE_ID, tv_clerk_position
				.getText().toString());
		return clerkEntity;
	}


	private void cleanData() {
		edt_clerk_name.setText("");
		edt_clerk_phone.setText("");
		edt_clerk_email_address.setText("");
	}

	class AddClerkUpdateViewAsyncCallback extends StoreCommonUpdateView<String>{

		public AddClerkUpdateViewAsyncCallback(Context context) {
			super(context);
		}

		@Override
		public void onCancelled() {
			ToastHelper.getInstance().showShortMsg(getString(R.string.clerk_txt_add_give_up));
			loadingView.hideLoading();
		}

		@Override
		public void handleException(IException ex) {
			if (ex.getMessage()!=null) {
				ToastHelper.getInstance().showLongMsg(ex.getMessage() + "," + getString(R.string.clerk_txt_add_fail));
			}else {
				ToastHelper.getInstance().showLongMsg(getString(R.string.clerk_txt_add_fail));
			}
			loadingView.hideLoading();
		}

		@Override
		public void onPostExecute(String result) {
			mViewHelper.showBTNDialog(getString(R.string.clerk_txt_add_success));
			if (result.equalsIgnoreCase("true")) {
				initView();
				cleanData();
			}
			setResult(RESULT_OK);
			loadingView.hideLoading();
		
		}
		@Override
		public void onPreExecute() {
			loadingView.showLoading();
		}


	}

}

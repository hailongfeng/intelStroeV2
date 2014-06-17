package com.intel.store.view;

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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("CutPasteId")
public class StoreMyClerkEditActivity extends BaseActivity implements
		MyActivityInterface, OnClickListener {

	private Bundle mbundle;
	private MapEntity mclerkInfo;
	private Button mbtn_back;
	private Button mbtn_myclerk_delete;
	private Button mbtn_submit;

	private TextView mtv_clerk_name;
	private TextView mtv_clerk_position;
	private EditText medt_clerk_phone;
	private EditText medt_email_address;
	private ViewHelper mViewHelper;
	private ClerkController clerkController;
	private LoadingView loadingViewDele;
	private LoadingView loadingViewModfy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_my_clerk_edit);
		mbundle = getIntent().getExtras();
		if (mbundle != null) {
			mclerkInfo = (MapEntity) mbundle.getSerializable("clerkInfo");
		}
		initView();
		setListener();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setIconEnable(menu, true);
		getMenuInflater().inflate(R.menu.my_clerk_edit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar_menu_item_del:
			deleteMyclerk();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void initView() {
		mViewHelper = new ViewHelper(this);
		loadingViewDele = (LoadingView) findViewById(R.id.common_id_ll_loading);
		loadingViewModfy = (LoadingView) findViewById(R.id.common_id_ll_loading);
		mbtn_myclerk_delete = (Button) findViewById(R.id.btn_myclerk_delete);
		mbtn_submit = (Button) findViewById(R.id.btn_submit);
		mbtn_back = (Button) findViewById(R.id.btn_back);
		mtv_clerk_name = (TextView) findViewById(R.id.edt_clerk_name);
		mtv_clerk_position = (TextView) findViewById(R.id.tv_clerk_position);
		medt_clerk_phone = (EditText) findViewById(R.id.edt_clerk_phone);
		medt_email_address = (EditText) findViewById(R.id.edt_clerk_email_address);
		if (mclerkInfo != null) {
			mtv_clerk_name.setText(mclerkInfo.getString(ClerkModel.REP_NM));
			medt_clerk_phone.setText(mclerkInfo.getString(ClerkModel.REP_TEL));
			medt_email_address.setText(mclerkInfo.getString(ClerkModel.REP_EMAIL));
			mtv_clerk_position.setText(mclerkInfo.getString(ClerkModel.REP_ASGN_ROLE_NM));
		}
	}

	@Override
	public void setListener() {
		mbtn_submit.setOnClickListener(this);
		mbtn_back.setOnClickListener(this);
		mbtn_myclerk_delete.setOnClickListener(this);

		loadingViewDele.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteMyclerk();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			StoreMyClerkEditActivity.this.finish();
			break;
		case R.id.btn_submit:
			modfyMyclerk();
			break;
		case R.id.btn_myclerk_delete:
			deleteMyclerk();
			break;
		default:
			break;
		}
	}

	private void deleteMyclerk() {
		mViewHelper.showBTN2Dialog(null,
				getString(R.string.clerk_txt_delete_ask),
				getString(R.string.btn_change), getString(R.string.btn_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (clerkController == null) {
							clerkController = new ClerkController();
						}

						mclerkInfo.setValue(ClerkModel.CREATER_REP_ID,
								StoreSession.getRepID());
						clerkController.delClerk(new DelMyclerkUpdateView(
								StoreMyClerkEditActivity.this), mclerkInfo);

					}
				}, null, null);
	}

	private void modfyMyclerk() {
		if (clerkController == null) {
			clerkController = new ClerkController();
		}
		mclerkInfo.setValue(ClerkModel.REP_TEL, medt_clerk_phone.getText()
				.toString());
		mclerkInfo.setValue(ClerkModel.REP_EMAIL, medt_email_address.getText()
				.toString());
		mclerkInfo.setValue(ClerkModel.Store_ID,
				StoreSession.getCurrentStoreId());
		mclerkInfo.setValue(ClerkModel.CREATER_REP_ID, StoreSession.getRepID());
		CheckResponse checkResponse = InputChecker.isRep(mclerkInfo);
		if (checkResponse.getResBoolean()) {
			mViewHelper.showBTNDialog(checkResponse.getResString().toString(),
					getString(R.string.btn_change), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							clerkController.modefyClerk(
									new EditMyclerkUpdateView(
											StoreMyClerkEditActivity.this),
									mclerkInfo);

						}
					}, null);

		} else {
			mViewHelper.showBTNDialog(checkResponse.getResString().toString(),
					getString(R.string.clerk_txt_error));
		}

	}

	private class EditMyclerkUpdateView extends StoreCommonUpdateView<Boolean> {

		public EditMyclerkUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onCancelled() {
			loadingViewModfy.hideLoading();
		}

		@Override
		public void onPreExecute() {

			loadingViewModfy.showLoading();
		}

		@Override
		public void onPostExecute(Boolean arg0) {

			if (arg0) {
				ToastHelper.getInstance().showLongMsg(
						getString(R.string.clerk_txt_changed));
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("clerkInfo", mclerkInfo);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				StoreMyClerkEditActivity.this.finish();
			} else {
				ToastHelper.getInstance().showLongMsg(
						getString(R.string.clerk_txt_delete_fail));
			}

			loadingViewModfy.hideLoading();
		}

		@Override
		public void handleException(IException ex) {

			loadingViewModfy.hideLoading();
			viewHelper.showErrorDialog(ex);
		}
	}

	private class DelMyclerkUpdateView extends StoreCommonUpdateView<Boolean> {

		public DelMyclerkUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onCancelled() {
			loadingViewDele.hideLoading();
		}

		@Override
		public void onPreExecute() {
			loadingViewDele.showLoading();
		}

		@Override
		public void onPostExecute(Boolean arg0) {
			loadingViewDele.hideLoading();

			if (arg0) {
				ToastHelper.getInstance().showLongMsg(
						getString(R.string.clerk_txt_delete_success));
				setResult(RESULT_OK);
				finish();
			} else {
				ToastHelper.getInstance().showLongMsg(
						getString(R.string.clerk_txt_delete_fail));
			}
		}

		@Override
		public void handleException(IException ex) {
			viewHelper.showErrorDialog(ex);
			ToastHelper.getInstance().showLongMsg(
					getString(R.string.clerk_txt_delete_fail));
		}
	}

}

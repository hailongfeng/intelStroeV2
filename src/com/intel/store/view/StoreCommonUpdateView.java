package com.intel.store.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.util.ViewHelper;
import com.pactera.framework.controller.BaseController.CommonUpdateViewAsyncCallback;
import com.pactera.framework.exception.IException;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.util.Loger;

public abstract class StoreCommonUpdateView<Result> extends
		CommonUpdateViewAsyncCallback<Result> {
	protected ViewHelper viewHelper;
	private Context context;

	public StoreCommonUpdateView(Context context) {
		super();
		this.context = context;
		viewHelper = new ViewHelper(this.context);
	}

	@Override
	public void onException(IException ie) {
		if (ie instanceof ServerException) {
			ServerException se = (ServerException) ie;
			if (se.getErrorCode() != null
					&& se.getErrorCode().equalsIgnoreCase("111111")) {
				Loger.e("session超时处理");

				OnClickListener positiveListener = new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						((StoreApplication) StoreApplication.getApp())
								.clearActivitys();
						Intent loginActivity = new Intent(context,
								LoginActivity.class);
						dialog.dismiss();
						context.startActivity(loginActivity);
					}
				};
				viewHelper.showErrorDialog2(ie, positiveListener, null);

			} else {
				handleException(ie);
			}
		} else if (ie instanceof TimeoutException) {
			ie = new TimeoutException(context.getResources().getString(
					R.string.comm_request_timeout), ie);
			handleException(ie);
		} else if (ie instanceof NetworkException) {
			ie = new NetworkException(context.getResources().getString(
					R.string.comm_txt_server_error), ie);
			handleException(ie);
		} else {
			handleException(ie);
		}
	}

	public void handleException(IException ex) {
		Loger.e(ex.getMessage());
	}

}

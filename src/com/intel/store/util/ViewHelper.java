package com.intel.store.util;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.StoreController;
import com.intel.store.model.SalesReportModel;
import com.intel.store.view.StoreCommonUpdateView;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PhoneStateUtil;

public class ViewHelper {
	private Context context;
	public TextView textView;

	public ViewHelper(Context context) {
		this.context = context;
	}

	/**
	 * Save the file name of SharedPreference session
	 * 
	 * @Title: showBTNDialog
	 * @param icon
	 * @param title
	 * @param msg
	 * @param btn1_content
	 * @param positiveListener
	 * @param onCancelListener
	 */
	public void showBTNDialog(String msg, String btn1_content,
			DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnCancelListener onCancelListener) {
		if (context == null) {
			return;
		}
		try {

			new AlertDialog.Builder(context).setMessage(msg)
					.setPositiveButton(btn1_content, positiveListener)
					.setOnCancelListener(onCancelListener).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dialog box pop-up message (2 button)
	 * 
	 * @Title: showBTN2Dialog
	 * @param icon
	 * @param title
	 * @param msg
	 * @param btn1_content
	 * @param btn2_content
	 * @param positiveListener
	 * @param negativeListener
	 * @param onCancelListener
	 */
	public void showBTN2Dialog(String title, String msg, String btn1_content,
			String btn2_content,
			DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener negativeListener,
			DialogInterface.OnCancelListener onCancelListener) {
		// setCanceledOnTouchOutside
		if (context == null) {
			return;
		}
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setTitle(title).setMessage(msg)
				.setPositiveButton(btn1_content, positiveListener)
				.setNegativeButton(btn2_content, negativeListener)
				.setOnCancelListener(onCancelListener).create();
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
	}

	public void showBTN1Dialog(String msg, String btn1_content,
			DialogInterface.OnClickListener neutralListener) {
		if (context == null) {
			return;
		}
		try {
			new AlertDialog.Builder(context).setMessage(msg)
					.setNeutralButton(btn1_content, neutralListener).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showBTNDialog(String msg) {
		if (context == null) {
			return;
		}
		try {

			new AlertDialog.Builder(context)
					.setMessage(msg)
					.setPositiveButton(
							context.getResources().getString(R.string.comm_ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();
								}
							}).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showBTN1Dialog(String msg, final Activity activity) {
		if (context == null) {
			return;
		}
		try {
			new AlertDialog.Builder(context)
					.setMessage(msg)
					.setPositiveButton(
							context.getResources().getString(R.string.comm_ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									activity.finish();
								}
							})
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();
									activity.finish();
								}
							}).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showBTNDialog(String msg, String title) {
		if (context == null) {
			return;
		}
		try {
			new AlertDialog.Builder(context)
					.setTitle(title)
					.setMessage(msg)
					.setPositiveButton(
							context.getResources().getString(R.string.comm_ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();
								}
							}).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showErrorDialog(IException ie,
			DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener onCancelListener) {
		if (context == null) {
			return;
		}
		try {
			String msg = null;
			String title =context.getString(R.string.comm_txt_abnormal);
			if (ie instanceof ServerException) {
				msg = ie.getMessage();
			} else if (ie instanceof TimeoutException) {
				msg = context.getString(R.string.comm_request_timeout);
			} else {
				msg = context.getString(R.string.comm_no_internet);
			}
			if (!PhoneStateUtil.hasInternet()) {
				msg = context.getString(R.string.comm_txt_check_network);
				positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = null;
						// 判断手机系统的版本 即API大于10 就是3.0或以上版本
						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent = new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						} else {
							intent = new Intent();
							ComponentName component = new ComponentName(
									"com.android.settings",
									"com.android.settings.WirelessSettings");
							intent.setComponent(component);
							intent.setAction("android.intent.action.VIEW");
						}
						context.startActivity(intent);
					}
				};
				new AlertDialog.Builder(context)
						.setTitle(title)
						.setMessage(msg)
						.setPositiveButton(
								context.getResources().getString(
										R.string.comm_ok), positiveListener)
						.setNegativeButton(
								context.getResources().getString(
										R.string.comm_back), onCancelListener)
						.show();
			} else {
				new AlertDialog.Builder(context)
						.setTitle(title)
						.setMessage(msg)
						.setPositiveButton(
								context.getResources().getString(
										R.string.comm_retry), positiveListener)
						.setNegativeButton(
								context.getResources().getString(
										R.string.comm_back), onCancelListener)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * session超时异常
	 * 
	 * @param ie
	 * @param positiveListener
	 * @param onCancelListener
	 */
	public void showErrorDialog2(IException ie,
			DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener onCancelListener) {
		if (context == null) {
			return;
		}
		try {
			String msg = null;
			String title = context.getString(R.string.comm_txt_abnormal);
			msg = ie.getMessage();
			AlertDialog dialog = new AlertDialog.Builder(context)
					.setTitle(title)
					.setMessage(msg)
					.setPositiveButton(
							context.getResources().getString(R.string.comm_ok),
							positiveListener).create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showErrorDialog(IException ie) {
		if (context == null) {
			return;
		}
		try {

			String msg = null;
			if (ie instanceof ServerException) {
				msg = ie.getMessage();
			} else if (ie instanceof TimeoutException) {
				msg = context.getString(R.string.comm_request_timeout);
			} else {
				msg = context.getString(R.string.comm_no_internet);
			}
			new AlertDialog.Builder(context)
					.setMessage(msg)
					.setPositiveButton(
							context.getResources()
									.getString(R.string.comm_back),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();
								}
							}).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showBarcodeDialog(String barcode) {
		if (context == null) {
			return;
		}
		try {
			StoreController storeController = new StoreController();
			storeController.validateBarcode(new ValidateBarcodeUpdateView(
					context, barcode), barcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Activity getRootContext(Activity activity) {
		while (activity.getParent() != null) {
			activity = activity.getParent();
		}
		return activity;
	}

	public ProgressDialog getProgressDialog(String message) {
		ProgressDialog mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setMessage(message);
		mProgressDialog.setIndeterminate(false);// 设置进度条是否为不明确
		return mProgressDialog;
	}

	class ValidateBarcodeUpdateView extends
			StoreCommonUpdateView<List<MapEntity>> {
		private LoadingView loadingView;
		private String mBarcode;
		private View mView;
		private TextView mTextView;

		public ValidateBarcodeUpdateView(Context context, String barcode) {
			super(context);
			mBarcode = barcode;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = inflater.inflate(R.layout.alert_scan_dialog, null);
			ImageView imageView = (ImageView) mView.findViewById(R.id.iv);
			imageView.setImageBitmap(StoreApplication.bitmapCache
					.getBitmap(mBarcode));
			loadingView = (LoadingView) mView
					.findViewById(R.id.common_id_ll_loading);
			mTextView = (TextView) mView.findViewById(R.id.tv_result);
			new AlertDialog.Builder(context)
					.setTitle(mBarcode)
					.setView(mView)
					.setPositiveButton(
							context.getResources().getString(R.string.comm_ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							})
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								@Override
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();
								}
							}).show();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(List<MapEntity> result) {
			loadingView.hideLoading();
			String model = context.getResources().getString(
					R.string.sales_reporte_result_content);
			if (result.size() > 0)
				model = result.get(0).getString(SalesReportModel.EXTRNL_PRD_NM);
			mTextView.setText(context.getString(R.string.txt_check_result) + model);
		}

		@Override
		public void handleException(IException ex) {
			Loger.d(ex.getMessage());
			loadingView.hideLoading();
			viewHelper.showBTNDialog(ex.getMessage());
		}
	}
}
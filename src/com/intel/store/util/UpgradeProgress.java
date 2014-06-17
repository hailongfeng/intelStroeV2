package com.intel.store.util;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.intel.store.model.CheckVersionModel;
import com.intel.store.service.UpgradeService;
import com.intel.store.view.StoreCommonUpdateView;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PhoneStateUtil;
import com.pactera.framework.util.ToastHelper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;

public class UpgradeProgress {
	private ViewHelper mViewHelper;
	private LoadingView loadingView;
	private StoreController storeController;
	private String upgradeApkUrl;
	private String newVersionLog;
	private boolean hasNewVersion = false;
	private boolean isAuto;
	private Activity contextActivity;
	private static	UpgradeProgress instance;
	private MyBroadcastReceiver broadcastReceiver;

	/**
	 * @return UpgradeProgress 单例
	 */
	public static synchronized UpgradeProgress getInstance(){
		if (instance==null) {
			synchronized (UpgradeProgress.class) {
				if (instance==null) {
					instance=new UpgradeProgress();
				}
			}
		}
		return instance;
	}
	//适用于不同activity中检测更新
	public synchronized void checkVersion(Activity activity,Boolean isAuto) {
		this.isAuto=isAuto;
		if (broadcastReceiver!=null) {
			ToastHelper.getInstance().showLongMsg("已在处理更新中");
			return ;
		}
		this.contextActivity = activity;
		initBroadcast();
		mViewHelper = new ViewHelper(contextActivity);
		loadingView = (LoadingView) contextActivity
				.findViewById(R.id.common_id_ll_loading);
		storeController = new StoreController();
		boolean hasSDCard = android.os.Environment.MEDIA_MOUNTED
				.equals(android.os.Environment.getExternalStorageState());
		if (!PhoneStateUtil.hasInternet() || !hasSDCard) {
			if (!hasSDCard) {
				mViewHelper.showBTNDialog(contextActivity.getResources()
						.getString(R.string.text_check_net_sdcard));
			}
			if (!PhoneStateUtil.hasInternet()) {
				mViewHelper.showBTNDialog("请检查手机网络");
			}
		} else {
			storeController.checkVersion(new CheckNewVersionUpdateView(this.contextActivity));
		}

	};

	private class CheckNewVersionUpdateView extends
			StoreCommonUpdateView<MapEntity> {

		public CheckNewVersionUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(MapEntity result) {
			hasNewVersion = !Boolean.parseBoolean(result
					.getString(CheckVersionModel.RESULT));
			Loger.v("hasNewVersion:" + hasNewVersion);
			if (hasNewVersion) {
				upgradeApkUrl = result.getString(CheckVersionModel.FILEURL);
				newVersionLog = "版本号："
						+ result.getString(CheckVersionModel.VERSION) + "\n"
						+ "更新内容：\n"
						+ result.getString(CheckVersionModel.UPDATENOTE) + "\n";
			}
			// closeWaitDialog();
			loadingView.hideLoading();
			checkCurrentStatus();
		}

		@Override
		public void handleException(IException ex) {
			loadingView.hideLoading();
			viewHelper.showErrorDialog(ex);
			contextActivity.getApplicationContext().unregisterReceiver(broadcastReceiver);
			broadcastReceiver=null;
		}
	}

	private void checkCurrentStatus() {
		// 已经是最新版本,且是手动点击更新的
		if (!hasNewVersion&&!this.isAuto) {
			mViewHelper.showBTNDialog(contextActivity
					.getString(R.string.upgrade_lastest_version));
			contextActivity.getApplicationContext().unregisterReceiver(broadcastReceiver);
			broadcastReceiver=null;
		}
		// 已经是最新版本,自动更新的，不显示最高版本
		if (!hasNewVersion&&this.isAuto) {
			contextActivity.getApplicationContext().unregisterReceiver(broadcastReceiver);
			broadcastReceiver=null;
		}
		// 有新版本，给出提示
		if (hasNewVersion) {
			// 确认开始更新按钮
			OnClickListener positiveListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent updateIntent = new Intent(contextActivity,
							UpgradeService.class);
					updateIntent.putExtra("titleId", R.string.app_name);
					updateIntent.putExtra("upgradeApkUrl", upgradeApkUrl);
					Loger.d("======upgradeApkUrl======" + upgradeApkUrl);
					contextActivity.startService(updateIntent);
				}

			};
			OnClickListener negativeListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 本次不再提醒更新
					StoreSession.setSuggestUpdate(false);
					if (broadcastReceiver!=null) {
						contextActivity.getApplicationContext().unregisterReceiver(broadcastReceiver);
					}
					broadcastReceiver=null;
				}

			};
			OnCancelListener cancleListener=new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					contextActivity.getApplicationContext().unregisterReceiver(broadcastReceiver);
					broadcastReceiver=null;
				}
			};
			try {
				mViewHelper.showBTN2Dialog(contextActivity.getString(R.string.upgrade_notification_title), newVersionLog, contextActivity.getString(R.string.upgrade_btn_upgrade), contextActivity.getString(R.string.upgrade_btn_nexttime),
						positiveListener, negativeListener, cancleListener);
			} catch (Exception e) {
				Loger.e(e.getMessage());
			}

		}
	}
	private class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(UpgradeService.BROADCAST_ACTION)) {
				if (intent.getBooleanExtra("downloadDone", false)) {
					ToastHelper.getInstance().showLongMsg(contextActivity.getString(R.string.upgrade_success));
					contextActivity.getApplicationContext().unregisterReceiver(broadcastReceiver);
					broadcastReceiver=null;
				}

			}
		}

	}
	private void initBroadcast() {
		// 生成广播处理
			if (broadcastReceiver!=null) {
				contextActivity.getApplicationContext().unregisterReceiver(broadcastReceiver);
			}
			broadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(
				UpgradeService.BROADCAST_ACTION);
		contextActivity.getApplicationContext().registerReceiver(broadcastReceiver,
				intentFilter);
	}
}

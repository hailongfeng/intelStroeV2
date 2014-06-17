package com.intel.store;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;

import com.intel.store.util.StoreSession;
import com.pactera.framework.imgload.AsyncImageLoader;
import com.pactera.framework.imgload.BitmapCache;
import com.pactera.framework.util.ConfigProperties;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.MyApplication;
import com.pactera.framework.util.PictureUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 项目名称：Store 类名称：StoreApplication 类描述： 创建人：fenghl 创建时间：2013年10月10日 下午9:29:06
 * 修改人：Administrator 修改时间：2013年12月24日 下午5:31:04 修改备注：添加所有生成的Activity，供程序退出使用。
 * version 1.0.0.0
 */
public class StoreApplication extends MyApplication {

	// 百度定位所需KEY
	//private String KEY = "EeE7ZPGBl9ky3WQv4dyUsuep";
	public static AsyncImageLoader asyncImageLoader = null;
	private List<Activity> activities = new ArrayList<Activity>();
	public static BitmapCache bitmapCache;
	public static Boolean isFinished = false;

	@Override
	public void onCreate() {

		super.onCreate();
		//if (mLocationClient != null)
			//mLocationClient.setAK(KEY);
		com.umeng.common.Log.LOG = true;
		MobclickAgent.openActivityDurationTrack(false);
		asyncImageLoader = new AsyncImageLoader(null, 3, 30);
		String pics_dir = ConfigProperties.PICS_DIR;
		Loger.d("pics_dir图片下载缓存目录=-------------->" + pics_dir);
		String filePath = PictureUtil.getAlbumDir().getAbsolutePath();
		Loger.d("filePath相机拍照后压缩过的路径=-------------->" + filePath);
		StoreSession.setSuggestUpdate(true);
	}

	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		isFinished = true;
		clearActivitys();
		if (bitmapCache != null) {
			bitmapCache.clearMemoryCache();
			bitmapCache = null;
		}
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
				.cancelAll();
		activities = null;
		asyncImageLoader.clearWorks();
		asyncImageLoader.stopThreads();
		asyncImageLoader = null;
		System.exit(0);
	}

	public void clearActivitys() {
		for (Activity activity : activities) {
			if (activity != null) {
				Loger.d(activity.toString());
				activity.finish();
			}
		}
	}

	public static void asyncImageLoaderRecle() {
		asyncImageLoader.stopThreads();
		asyncImageLoader = new AsyncImageLoader(null, 3, 30);
	}

}

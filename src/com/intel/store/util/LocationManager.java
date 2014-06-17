package com.intel.store.util;

import java.util.Timer;
import java.util.TimerTask;


import com.pactera.framework.location.LocationHelper;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PhoneStateUtil;
import com.pactera.framework.util.ToastHelper;

public class LocationManager {
	private static LocationManager instance;
	
	@SuppressWarnings("unused")
	private static final int NO_NETWORK=1;
	private static final int LOC_SUCCESS=2;

	private Timer timer;

	private LocationManager() {
	}

	public static LocationManager getInstance() {
		if (instance == null) {
			instance = new LocationManager();
		}
		return instance;
	}

	/**
	 * 每隔五分钟定位一次
	 */
	public void locationPerXMins(int xmins) {
		long range = xmins * 60 * 1000;// xmins 分*秒*毫秒
		if (range<5000) {
			range=5000;
		}
		timer=new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				StoreSession.setLocateOkId("false");
				if (PhoneStateUtil.hasInternet()) {
					LocationHelper.getLocationHelper()
							.setOnGetBaiDuCityListener(mOnGetBaiDuCityListener);
					LocationHelper.getLocationHelper().stopGetCityThread();
					LocationHelper.getLocationHelper().startGetCityThread();
				} else {
					ToastHelper.getInstance().showLongMsg("定位时，网络无法连接");
				}
			}
		}, 0, range);
	}
	
	public void locationOnce(LocationHelper.OnGetBaiDuCityListener baiDuCityListener) {
		this.mBaiDuCityListener=baiDuCityListener;
		StoreSession.setLocateOkId("false");
		if (PhoneStateUtil.hasInternet()) {
			LocationHelper.getLocationHelper()
			.setOnGetBaiDuCityListener(mOnGetBaiDuCityListener);
			LocationHelper.getLocationHelper().stopGetCityThread();
			LocationHelper.getLocationHelper().startGetCityThread();
		} else {
			ToastHelper.getInstance().showShortMsg("定位时，网络无法连接");
		}
	}

	private LocationHelper.OnGetBaiDuCityListener mBaiDuCityListener;
	
	private LocationHelper.OnGetBaiDuCityListener mOnGetBaiDuCityListener = new LocationHelper.OnGetBaiDuCityListener() {
		@Override
		public void onGetBaiDuCityErr() {
			if (mBaiDuCityListener!=null) {
				mBaiDuCityListener.onGetBaiDuCityErr();
			}
		}

		@Override
		public void onGetBaiDuCityOk(String sLatitude, String sLongitude,
				String province, String city, String district, String street,
				String streetNumber, String addrStr) {
			StoreSession.setLatitude(sLatitude);
			StoreSession.setLongitude(sLongitude);
			Loger.d("list view .  currentAddr = " + province + "===" + city
					+ "===" + district + "===" + street + "===" + streetNumber+"addrStr"+addrStr);
			StoreSession.setCurrentAddress(addrStr);
			StoreSession.setProvince(province);
			StoreSession.setCityName(city);
			StoreSession.setDistrict(district);
			StoreSession.setStreet(streetNumber);
			StoreSession.setStreetNumber(streetNumber);
			StoreSession.setLocateOkId("true");
			StoreSession.setLastLocationTime(System.currentTimeMillis() + "");
			
			if (mBaiDuCityListener!=null) {
				mBaiDuCityListener.onGetBaiDuCityOk(sLatitude, sLongitude, province, city, district, street, streetNumber, addrStr);
			}
			ToastHelper.getInstance().showLongMsg("定位成功！");
		}
	};

	public void stopLocation() {
		LocationHelper.getLocationHelper().stopGetCityThread();
		if (timer != null) {
			timer.cancel();
		}
	}
}
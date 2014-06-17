package com.intel.store.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.pactera.framework.util.Loger;

public class LocationActivity extends Activity {

	private TextView txt_current_local;
	private LocationClient mLocClient;
	public MyLocationListenner myListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		txt_current_local = (TextView) findViewById(R.id.txt_current_local);
		myListener = new MyLocationListenner();
		mLocClient =StoreApplication.mLocationClient;
		mLocClient.registerLocationListener(myListener);
		try {
			setLocationOption();
			mLocClient.start();
			if (mLocClient != null && mLocClient.isStarted())
				mLocClient.requestLocation();
			else
				Loger.d("locClient is null or not started");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				/**
				 * 格式化显示地址信息
				 */
				sb.append("\n"+getString(R.string.txt_province)+"省：");
				sb.append(location.getProvince());
				Loger.d(location.getProvince());
				sb.append("\n"+getString(R.string.txt_city)+"：");
				sb.append(location.getCity());
				Loger.d(location.getCity());
				sb.append("\n"+getString(R.string.txt_county)+"：");
				sb.append(location.getDistrict());
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
			sb.append("\nsdk version : ");
			sb.append(mLocClient.getVersion());
			sb.append("\nisCellChangeFlag : ");
			sb.append(location.isCellChangeFlag());
			//Loger.d(sb.toString());
			//Loger.d(sb.toString());
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
			//Loger.d(sb.toString());
		}
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setScanSpan(3000);
		option.setPoiNumber(5);
		option.setPoiDistance(1000);
		option.disableCache(true);
		option.setPoiExtraInfo(true);
		mLocClient.setLocOption(option);
	}


}

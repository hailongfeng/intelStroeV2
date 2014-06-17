package com.intel.store.util;

import android.app.Application;
import android.provider.Settings;
import android.util.Log;

import com.intel.store.StoreApplication;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.Session;
import com.pactera.framework.util.ToastHelper;

public class StoreSession extends Session {

	/**	网络线路选择 */
	public static final String KEY_LINE = "line";
	public static final String KEY_JSESSION_ID = "jsessionid";
	/** irep登录Session */
	public static final String KEY_IREP_SESSION_ID = "irep_ssionid";
	/** irep登录状态 */
	public static final String KEY_IREP_SUCCESSED = "irep_successed";
	/** irep密码 */
	public static final String KEY_IREP_PASSWORD = "irep_password";
	/** irep用户名 */
	public static final String KEY_IREP_NAME = "irep_name";
	/** 登录账号 */
	public static final String KEY_ACCOUNT = "account";
	/** 密码 */
	public static final String KEY_PASSWORD = "password";
	/** 用户名 */
	public static final String KEY_NAME = "name";
	/** role名称 */
	public static final String KEY_ROLENAME = "role_name";
	/** role id */
	public static final String KEY_ROLE_ID = "rep_asgn_role_id";
	/** 选择的门店ID */
	public static final String KEY_CURRENTSTOREID = "current_storeid";
	/** 选择的门店Name */
	private static final String KEY_CURRENTSTORENAME = "current_storename";
	/** 选择的门店类型 */
	public static final String KEY_CURRENT_STOREROLE = "current_storerole";
	/** 选择的门店城市类型 */
	public static final String KEY_CURRENT_CITY_TYPE = "current_cityType";
	/** 是否自动登录 */
	public static final String KEY_USER_LOGIN_AUTO = "user_login_auto";
	/** 是否记住密码 */
	public static final String KEY_REMENBER_ACCOUNT = "remember_account";
	/** 是否开启定位 */
	public static final String KEY_GPS_STATUS = "gps_status";
	/** 是否同意隐私声明 */
	public static final String KEY_ACCEPT_PRIVATE = "accept_private";
	/** 销售话术版本 */
	public static final String KEY_SALES_WORD_VERSION = "sales_word";
	/** 经理的ID */
	public static final String KEY_REP_ID = "rep_id";
	/*  *//** 软件是否自动提醒更新 */
	/*
	 * public static final String KEY_AUTO_UPDATE = "auto_update";
	 */
	/**DIY的编号是77 */
	public static final String DIY = "77";
	/**OEM的编号是78 */
	public static final String OEM = "78";
	
	/** 软件是否提醒更新 */
	public static final String KEY_SUGGEST_UPDATE = "suggest_update";
	/** 上次更新的时间 */
	public static final String KEY_LAST_SYNC_DATA_TIME = "last_sync_data_time";
	// 定位相关
	public static final String KEY_LOCATE_OK_ID = "locate_ok_id";
	public static final String KEY_CURRENT_ADDR = "current_addr";
	public static final String KEY_PROVINCE_NAME = "province_name";
	public static final String KEY_CITY_NAME = "city_name";
	public static final String KEY_DISTRICT_NAME = "district_name";
	public static final String KEY_STREET_NAME = "street_name";
	public static final String KEY_STREET_NUMBER_NAME = "streetNumber_name";
	/** 上次定位的时间 */
	public static final String KEY_LAST_LOCATION_TIME = "last_location_time";
	private static String KEY_TELEPHONE = "telephone";
	private static String KEY_EMAIL = "email";
	public static Boolean STORE_INFO_CHANGED =false;

	public static void setLine(String line) {
		setString(KEY_LINE, line);
	}

	public static String getLine() {
		String line = getString(KEY_LINE, Constants.LINE_DX);
		Loger.i(line);
		return line;
	}

	public static void setIrepStatus(Boolean value) {
		sessionSp.getEditor().putBoolean(KEY_IREP_SUCCESSED, value).commit();
	}

	public static boolean getIrepStatus() {
		return sessionSp.getPreferences().getBoolean(KEY_IREP_SUCCESSED, false);
	}

	public static void setIrepSessionId(String sessionId) {
		setString(KEY_IREP_SESSION_ID, sessionId);
	}

	public static String getIrepSessionId() {
		return getString(KEY_IREP_SESSION_ID, "");
	}

	public static void setIrepUsername(String username) {
		setString(KEY_IREP_NAME, username);
	}

	public static String getIrepUsername() {
		return getString(KEY_IREP_NAME, "");
	}

	public static void setIrepPassword(String passowrd) {
		try {
			setString(KEY_IREP_PASSWORD,
					AESEncrytor.encrypt(getIrepUsername(), passowrd));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getIrepPassword() {
		String getIrepPassword = null;
		try {
			getIrepPassword = AESEncrytor.decrypt(getIrepUsername(),
					getString(KEY_IREP_PASSWORD, ""));
		} catch (Exception e) {
			Loger.e("AES解密异常");
			ToastHelper.getInstance().showShortMsg("AES解密异常,重新输入密码");
			e.printStackTrace();
		}
		return getIrepPassword;
	}

	public static void setJsessionId(String jsessionId) {
		setString(KEY_JSESSION_ID, jsessionId);
	}

	public static String getJsessionId() {
		return getString(KEY_JSESSION_ID, "");
	}

	public static void setRoleId(String rep_asgn_role_id) {
		setString(KEY_ROLE_ID, rep_asgn_role_id);
	}

	public static String getRoleId() {
		return getString(KEY_ROLE_ID, "");
	}

	public static void setAccount(String account) {
		setString(KEY_ACCOUNT, account);
	}

	public static String getAccount() {
		return getString(KEY_ACCOUNT, "");
	}

	public static void setPassword(String passowrd) {
		try {
			setString(KEY_PASSWORD,
					AESEncrytor.encrypt(getName(), passowrd));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPassword() {
		String getPassword = null;
		try {
			getPassword = AESEncrytor.decrypt(getName(),
					getString(KEY_PASSWORD, ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getPassword;
	}

	public static void setName(String strName) {
		setString(KEY_NAME, strName);
	}

	public static String getName() {
		return getString(KEY_NAME, "");
	}

	public static void setRoleName(String strName) {
		setString(KEY_ROLENAME, strName);
	}

	public static String getRoleName() {
		return getString(KEY_ROLENAME, "");
	}

	public static void setCurrentStoreId(String storeId) {
		setString(KEY_CURRENTSTOREID, storeId);
	}

	public static String getCurrentStoreId() {
		return getString(KEY_CURRENTSTOREID, "");
	}
	
	public static void setCurrentStoreNAME(String storeName) {
		setString(KEY_CURRENTSTORENAME, storeName);
	}
	
	public static String getCurrentStoreNAME() {
		return getString(KEY_CURRENTSTORENAME, "");
	}

	public static void setCurrentCityTypeName(String cityType) {
		setString(KEY_CURRENT_CITY_TYPE, cityType);
	}

	public static String getCurrentCityTypeName() {
		return getString(KEY_CURRENT_CITY_TYPE, "");
	}

	public static void setCurrentStoreRole(String storeRole) {
		setString(KEY_CURRENT_STOREROLE, storeRole);
	}

	public static String getCurrentStoreRole() {
		return getString(KEY_CURRENT_STOREROLE, "");
	}

	public static void setUserLoginAuto(boolean userLoginAuto) {
		sessionSp.getEditor().putBoolean(KEY_USER_LOGIN_AUTO, userLoginAuto)
				.commit();
	}

	public static boolean getUserLoginAuto() {
		return sessionSp.getPreferences()
				.getBoolean(KEY_USER_LOGIN_AUTO, false);
	}

	public static void setSuggestUpdate(boolean autoUpdate) {
		sessionSp.getEditor().putBoolean(KEY_SUGGEST_UPDATE, autoUpdate)
				.commit();
	}

	public static boolean getSuggestUpdate() {
		return sessionSp.getPreferences().getBoolean(KEY_SUGGEST_UPDATE, true);
	}

	public static void setRememberAccount(boolean remenberAccount) {
		sessionSp.getEditor().putBoolean(KEY_REMENBER_ACCOUNT, remenberAccount)
				.commit();
	}

	public static boolean getRemenberAccount() {
		return sessionSp.getPreferences().getBoolean(KEY_REMENBER_ACCOUNT,
				false);
	}

	public static void setGPSstatu(boolean status) {
		sessionSp.getEditor().putBoolean(KEY_GPS_STATUS, status).commit();
	}

	public static boolean getGPSstatu() {
		try {
			if (isCheat()) {
				ToastHelper.getInstance().showLongMsg("检测到您有软件影响GPS工作，请关闭相关软件后才能正常定位");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessionSp.getPreferences().getBoolean(KEY_GPS_STATUS, false);
	}

	private static Boolean isCheat(){
		boolean result;
		if (Settings.Secure.getString(StoreApplication.getAppContext().getContentResolver(),
			    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0")) {
			   result = false;
			  } else {
			   result = true;
			  }
		android.location.LocationManager lm = (android.location.LocationManager) StoreApplication.getAppContext().getSystemService(Application.LOCATION_SERVICE);
			  try {
			   Log.d("", "Removing Test providers");
			   lm.removeTestProvider(android.location.LocationManager.GPS_PROVIDER);
			  } catch (IllegalArgumentException error) {
			   Log.d("", "Got exception in removing test  provider");
			  }
			  return result;
	}
	public static void setAcceptPrivate(boolean isAccept) {
		sessionSp.getEditor().putBoolean(KEY_ACCEPT_PRIVATE, isAccept).commit();
	}

	public static boolean getAcceptPrivate() {
		return sessionSp.getPreferences().getBoolean(KEY_ACCEPT_PRIVATE, false);
	}

	public static void setRepID(String repID) {
		setString(KEY_REP_ID, repID);
	}

	public static String getRepID() {
		return getString(KEY_REP_ID, "");
	}

	public static void setDataVersion(String salesTerminologyVersion) {
		setString(KEY_SALES_WORD_VERSION, salesTerminologyVersion);
	}

	public static String getDataVersion() {
		return getString(KEY_SALES_WORD_VERSION, "0");
	}

	public static void setTelephone(String telephone) {
		setString(KEY_TELEPHONE, telephone);
	}

	public static String getTelephone() {
		return getString(KEY_TELEPHONE, "");
	}

	public static void setEmail(String email) {
		setString(KEY_EMAIL, email);
	}

	public static String getEmail() {
		return getString(KEY_EMAIL, "");
	}

	// 定位相關
	public static void setLastSyncTime(String lastSyncTime) {
		setString(KEY_LAST_SYNC_DATA_TIME, lastSyncTime);
	}

	public static String getLastSyncTime() {
		return getString(KEY_LAST_SYNC_DATA_TIME, "");
	}

	public static String getCurrentAddress() {
		return getString(KEY_CURRENT_ADDR, "");
	}

	public static void setCurrentAddress(String currentAddr) {
		setString(KEY_CURRENT_ADDR, currentAddr);
	}

	public static String getProvince() {
		return getString(KEY_PROVINCE_NAME, "");
	}

	public static void setProvince(String province) {
		setString(KEY_PROVINCE_NAME, province);
	}

	public static String getCity() {
		return getString(KEY_CITY_NAME, "");
	}

	public static void setCity(String city) {
		setString(KEY_CITY_NAME, city);
	}

	public static String getDistrict() {
		return getString(KEY_DISTRICT_NAME, "");
	}

	public static void setDistrict(String district) {
		setString(KEY_DISTRICT_NAME, district);
	}

	public static String getStreet() {
		return getString(KEY_STREET_NAME, "");
	}

	public static void setStreet(String street) {
		setString(KEY_STREET_NAME, street);
	}

	public static String getStreetNumber() {
		return getString(KEY_STREET_NUMBER_NAME, "");
	}

	public static void setStreetNumber(String streetNumber) {
		setString(KEY_STREET_NUMBER_NAME, streetNumber);
	}

	public static void setLastLocationTime(String lastLocationTime) {
		setString(KEY_LAST_LOCATION_TIME, lastLocationTime);
	}

	public static String getLastLocationTime() {
		return getString(KEY_LAST_LOCATION_TIME, "");
	}

	public static String getLocateOkId() {
		return getString(KEY_LOCATE_OK_ID, "false");
	}

	public static void setLocateOkId(String strLocateOk) {
		setString(KEY_LOCATE_OK_ID, strLocateOk);
	}

	public static void clearForLogout() {
		StoreSession.setAccount("");
		StoreSession.setPassword("");
		StoreSession.setJsessionId("");
		StoreSession.setLongitude("");
		StoreSession.setLatitude("");
		StoreSession.setRepID("");
		StoreSession.setName("");
		StoreSession.setLastSyncTime("");
		StoreSession.setIrepStatus(false);
		StoreSession.setUserLoginAuto(false);
		StoreSession.setRememberAccount(false);
	}



}

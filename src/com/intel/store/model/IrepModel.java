package com.intel.store.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.dao.remote.IrepRemoteDao;
import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class IrepModel extends StoreBaseModel {

	public static final int currentUser = 1;
	public static final int participantId = 2;
	public static final int currentUID = 3;
	public static final int cultureCode = 4;
	public static final int firstName = 5;
	public static final int lastName = 6;
	public static final int userTypeId = 7;
	public static final int retailer = 8;
	public static final int storeChainId = 9;
	public static final int storeChainName = 10;
	public static final int storeCode = 11;
	public static final int countryCode = 12;
	public static final int userType = 13;
	public static final int geoCode = 14;
	public static final int tier = 15;
	public static final int YYYYQQ = 16;
	public static final int chipCount = 17;
	public static final int lifeTimeChips = 18;
	public static final int verified = 19;
	public static final int email = 20;
	public static final int disclaimerRequired = 21;
	public static final int site = 22;
	public static final int siteId = 23;
	public static final int showAdminLink = 24;
	public static final int active = 25;
	public static final int showNotificationBar = 26;
	public static final int locationId = 27;
	public static final int forcedProfileUpdate = 28;
	public static final int systemIntegration = 29;
	public static final int enrolledDate = 30;
	public static final int lastLoginDate = 31;
	public static final int isAnonymous = 32;
	public static final int isRTL = 33;
	private IrepRemoteDao irepRemoteDao;

	public IrepModel() {
		// remoteStoreListDao = new RemoteStoreListDao();
		 irepRemoteDao = new IrepRemoteDao();
	}

	public Boolean irepLogin(String username, String password)
			throws TimeoutException, NetworkException, ServerException {
		HttpResult httpResult = irepRemoteDao.irepLogin(username, password);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		StoreSession.setIrepStatus(false);
		try {
			JSONObject jsonObject = new JSONObject(response);
			// 验证通过
			if (jsonObject.getString("isValid") != null
					&& jsonObject.getString("isValid").equals("true")) {
				JSONObject userData = (JSONObject) jsonObject.get("userData");
				if (userData != null
						&& userData.getString("currentUID") != null) {
					StoreSession.setIrepSessionId(userData
							.getString("currentUID"));
					StoreSession.setIrepUsername(username);
					StoreSession.setIrepPassword(password);
					StoreSession.setIrepStatus(true);
					return true;
				}else {
					throw new ServerException(StoreApplication.getAppContext().getString(R.string.txt_server_unknow_error), new Exception());
				}
			} else {
				throw new ServerException(StoreApplication.getAppContext().getString(R.string.login_server_error), new Exception());
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

}

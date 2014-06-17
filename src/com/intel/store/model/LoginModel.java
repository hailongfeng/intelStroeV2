package com.intel.store.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import com.intel.store.dao.remote.StoreRemoteDao;
import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class LoginModel extends StoreBaseModel {

	public boolean loginFromRemote(String userName, String password,
			String userType) throws TimeoutException, ServerException,
			NetworkException {
		StoreRemoteDao storeRemoteDao=new StoreRemoteDao();
		String signPassword = Base64.encodeToString(password.getBytes(),
				Base64.DEFAULT);
		HttpResult httpResult = storeRemoteDao.login(userName, signPassword,
				userType);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			JSONObject data = preParseResponse(response);
			JSONObject securityJson = data.getJSONObject("data");
			StoreSession.setJsessionId(securityJson.getString("sessionId"));
			if (!StoreSession.getAccount().equals(userName)) {
				StoreSession.setIrepStatus(false);
				StoreSession.setAccount(userName);
			}
			StoreSession.setName(securityJson.getString("rep_nm"));
			StoreSession.setRoleName(securityJson.getString("rep_asgn_role_nm"));
			StoreSession.setRoleId(securityJson.getString("rep_asgn_role_id"));
			StoreSession.setTelephone(securityJson.getString("rep_tel"));
			StoreSession.setEmail(securityJson.getString("rep_email"));
			StoreSession.setPassword(password);
			StoreSession.setRepID((securityJson.getString("rep_id")));
			return true;
		} catch (JSONException e) {
			Loger.d(e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * logout
	 * 
	 * @throws TimeoutException
	 * @throws NetworkException
	 * @throws DBException
	 * @return
	 */
	public boolean logout() throws DBException, NetworkException {

		//StoreRemoteDao storeRemoteDao=new StoreRemoteDao();
		//HttpResult httpResult = storeRemoteDao.logout();
		//String response = preParseHttpResult(httpResult);
		//Loger.d(response);
		StoreSession.clearForLogout();

		return true;
	}
}

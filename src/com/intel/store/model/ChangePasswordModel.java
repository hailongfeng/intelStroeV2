package com.intel.store.model;

import android.util.Base64;

import com.intel.store.dao.remote.RemoteClerkDao;
import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class ChangePasswordModel extends StoreBaseModel {

	public Boolean changePasswordFromRemote(String userName, String oldpwd,
			String newpwd) throws TimeoutException, ServerException,
			NetworkException {
		String signOldpassword = Base64.encodeToString(oldpwd.toString()
				.getBytes(), Base64.DEFAULT);
		String signNewpassword = Base64.encodeToString(newpwd.toString()
				.getBytes(), Base64.DEFAULT);

		RemoteClerkDao remoteClerkDao = new RemoteClerkDao();
		HttpResult httpResult = remoteClerkDao.changePasswordFromRemote(
				userName, signOldpassword, signNewpassword);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		// JSONObject jsonObject= preParseResponse(response);
		// 修改密码之后操作
		StoreSession.setPassword(newpwd);
		StoreSession.setRememberAccount(false);

		return true;
	}
}

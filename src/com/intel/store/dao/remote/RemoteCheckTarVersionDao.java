package com.intel.store.dao.remote;

import java.util.HashMap;

import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;

public class RemoteCheckTarVersionDao extends StoreRemoteBaseDao {

	public HttpResult checkTarVersion(String version)
			throws TimeoutException, NetworkException {
		final String url = HOST + "checkTarVersionServlet";
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("version", version);
		return intelPost(url, hmParams);
	}
}

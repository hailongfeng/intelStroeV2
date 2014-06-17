package com.intel.store.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.dao.remote.StoreRemoteDao;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class CheckTarVersionModel extends StoreBaseModel {

	public static final int RESULT = 1;
	public static final int VERSION = 2;
	public static final int FILEURL = 3;
	public static final int UPDATENOTE = 4;

	public MapEntity checkTarVersion(String version) throws TimeoutException,
			ServerException, NetworkException {
		StoreRemoteDao storeRemoteDao= new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.checkTarVersion(version);
		String response = preParseHttpResult(httpResult);
		Loger.i(response);
		MapEntity map = new MapEntity();
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONObject jsonData = data.getJSONObject("data");
				map.setValue(RESULT, Boolean.parseBoolean(jsonData.getString("result")));
				map.setValue(VERSION, jsonData.getString("version"));
				map.setValue(FILEURL, jsonData.getString("fileurl"));
				map.setValue(UPDATENOTE, jsonData.getString("updatenote"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

}

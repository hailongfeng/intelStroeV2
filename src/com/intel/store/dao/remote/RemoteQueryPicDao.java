package com.intel.store.dao.remote;

import java.util.HashMap;

import com.intel.store.util.Constants;
import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;

public class RemoteQueryPicDao extends StoreRemoteBaseDao {
	public HttpResult listWvByQuarter(String quarter)
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_ListWvByQuarter;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("role_id", StoreSession.getCurrentStoreRole());
		hmParams.put("city_type_nm", StoreSession.getCurrentCityTypeName());
		hmParams.put("quarter", quarter);
		return intelPost(url, hmParams);
	}
	public HttpResult queryPicRound()
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_QueryCurrentQuarter;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		return intelPost(url, hmParams);
	}
	public HttpResult queryHistoryWv()
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_LISTHISTORYWV;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("role_id", StoreSession.getCurrentStoreRole());
		hmParams.put("city_type_nm", StoreSession.getCurrentCityTypeName());
		return intelPost(url, hmParams);
	}
	public HttpResult listPhotoByWvCategory2(String wvId,String category)
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_LISTHISTORYWV;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("wv_id", wvId);
		hmParams.put("category", category);
		hmParams.put("role_id", StoreSession.getCurrentStoreRole());
		hmParams.put("city_type_nm", StoreSession.getCurrentCityTypeName());
		return intelPost(url, hmParams);
	}
	public HttpResult listPhotoByWvCategory(String wvId,String category) throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_ListPhotoByWvCategory;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("wv_id", wvId);
		hmParams.put("category", category);
		hmParams.put("stor_id", StoreSession.getCurrentStoreId());
		return intelPost(url, hmParams);
	}
	
}

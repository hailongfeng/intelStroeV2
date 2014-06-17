package com.intel.store.dao.remote;

import java.util.HashMap;

import com.intel.store.util.Constants;
import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class RemoteQueryPicCategoryDao extends StoreRemoteBaseDao {

	public HttpResult queryPicCategory() throws TimeoutException, NetworkException {
		final String url = HOST + Constants.PICTURE_CAT_SERVELET;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("sessionId",StoreSession.getJsessionId());
		hmParams.put("city_type_nm",StoreSession.getCurrentCityTypeName());
		hmParams.put("role_id", StoreSession.getCurrentStoreRole());
		Loger.d("url:"+url);
		printParams(hmParams);
		return intelPost(url, hmParams);
	}
	public HttpResult listCategoryByWv(String wvId) throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_ListCategoryByWv;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("wv_id", wvId);
		hmParams.put("stor_id", StoreSession.getCurrentStoreId());
		hmParams.put("role_id", StoreSession.getCurrentStoreRole());
		hmParams.put("city_type_nm", StoreSession.getCurrentCityTypeName());
		return intelPost(url, hmParams);
	
	}

}

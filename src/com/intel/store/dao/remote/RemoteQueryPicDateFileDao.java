package com.intel.store.dao.remote;

import java.util.HashMap;

import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;

public class RemoteQueryPicDateFileDao extends StoreRemoteBaseDao {
	public HttpResult queryPicDateFile(String rep_id  ,String stor_id)
			throws TimeoutException, NetworkException {
		final String url = HOST + "/storeRspMgmt/queryPicDateFile.do";
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("rep_id", "53825");
		hmParams.put("sessionId",StoreSession.getJsessionId());
		hmParams.put("stor_id", stor_id);
		return intelPost(url, hmParams);
	}
}

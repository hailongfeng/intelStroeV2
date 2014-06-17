package com.intel.store.dao.remote;

import java.util.HashMap;

import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;

public class RemoteQueryPicByDateDao extends StoreRemoteBaseDao {

	public HttpResult queryPicByDate(String slsprsId,String last_upd_dtm,String stor_id)
			throws TimeoutException, NetworkException {
		final String url = HOST + "storeRspMgmt/queryPicByDate.do";
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("rep_id", slsprsId);
		hmParams.put("last_upd_dtm", last_upd_dtm);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		hmParams.put("stor_id", stor_id);
		return intelPost(url, hmParams);
	}
}

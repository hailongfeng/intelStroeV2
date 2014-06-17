package com.intel.store.dao.remote;

import java.util.HashMap;

import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;

public class RemoteMessageDao extends StoreRemoteBaseDao {

	public HttpResult countUnReadMsg(String rspId) 
			throws TimeoutException, NetworkException {
		final String url = HOST + "/newStoreRspMgmt/countUnReadMsg.do";
		HashMap<String, String> hmParams=new HashMap<String, String>();
		hmParams.put(rspId, "rsp_id");
		return  intelPost(url, hmParams);
	}
	
	public HttpResult listMessage(String rspId,String statusStr) 
			throws TimeoutException, NetworkException {
		final String url = HOST + "/newStoreRspMgmt/listMessage.do";
		HashMap<String, String> hmParams=new HashMap<String, String>();
		hmParams.put(rspId, "rsp_id");
		hmParams.put(statusStr, "statusStr");
		return  intelPost(url, hmParams);
	}
	
	public HttpResult updateMessageStatus(String rspId,String msg_id) 
			throws TimeoutException, NetworkException {
		final String url = HOST + "/newStoreRspMgmt/updateMessageStatus.do";
		HashMap<String, String> hmParams=new HashMap<String, String>();
		hmParams.put(rspId, "rsp_id");
		hmParams.put(msg_id, "msg_id");
		return  intelPost(url, hmParams);
	}

}

package com.intel.store.dao.remote;

import java.util.HashMap;

import android.util.Base64;

import com.intel.store.util.Constants;
import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class RemoteClerkDao extends StoreRemoteBaseDao {

	public HttpResult getMyClerkList(String repId, String storId)
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_GETCLERKLIST;
		Loger.d("url:" + url);
		HashMap<String, String> hmParams = new HashMap<String, String>();
		Loger.d("RemoteGetMyClerkList dao. url = " + url + ", rep_id = "
				+ repId + ", stor_id = " + storId);
		hmParams.put("rep_id", repId);
		hmParams.put("stor_id", storId);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		return intelPost(url, hmParams);
	}
	public HttpResult addClerk(String storId,String rep_nm,String rep_tel,String rep_email,String last_upd_usr_id) throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_ADDRSP;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		rep_nm=new String(Base64.encode(rep_nm.getBytes(),Base64.DEFAULT));
		rep_email=new String(Base64.encode(rep_email.getBytes(),Base64.DEFAULT));
		hmParams.put("stor_id",storId);
		hmParams.put("rep_nm", rep_nm);
		hmParams.put("rep_tel",rep_tel);
		hmParams.put("rep_email", rep_email);
		hmParams.put("last_upd_usr_id", last_upd_usr_id);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		Loger.d("url:"+url);
		return intelPost(url, hmParams);
	}
	public HttpResult modefyClerk(String rsp_id,String rep_tel,String rep_email,String operate_rsp_id) throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_MDFRSP;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		rep_email=new String(Base64.encode(rep_email.getBytes(),Base64.DEFAULT));
		hmParams.put("rsp_id", rsp_id);
		hmParams.put("rep_tel",rep_tel);
		hmParams.put("rep_email", rep_email);
		hmParams.put("operate_rsp_id", operate_rsp_id);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		Loger.d("url:"+url);
		return	intelPost(url, hmParams);
	}
	public HttpResult delClerk(String rsp_id,String operate_rsp_id) throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_DELRSP;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("rsp_id",rsp_id);
		hmParams.put("operate_rsp_id", operate_rsp_id);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		Loger.d("url:"+url);
		return	intelPost(url, hmParams);
	}


	public HttpResult changePasswordFromRemote(String userName,
			String oldpwd, final String newpwd) throws TimeoutException,
			NetworkException {
		final String url = HOST + Constants.API_CHANGE_PASSWORD;
		Loger.d("url:" + url);
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("username", userName);
		hmParams.put("password", oldpwd);
		hmParams.put("newPwd", newpwd);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		return intelPost(url, hmParams);
	}

}

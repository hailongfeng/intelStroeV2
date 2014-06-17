package com.intel.store.dao.remote;

import java.util.HashMap;

import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class RemotePersonalInfoDao extends StoreRemoteBaseDao{
 
	public HttpResult addPersonalInfo(String userName,String password) throws TimeoutException, NetworkException{
		final String url = HOST + "loginServlet";
		HashMap<String, String> hmParams = new HashMap<String, String>();
		Loger.d("login dao. url = " + url + ", username = " + userName + ", password = " + password);
		hmParams.put("userName", userName);
		hmParams.put("password", password);
		return intelPost(url, hmParams);
	}
	public HttpResult PersonalInfo(String userName,String password) throws TimeoutException, NetworkException{
		final String url = HOST + "loginServlet";
		HashMap<String, String> hmParams = new HashMap<String, String>();
		Loger.d("login dao. url = " + url + ", username = " + userName + ", password = " + password);
		hmParams.put("userName", userName);
		hmParams.put("password", password);
		return intelPost(url, hmParams);
		
	}
}

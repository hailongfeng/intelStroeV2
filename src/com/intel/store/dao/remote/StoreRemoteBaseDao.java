package com.intel.store.dao.remote;

import java.util.HashMap;

import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpPostUtil;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.http.HttpsPostUtil;
import com.pactera.framework.model.RemoteBaseDao;
import com.pactera.framework.util.ConfigProperties;
import com.pactera.framework.util.InputChecker;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.Utils;

public class StoreRemoteBaseDao extends RemoteBaseDao {
	public static String STR_HOST = StoreSession.getLine();
	public static String HOST = Utils.getNetConfigProperties().getProperty(
			STR_HOST);
	public static final String STR_PICTURE_HOST = "pictureHost";
	public static final String PICTURE_HOST = Utils.getNetConfigProperties()
			.getProperty(STR_PICTURE_HOST);

	protected static boolean isUseHttpsPost = false;

	static {
		isUseHttpsPost = "https".equals(Utils.getNetConfigProperties()
				.getProperty("httpOrhttps"));
	}

	protected HttpResult intelPost(String url, HashMap<String, String> hmParams)
			throws TimeoutException, NetworkException {
		if (InputChecker.isEmpty(hmParams.get("sessionId"))) {
			hmParams.put("sessionId", StoreSession.getJsessionId());
		}
		if (Boolean.parseBoolean(ConfigProperties.LOGER)) {
			String tempurl = url + "?";
			int i = 0;
			for (String key : hmParams.keySet()) {
				Loger.d(key + "=" + hmParams.get(key));
				if (i == 0) {
					tempurl += key + "=" + hmParams.get(key);
				} else {
					tempurl += "&" + key + "=" + hmParams.get(key);
				}
				i++;
			}
			Loger.d(tempurl);
		}
		if (isUseHttpsPost) {
			return new HttpsPostUtil().executeRequest(url, hmParams);
		} else {
			return new HttpPostUtil().executeRequest(url, hmParams);
		}
	}

	protected void printParams(HashMap<String, String> hmParams) {
		for (String key : hmParams.keySet()) {
			Loger.d(key + "=" + hmParams.get(key));
		}
	}

}
